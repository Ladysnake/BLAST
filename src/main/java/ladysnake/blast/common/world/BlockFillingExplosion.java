package ladysnake.blast.common.world;

import com.mojang.datafixers.util.Pair;
import eu.pb4.common.protection.api.CommonProtection;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockFillingExplosion extends CustomExplosion {
    public BlockState blockStateToPlace;

    public BlockFillingExplosion(World world, Entity entity, double x, double y, double z, float power, BlockState blockStateToPlace) {
        super(world, entity, x, y, z, power, null, DestructionType.DESTROY);
        this.blockStateToPlace = blockStateToPlace;
    }

    @Override
    public void affectWorld(boolean boolean_1) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
        boolean boolean_2 = this.destructionType != DestructionType.KEEP;
        if (this.power >= 2.0F && boolean_2) {
            this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        } else {
            this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }

        Iterator var3;
        BlockPos blockPos;
        if (boolean_2) {
            var3 = this.affectedBlocks.iterator();
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();

            while (var3.hasNext()) {
                blockPos = (BlockPos) var3.next();
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block_1 = blockState.getBlock();

                if (canPlace(blockPos) && canExplode(blockPos)) {
                    if (boolean_1) {
                        double double_1 = (float) blockPos.getX() + this.world.random.nextFloat();
                        double double_2 = (float) blockPos.getY() + this.world.random.nextFloat();
                        double double_3 = (float) blockPos.getZ() + this.world.random.nextFloat();
                        double double_4 = double_1 - this.x;
                        double double_5 = double_2 - this.y;
                        double double_6 = double_3 - this.z;
                        double double_7 = Math.sqrt(double_4 * double_4 + double_5 * double_5 + double_6 * double_6);
                        double_4 /= double_7;
                        double_5 /= double_7;
                        double_6 /= double_7;
                        double double_8 = 0.5D / (double_7 / (double) this.power + 0.1D);
                        double_8 *= this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3F;
                        double_4 *= double_8;
                        double_5 *= double_8;
                        double_6 *= double_8;
                        if (blockState.getFluidState().isEmpty()) {
                            this.world.addParticle(ParticleTypes.POOF, (double_1 + this.x) / 2.0D, (double_2 + this.y) / 2.0D, (double_3 + this.z) / 2.0D, double_4, double_5, double_6);
                            this.world.addParticle(ParticleTypes.SMOKE, double_1, double_2, double_3, double_4, double_5, double_6);
                        } else {
                            this.world.addParticle(ParticleTypes.BUBBLE, (double_1 + this.x) / 2.0D, (double_2 + this.y) / 2.0D, (double_3 + this.z) / 2.0D, double_4, double_5, double_6);
                            this.world.addParticle(ParticleTypes.BUBBLE_POP, double_1, double_2, double_3, double_4, double_5, double_6);
                        }
                    }

                    if (blockState.isAir() || !blockState.getFluidState().isEmpty() || blockState.getBlock() instanceof FluidFillable) {
                        if (block_1.shouldDropItemsOnExplosion(this) && this.world instanceof ServerWorld) {
                            BlockEntity blockEntity = this.world.getBlockEntity(blockPos) != null ? this.world.getBlockEntity(blockPos) : null;
                            ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld) world)
                                    .add(LootContextParameters.ORIGIN, Vec3d.of(blockPos))
                                    .add(LootContextParameters.TOOL, itemStack)
                                    .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity)
                                    .addOptional(LootContextParameters.THIS_ENTITY, this.entity)
                                    .luck(this.world.random.nextFloat());

                            if (this.destructionType == DestructionType.DESTROY)
                                builder.add(LootContextParameters.EXPLOSION_RADIUS, this.power);

                            BlockPos finalBlockPos = blockPos;
                            blockState.getDroppedStacks(builder).forEach(itemStack1 -> method_24023(objectArrayList, itemStack1, finalBlockPos.toImmutable()));
                        }

                        if (!this.world.isClient) {
                            this.world.setBlockState(blockPos, blockStateToPlace, 3);
                        }
                        block_1.onDestroyedByExplosion(this.world, blockPos, this);
                        this.world.getProfiler().pop();
                    } else if (!blockState.getFluidState().isEmpty()) {
                        BlockState blockToPlace;
                        if (blockState.getFluidState().isStill()) {
                            blockToPlace = blockState;
                        } else {
                            blockToPlace = blockStateToPlace;
                        }
                        this.world.setBlockState(blockPos, blockToPlace, 3);
                    }
                }
            }

            for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : objectArrayList) {
                Pair<ItemStack, BlockPos> pair = itemStackBlockPosPair;
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
        }
    }
}

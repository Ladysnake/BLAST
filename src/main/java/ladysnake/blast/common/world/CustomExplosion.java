package ladysnake.blast.common.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.*;

public class CustomExplosion extends Explosion {
    private final Explosion.DestructionType blockDestructionType;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity entity;
    private final float power;
    private final BlockBreakEffect effect;
    private DamageSource damageSource;
    private final List<BlockPos> affectedBlocks;
    private final Map<PlayerEntity, Vec3d> affectedPlayers;

    public CustomExplosion(World world, Entity entity, double x, double y, double z, float power, BlockBreakEffect effect, Explosion.DestructionType destructionType) {
        super(world, entity, null, null, x, y, z, power, false, destructionType);
        this.random = new Random();
        this.affectedBlocks = Lists.newArrayList();
        this.affectedPlayers = Maps.newHashMap();
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.effect = effect;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockDestructionType = destructionType;
        this.damageSource = DamageSource.explosion(this);
    }

    public float getPower() {
        return power;
    }

    public void collectBlocksAndDamageEntities() {
        Set<BlockPos> set_1 = Sets.newHashSet();

        int int_3;
        int int_4;
        for(int int_2 = 0; int_2 < 16; ++int_2) {
            for(int_3 = 0; int_3 < 16; ++int_3) {
                for(int_4 = 0; int_4 < 16; ++int_4) {
                    if (int_2 == 0 || int_2 == 15 || int_3 == 0 || int_3 == 15 || int_4 == 0 || int_4 == 15) {
                        double double_1 = (double)((float)int_2 / 15.0F * 2.0F - 1.0F);
                        double double_2 = (double)((float)int_3 / 15.0F * 2.0F - 1.0F);
                        double double_3 = (double)((float)int_4 / 15.0F * 2.0F - 1.0F);
                        double double_4 = Math.sqrt(double_1 * double_1 + double_2 * double_2 + double_3 * double_3);
                        double_1 /= double_4;
                        double_2 /= double_4;
                        double_3 /= double_4;
                        float float_1 = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double double_5 = this.x;
                        double double_6 = this.y;
                        double double_7 = this.z;

                        for(float var21 = 0.3F; float_1 > 0.0F; float_1 -= 0.22500001F) {
                            BlockPos blockPos_1 = new BlockPos(double_5, double_6, double_7);
                            BlockState blockState_1 = this.world.getBlockState(blockPos_1);
                            FluidState fluidState_1 = this.world.getFluidState(blockPos_1);
                            if (!blockState_1.isAir() || !fluidState_1.isEmpty()) {
                                float float_3 = Math.max(blockState_1.getBlock().getBlastResistance(), fluidState_1.getBlastResistance());
                                if ((this.effect == BlockBreakEffect.AQUATIC && !fluidState_1.isEmpty()) || (this.effect == BlockBreakEffect.UNSTOPPABLE && fluidState_1.isEmpty())) {
                                    float_3 = 0;
                                }
                                if (this.entity != null) {
                                    float_3 = this.entity.getEffectiveExplosionResistance(this, this.world, blockPos_1, blockState_1, fluidState_1, float_3);
                                }

                                float_1 -= (float_3 + 0.3F) * 0.3F;
                            }

                            if (float_1 > 0.0F && (this.entity == null || this.entity.canExplosionDestroyBlock(this, this.world, blockPos_1, blockState_1, float_1)) && blockState_1.getBlock() != Blocks.BEDROCK) {
                                set_1.add(blockPos_1);
                            }

                            double_5 += double_1 * 0.30000001192092896D;
                            double_6 += double_2 * 0.30000001192092896D;
                            double_7 += double_3 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set_1);
        float float_4 = this.power * 2.0F;
        int_3 = MathHelper.floor(this.x - (double)float_4 - 1.0D);
        int_4 = MathHelper.floor(this.x + (double)float_4 + 1.0D);
        int int_7 = MathHelper.floor(this.y - (double)float_4 - 1.0D);
        int int_8 = MathHelper.floor(this.y + (double)float_4 + 1.0D);
        int int_9 = MathHelper.floor(this.z - (double)float_4 - 1.0D);
        int int_10 = MathHelper.floor(this.z + (double)float_4 + 1.0D);
        List<Entity> list_1 = this.world.getOtherEntities(this.entity, new Box((double)int_3, (double)int_7, (double)int_9, (double)int_4, (double)int_8, (double)int_10));
        Vec3d vec3d_1 = new Vec3d(this.x, this.y, this.z);

        for(int int_11 = 0; int_11 < list_1.size(); ++int_11) {
            Entity entity_1 = (Entity)list_1.get(int_11);
            double double_8 = (double)(MathHelper.sqrt(entity_1.squaredDistanceTo(new Vec3d(this.x, this.y, this.z))) / float_4);
            if (double_8 <= 1.0D) {
                double double_9 = entity_1.getX() - this.x;
                double double_10 = entity_1.getY() + (double)entity_1.getStandingEyeHeight() - this.y;
                double double_11 = entity_1.getZ() - this.z;
                double double_12 = (double)MathHelper.sqrt(double_9 * double_9 + double_10 * double_10 + double_11 * double_11);
                if (double_12 != 0.0D) {
                    double_9 /= double_12;
                    double_10 /= double_12;
                    double_11 /= double_12;
                    double double_13 = (double)getExposure(vec3d_1, entity_1);
                    double double_14 = (1.0D - double_8) * double_13;
                    if (!(entity_1 instanceof ExperienceOrbEntity || entity_1 instanceof ItemEntity)) {
                        entity_1.damage(this.getDamageSource(), (float) ((int) ((double_14 * double_14 + double_14) / 2.0D * 7.0D * (double) float_4 + 1.0D)));
                    }
                    double double_15 = double_14;
                    if (entity_1 instanceof LivingEntity) {
                        double_15 = ProtectionEnchantment.transformExplosionKnockback((LivingEntity)entity_1, double_14);
                    }

                    entity_1.setVelocity(entity_1.getVelocity().add(double_9 * double_15, double_10 * double_15, double_11 * double_15));
                    if (entity_1 instanceof PlayerEntity) {
                        PlayerEntity playerEntity_1 = (PlayerEntity)entity_1;
                        if (!playerEntity_1.isSpectator() && (!playerEntity_1.isCreative() || !playerEntity_1.abilities.flying)) {
                            this.affectedPlayers.put(playerEntity_1, new Vec3d(double_9 * double_14, double_10 * double_14, double_11 * double_14));
                        }
                    }
                }
            }
        }

    }

    public void affectWorld(boolean boolean_1) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
        boolean boolean_2 = this.blockDestructionType != Explosion.DestructionType.NONE;
        if (this.power >= 2.0F && boolean_2) {
            this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        } else {
            this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }

        Iterator var3;
        BlockPos blockPos;
        if (boolean_2) {
            var3 = this.affectedBlocks.iterator();
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList();

            while(var3.hasNext()) {
                blockPos = (BlockPos)var3.next();
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block_1 = blockState.getBlock();
                if (boolean_1) {
                    double double_1 = (double)((float)blockPos.getX() + this.world.random.nextFloat());
                    double double_2 = (double)((float)blockPos.getY() + this.world.random.nextFloat());
                    double double_3 = (double)((float)blockPos.getZ() + this.world.random.nextFloat());
                    double double_4 = double_1 - this.x;
                    double double_5 = double_2 - this.y;
                    double double_6 = double_3 - this.z;
                    double double_7 = (double)MathHelper.sqrt(double_4 * double_4 + double_5 * double_5 + double_6 * double_6);
                    double_4 /= double_7;
                    double_5 /= double_7;
                    double_6 /= double_7;
                    double double_8 = 0.5D / (double_7 / (double)this.power + 0.1D);
                    double_8 *= (double)(this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3F);
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

                if (!blockState.isAir() && blockState.getFluidState().isEmpty() || blockState.getBlock() instanceof FluidFillable) {
                    if (block_1.shouldDropItemsOnExplosion(this) && this.world instanceof ServerWorld) {
                        BlockEntity blockEntity = block_1.hasBlockEntity() ? this.world.getBlockEntity(blockPos) : null;

                        ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                        if (this.effect == BlockBreakEffect.FORTUNE) {
                            itemStack.addEnchantment(Enchantments.FORTUNE, 3);
                        }

                        LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world)).random(this.world.random).parameter(LootContextParameters.TOOL, itemStack).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, this.entity);
                        if (this.blockDestructionType == Explosion.DestructionType.DESTROY) {
                            builder.parameter(LootContextParameters.EXPLOSION_RADIUS, this.power);
                        }

                        BlockPos finalBlockPos = blockPos;
                        blockState.getDroppedStacks(builder).forEach(itemStack1 -> method_24023(objectArrayList, itemStack1, finalBlockPos.toImmutable()));
                    }

                    if (!this.world.isClient) {
                        this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                    }
                    block_1.onDestroyedByExplosion(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                }
            }

            for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : objectArrayList) {
                Pair<ItemStack, BlockPos> pair = (Pair) itemStackBlockPosPair;
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
        }

    }

    private static void method_24023(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, ItemStack itemStack, BlockPos blockPos) {
        int i = objectArrayList.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = (Pair)objectArrayList.get(j);
            ItemStack itemStack2 = (ItemStack)pair.getFirst();
            if (ItemEntity.canMerge(itemStack2, itemStack)) {
                    ItemStack itemStack3 = ItemEntity.merge(itemStack2, itemStack, 16);
                objectArrayList.set(j, Pair.of(itemStack3, pair.getSecond()));
                if (itemStack.isEmpty()) {
                    return;
                }
            }
        }

        objectArrayList.add(Pair.of(itemStack, blockPos));
    }


    public enum BlockBreakEffect {
        FORTUNE,
        UNSTOPPABLE,
        AQUATIC
    }

}

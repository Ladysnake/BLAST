package ladysnake.blast.common.world;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.util.ProtectionsProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EnderExplosion extends CustomExplosion {
    public static final float PARTICLE_DISTANCE = 1f;

    public EnderExplosion(World world, Entity entity, double x, double y, double z, float power, DestructionType destructionType) {
        super(world, entity, x, y, z, power, null, destructionType);
    }

    @Override
    public void collectBlocksAndDamageEntities() {
        Set<BlockPos> set = Sets.newHashSet();

        int k;
        int l;
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                for (l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (float) j / 15.0F * 2.0F - 1.0F;
                        double e = (float) k / 15.0F * 2.0F - 1.0F;
                        double f = (float) l / 15.0F * 2.0F - 1.0F;
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double m = this.x;
                        double n = this.y;
                        double o = this.z;

                        for (float var21 = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                            BlockState blockState = this.world.getBlockState(blockPos);
                            FluidState fluidState = this.world.getFluidState(blockPos);
                            if (!blockState.isAir() || !fluidState.isEmpty()) {
                                float br = Math.max(blockState.getBlock().getBlastResistance(), fluidState.getBlastResistance());
                                if (((this.effect == BlockBreakEffect.AQUATIC || this.effect == BlockBreakEffect.FROSTY) && !fluidState.isEmpty()) || (this.effect == BlockBreakEffect.UNSTOPPABLE && fluidState.isEmpty() && blockState.getHardness(this.world, blockPos) >= 0)) {
                                    br = 0;
                                }
                                if (this.entity != null) {
                                    br = this.entity.getEffectiveExplosionResistance(this, this.world, blockPos, blockState, fluidState, br);
                                }

                                h -= (br + 0.3F) * 0.3F;
                            }


                            if (h > 0.0F && (this.entity == null || this.entity.canExplosionDestroyBlock(this, this.world, blockPos, blockState, h))) {
                                set.add(blockPos);
                            }

                            m += d * 0.30000001192092896D;
                            n += e * 0.30000001192092896D;
                            o += f * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0F;
        k = MathHelper.floor(this.x - (double) q - 1.0D);
        l = MathHelper.floor(this.x + (double) q + 1.0D);
        int t = MathHelper.floor(this.y - (double) q - 1.0D);
        int u = MathHelper.floor(this.y + (double) q + 1.0D);
        int v = MathHelper.floor(this.z - (double) q - 1.0D);
        int w = MathHelper.floor(this.z + (double) q + 1.0D);
        List<Entity> list = this.world.getOtherEntities(null, new Box(k, t, v, l, u, w));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (Entity entity : list) {
            if (!entity.isImmuneToExplosion(this) && ProtectionsProvider.canInteractEntity(entity, damageSource)) {
                double y = Math.sqrt(entity.squaredDistanceTo(vec3d)) / q;
                if (y <= 1.0D) {
                    double z = entity.getX() - this.x;
                    double aa = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double ab = entity.getZ() - this.z;
                    double ac = Math.sqrt(z * z + aa * aa + ab * ab);
                    if (ac != 0.0D) {
                        if (entity instanceof LivingEntity livingEntity) {
                            double d = livingEntity.getX();
                            double e = livingEntity.getY();
                            double f = livingEntity.getZ();

                            for (int i = 0; i < 16; ++i) {
                                double g = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
                                double h = MathHelper.clamp(livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(16) - 8), world.getBottomY(), (world.getBottomY() + world.getHeight() - 1));
                                double j = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
                                if (livingEntity.hasVehicle()) {
                                    livingEntity.stopRiding();
                                }

                                if (livingEntity.teleport(g, h, j, true)) {
                                    SoundEvent soundEvent = livingEntity instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                                    world.playSound(null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                                    livingEntity.playSound(soundEvent, 1.0F, 1.0F);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public void affectWorld(boolean boolean_1) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.9F);
        boolean boolean_2 = this.destructionType != DestructionType.KEEP;
        this.world.addParticle(ParticleTypes.REVERSE_PORTAL, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);

        Iterator var3;
        BlockPos blockPos;
        if (boolean_2) {
            var3 = this.affectedBlocks.iterator();
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();

            while (var3.hasNext()) {
                blockPos = (BlockPos) var3.next();
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block_1 = blockState.getBlock();

                if (canExplode(blockPos) && !blockState.isAir() && blockState.getFluidState().isEmpty() || blockState.getBlock() instanceof FluidFillable) {
                    if (block_1.shouldDropItemsOnExplosion(this) && this.world instanceof ServerWorld) {
                        BlockEntity blockEntity = this.world.getBlockEntity(blockPos) != null ? this.world.getBlockEntity(blockPos) : null;

                        ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                        itemStack.addEnchantment(Enchantments.SILK_TOUCH, 1);

                        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld) world)
                                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos))
                                .add(LootContextParameters.TOOL, ItemStack.EMPTY)
                                .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity)
                                .addOptional(LootContextParameters.THIS_ENTITY, this.entity);

                        if (this.destructionType == DestructionType.DESTROY)
                            builder.add(LootContextParameters.EXPLOSION_RADIUS, this.power);

                        BlockPos finalBlockPos = blockPos;
                        blockState.getDroppedStacks(builder).forEach(itemStack1 -> method_24023(objectArrayList, itemStack1, finalBlockPos.toImmutable()));
                    }

                    if (!this.world.isClient) {
                        this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                    }
                    for (int x = 0; x <= 1; x += PARTICLE_DISTANCE) {
                        for (int y = 0; y <= 1; y += PARTICLE_DISTANCE) {
                            for (int z = 0; z <= 1; z += PARTICLE_DISTANCE) {
                                this.world.addParticle(ParticleTypes.REVERSE_PORTAL, blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z, 0, 0, 0);
                            }
                        }
                    }

                    block_1.onDestroyedByExplosion(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                } else if (!blockState.getFluidState().isEmpty()) {
                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                    for (int x = 0; x <= 1; x += PARTICLE_DISTANCE) {
                        for (int y = 0; y <= 1; y += PARTICLE_DISTANCE) {
                            for (int z = 0; z <= 1; z += PARTICLE_DISTANCE) {
                                this.world.addParticle(ParticleTypes.REVERSE_PORTAL, blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z, 0, 0, 0);
                            }
                        }
                    }
                }
            }

            for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : objectArrayList) {
                Block.dropStack(this.world, itemStackBlockPosPair.getSecond(), itemStackBlockPosPair.getFirst());
            }
        }
    }

}

package ladysnake.blast.common.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.entity.StripminerEntity;
import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CustomExplosion extends Explosion {
    public final Explosion.DestructionType blockDestructionType;
    public final Random random;
    public final World world;
    public final double x;
    public final double y;
    public final double z;
    public final Entity entity;
    public final float power;
    public final BlockBreakEffect effect;
    public final DamageSource damageSource;
    public final List<BlockPos> affectedBlocks;
    public final Map<PlayerEntity, Vec3d> affectedPlayers;

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
                            BlockPos blockPos = new BlockPos(m, n, o);
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
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, t, v, l, u, w));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int x = 0; x < list.size(); ++x) {
            Entity entity = list.get(x);
            if (!entity.isImmuneToExplosion()) {
                double y = Math.sqrt(entity.squaredDistanceTo(vec3d)) / q;
                if (y <= 1.0D) {
                    double z = entity.getX() - this.x;
                    double aa = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double ab = entity.getZ() - this.z;
                    double ac = Math.sqrt(z * z + aa * aa + ab * ab);
                    if (ac != 0.0D) {
                        z /= ac;
                        aa /= ac;
                        ab /= ac;
                        double ad = getExposure(vec3d, entity);
                        double ae = (1.0D - y) * ad;
                        if (!(entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity)) {
                            entity.damage(this.getDamageSource(), (float) ((int) ((ae * ae + ae) / 2.0D * 7.0D * (double) q + 1.0D)));
                        }
                        double af = ae;
                        if (entity instanceof LivingEntity) {
                            af = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ae);
                        }

                        entity.setVelocity(entity.getVelocity().add(z * af, aa * af, ab * af));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerEntity = (PlayerEntity) entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, new Vec3d(z * ae, aa * ae, ab * ae));
                            }
                        }
                    }
                }
            }
        }

    }

    public void affectWorld(boolean boolean_1) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
        boolean boolean_2 = this.blockDestructionType != DestructionType.NONE;
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

            while (var3.hasNext()) {
                blockPos = (BlockPos) var3.next();
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block_1 = blockState.getBlock();
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

                if (!blockState.isAir() && blockState.getFluidState().isEmpty() || blockState.getBlock() instanceof FluidFillable) {
                    if (block_1.shouldDropItemsOnExplosion(this) && this.world instanceof ServerWorld) {
                        BlockEntity blockEntity = this.world.getBlockEntity(blockPos) != null ? this.world.getBlockEntity(blockPos) : null;

                        ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                        if (this.effect == BlockBreakEffect.FORTUNE) {
                            itemStack.addEnchantment(Enchantments.FORTUNE, 3);
                        }

                        LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world)).random(this.world.random).parameter(LootContextParameters.ORIGIN, Vec3d.of(blockPos)).parameter(LootContextParameters.TOOL, itemStack).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, this.entity);
                        if (this.blockDestructionType == DestructionType.DESTROY) {
                            builder.parameter(LootContextParameters.EXPLOSION_RADIUS, this.power);
                        }

                        BlockPos finalBlockPos = blockPos;
                        blockState.getDroppedStacks(builder).forEach(itemStack1 -> method_24023(objectArrayList, itemStack1, finalBlockPos.toImmutable()));
                    }

                    if (!this.world.isClient) {
                        BlockState blockToPlace = Blocks.AIR.getDefaultState();
                        if (this.effect == BlockBreakEffect.FROSTY) {
                            blockToPlace = BlastBlocks.DRY_ICE.getDefaultState();
                            if (this.entity instanceof StripminerEntity) {
                                blockToPlace = blockToPlace.with(PillarBlock.AXIS, ((StripminerEntity) this.entity).getFacing().getAxis());
                            }
                        }

                        this.world.setBlockState(blockPos, blockToPlace, 3);
                    }
                    block_1.onDestroyedByExplosion(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                } else if (!blockState.getFluidState().isEmpty()) {
                    BlockState blockToPlace = Blocks.AIR.getDefaultState();
                    if (blockState.getFluidState().isStill()) {
                        if (blockState.getFluidState().getFluid() == Fluids.WATER) {
                            if (this.effect == BlockBreakEffect.FROSTY) {
                                blockToPlace = Blocks.ICE.getDefaultState();
                            } else {
                                blockToPlace = blockState;
                            }
                        } else if (blockState.getFluidState().getFluid() == Fluids.LAVA) {
                            if (this.effect == BlockBreakEffect.FROSTY) {
                                blockToPlace = Blocks.BASALT.getDefaultState();
                                if (this.entity instanceof StripminerEntity) {
                                    blockToPlace = blockToPlace.with(PillarBlock.AXIS, ((StripminerEntity) this.entity).getFacing().getAxis());
                                }
                            } else {
                                blockToPlace = blockState;
                            }
                        }
                    } else {
                        blockToPlace = Blocks.AIR.getDefaultState();
                    }
                    this.world.setBlockState(blockPos, blockToPlace, 3);
                }
            }

            for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : objectArrayList) {
                Pair<ItemStack, BlockPos> pair = itemStackBlockPosPair;
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
        }

        if (effect == BlockBreakEffect.FIERY) {
            for (BlockPos blockPos3 : this.affectedBlocks) {
                if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockPos3).isAir() && this.world.getBlockState(blockPos3.down()).isOpaqueFullCube(this.world, blockPos3.down())) {
                    this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
                }
            }
        }
    }

    private static void method_24023(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, ItemStack itemStack, BlockPos blockPos) {
        int i = objectArrayList.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = objectArrayList.get(j);
            ItemStack itemStack2 = pair.getFirst();
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
        AQUATIC,
        FIERY,
        FROSTY
    }

}

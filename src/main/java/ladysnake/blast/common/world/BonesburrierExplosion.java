package ladysnake.blast.common.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.entity.ShrapnelBlockEntity;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.util.ProtectionsProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BonesburrierExplosion extends CustomExplosion {
    public BonesburrierExplosion(World world, Entity entity, double x, double y, double z, float power, DestructionType destructionType) {
        super(world, entity, x, y, z, power, null, destructionType);
    }

    @Override
    public void collectBlocksAndDamageEntities() {
        world.emitGameEvent(entity, GameEvent.EXPLODE, BlockPos.ofFloored(x, y, z));
        Set<BlockPos> set = new HashSet<>();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
                        double iX = (float) i / 15 * 2 - 1;
                        double jX = (float) j / 15 * 2 - 1;
                        double kX = (float) k / 15 * 2 - 1;
                        double product = Math.sqrt(iX * iX + jX * jX + kX * kX);
                        iX /= product;
                        jX /= product;
                        kX /= product;
                        double dX = x;
                        double dY = y;
                        double dZ = z;
                        for (float currentPower = getPower() * (0.7f + world.random.nextFloat() * 0.6f); currentPower > 0.0f; currentPower -= 0.22500001f) {
                            mutable.set(dX, dY, dZ);
                            if (!world.isInBuildLimit(mutable)) {
                                break;
                            }
                            BlockState state = world.getBlockState(mutable);
                            Optional<Float> blastResistance = behavior.getBlastResistance(this, world, mutable, state, world.getFluidState(mutable));
                            if (blastResistance.isPresent()) {
                                currentPower -= ((blastResistance.get() >= 1200 ? blastResistance.get() : 0) + 0.3f) * 0.3f;
                            }
                            if (currentPower > 0.0f && behavior.canDestroyBlock(this, world, mutable, state, currentPower)) {
                                set.add(mutable.toImmutable());
                            }
                            dX += iX * 0.3f;
                            dY += jX * 0.3f;
                            dZ += kX * 0.3f;
                        }
                    }
                }
            }
        }
        affectedBlocks.addAll(set);
        float power = getPower() * 2;
        int minX = MathHelper.floor(x - power - 1);
        int maxX = MathHelper.floor(x + power + 1);
        int minY = MathHelper.floor(y - power - 1);
        int maxY = MathHelper.floor(y + power + 1);
        int minZ = MathHelper.floor(z - power - 1);
        int maxZ = MathHelper.floor(z + power + 1);
        Vec3d source = new Vec3d(x, y, z);
        for (Entity entity : world.getOtherEntities(entity, new Box(minX, minY, minZ, maxX, maxY, maxZ))) {
            if (!entity.isImmuneToExplosion(this)) {
                double distance = Math.sqrt(entity.squaredDistanceTo(source)) / power;
                if (distance <= 1) {
                    double dX = entity.getX() - x;
                    double dY = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - y;
                    double dZ = entity.getZ() - z;
                    double product = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                    if (product != 0) {
                        dX /= product;
                        dY /= product;
                        dZ /= product;
                        if (behavior.shouldDamage(this, entity)) {
                            entity.damage(damageSource, behavior.calculateDamage(this, entity));
                        }
                        double knockback = (1 - distance) * getExposure(source, entity) * behavior.getKnockbackModifier(entity);
                        if (entity instanceof LivingEntity living) {
                            knockback *= (1 - living.getAttributeValue(EntityAttributes.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE));
                        }
                        dX *= knockback;
                        dY *= knockback;
                        dZ *= knockback;
                        Vec3d velocity = new Vec3d(dX, dY, dZ);
                        entity.setVelocity(entity.getVelocity().add(velocity));
                        affectedEntities.add(entity);
                        if (entity instanceof PlayerEntity player) {
                            if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                                getAffectedPlayers().put(player, velocity);
                            }
                        }
                        entity.onExplodedBy(entity);
                    }
                }
            }
        }
    }

    @Override
    public void affectWorld(boolean particles) {
        world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4, (1 + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
        boolean destroy = destructionType != DestructionType.KEEP;
        if (particles) {
            if (getPower() >= 2 && destroy) {
                world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0, 0);
            } else {
                world.addParticle(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0);
            }
        }
        if (destroy) {
            Util.shuffle(affectedBlocks, world.random);
            Vec3d source = new Vec3d(x, y, z);
            ObjectArrayList<Pair<ItemStack, BlockPos>> destroyedBlocks = new ObjectArrayList<>();
            for (BlockPos pos : affectedBlocks) {
                if (canPlace(pos) && canExplode(pos)) {
                    BlockState state = world.getBlockState(pos);
                    if (!state.isAir()) {
                        world.getProfiler().push("explosion_blocks");
                        if (world instanceof ServerWorld serverWorld) {
                            if (state.getBlock().shouldDropItemsOnExplosion(this)) {
                                state.getDroppedStacks(getBuilder(serverWorld, pos, ItemStack.EMPTY, state.hasBlockEntity() ? world.getBlockEntity(pos) : null)).forEach(stack -> tryMergeStack(destroyedBlocks, stack, pos.toImmutable()));
                            }
                            // shrapnel
                            PlayerEntity owner = null;
                            if (damageSource.getSource() instanceof BombEntity bomb && bomb.getOwner() instanceof PlayerEntity player) {
                                owner = player;
                            } else if (damageSource.getAttacker() != null && damageSource.getAttacker() instanceof PlayerEntity player) {
                                owner = player;
                            }
                            ShrapnelBlockEntity shrapnelBlockEntity = ShrapnelBlockEntity.spawnFromBlock(world, pos, state, owner);
                            shrapnelBlockEntity.setVelocity(new Vec3d(pos.getX(), pos.getY(), pos.getZ()).subtract(source).normalize());
                            shrapnelBlockEntity.dropItem = false;
                            shrapnelBlockEntity.velocityModified = true;
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                            // paint
                            BlockPos.Mutable mutable = new BlockPos.Mutable();
                            for (Direction direction : Direction.values()) {
                                if (ProtectionsProvider.canPlaceBlock(mutable.set(pos, direction), world, damageSource)) {
                                    BlockState adjacentBlockState = world.getBlockState(mutable.set(pos, direction));
                                    FluidState fluidState = world.getFluidState(mutable.set(pos, direction));
                                    Optional<Float> optional = behavior.getBlastResistance(this, world, mutable.set(pos, direction), adjacentBlockState, fluidState);
                                    if (optional.isPresent() && optional.get() < 1200 && !affectedBlocks.contains(mutable.set(pos, direction))) {
                                        world.setBlockState(mutable.set(pos, direction), BlastBlocks.FOLLY_RED_PAINT.getDefaultState());
                                    }
                                }
                            }
                        }
                        state.getBlock().onDestroyedByExplosion(world, pos, this);
                        world.getProfiler().pop();
                    }
                }
            }
            destroyedBlocks.forEach(pair -> Block.dropStack(world, pair.getSecond(), pair.getFirst()));
        }
    }
}

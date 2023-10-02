package ladysnake.blast.common.world;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.block.DetonatableBlock;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class BonesburrierExplosion extends CustomExplosion {
    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();

    public BonesburrierExplosion(World world, Entity entity, double x, double y, double z, float power, DestructionType destructionType) {
        super(world, entity, x, y, z, power, null, destructionType);
    }

    public static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double p;
                    double o;
                    double n = MathHelper.lerp(k, box.minX, box.maxX);
                    Vec3d vec3d = new Vec3d(n + g, o = MathHelper.lerp(l, box.minY, box.maxY), (p = MathHelper.lerp(m, box.minZ, box.maxZ)) + h);
                    if (entity.getWorld().raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float) i / (float) j;
    }

    public void collectBlocksAndDamageEntities() {
        int l;
        int k;
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new BlockPos((int) this.x, (int) this.y, (int) this.z));
        HashSet<BlockPos> set = Sets.newHashSet();
        int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                block2:
                for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float) j / 15.0f * 2.0f - 1.0f;
                    double e = (float) k / 15.0f * 2.0f - 1.0f;
                    double f = (float) l / 15.0f * 2.0f - 1.0f;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = this.x;
                    double n = this.y;
                    double o = this.z;
                    float p = 0.3f;
                    for (float h = this.power * (0.7f + this.world.random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos blockPos = new BlockPos((int) m, (int) n, (int) o);
                        BlockState blockState = this.world.getBlockState(blockPos);
                        FluidState fluidState = this.world.getFluidState(blockPos);
                        if (!this.world.isInBuildLimit(blockPos)) continue block2;
                        Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(this, this.world, blockPos, blockState, fluidState);
                        if (blockState.getBlock()==Blocks.NETHERITE_BLOCK) {
                            System.out.println(optional.get().floatValue());
                        }
                        if (optional.isPresent()) {
                            h -= ((optional.get().floatValue() >= 1200 ? optional.get().floatValue() : 0) + 0.3f) * 0.3f;
                        }
                        if (h > 0.0f && DEFAULT_BEHAVIOR.canDestroyBlock(this, this.world, blockPos, blockState, h)) {
                            set.add(blockPos);
                        }
                        m += d * (double) 0.3f;
                        n += e * (double) 0.3f;
                        o += f * (double) 0.3f;
                    }
                }
            }
        }
        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0f;
        k = MathHelper.floor(this.x - (double) q - 1.0);
        l = MathHelper.floor(this.x + (double) q + 1.0);
        int r = MathHelper.floor(this.y - (double) q - 1.0);
        int s = MathHelper.floor(this.y + (double) q + 1.0);
        int t = MathHelper.floor(this.z - (double) q - 1.0);
        int u = MathHelper.floor(this.z + (double) q + 1.0);
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, r, t, l, s, u));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
        for (int v = 0; v < list.size(); ++v) {
            PlayerEntity playerEntity;
            double z;
            double y;
            double x;
            double aa;
            double w;
            Entity entity = list.get(v);
            if (entity.isImmuneToExplosion() || !((w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double) q) <= 1.0) || (aa = Math.sqrt((x = entity.getX() - this.x) * x + (y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y) * y + (z = entity.getZ() - this.z) * z)) == 0.0)
                continue;
            x /= aa;
            y /= aa;
            z /= aa;
            double ab = Explosion.getExposure(vec3d, entity);
            double ac = (1.0 - w) * ab;
            entity.damage(this.getDamageSource(), (int) ((ac * ac + ac) / 2.0 * 7.0 * (double) q + 1.0));
            double ad = ac;
            if (entity instanceof LivingEntity) {
                ad = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ac);
            }
            entity.setVelocity(entity.getVelocity().add(x * ad, y * ad, z * ad));
            if (!(entity instanceof PlayerEntity) || (playerEntity = (PlayerEntity) entity).isSpectator() || playerEntity.isCreative() && playerEntity.getAbilities().flying)
                continue;
            this.affectedPlayers.put(playerEntity, new Vec3d(x * ac, y * ac, z * ac));
        }
    }

    /**
     * @param particles whether this explosion should emit explosion or explosion emitter particles around the source of the explosion
     */
    public void affectWorld(boolean particles) {
        boolean bl;
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
        }
        boolean bl2 = bl = this.destructionType != DestructionType.KEEP;
        if (particles) {
            if (this.power < 2.0f || !bl) {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        if (bl) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();
            Util.shuffle(this.affectedBlocks, this.world.random);
            for (BlockPos blockPos : this.affectedBlocks) {
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (blockState.isAir()) continue;
                BlockPos blockPos2 = blockPos.toImmutable();
                this.world.getProfiler().push("explosion_blocks");
                if (block.shouldDropItemsOnExplosion(this) && this.world instanceof ServerWorld) {
                    BlockEntity blockEntity = blockState.hasBlockEntity() ? this.world.getBlockEntity(blockPos) : null;
                    LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld) world)
                            .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos))
                            .add(LootContextParameters.TOOL, ItemStack.EMPTY)
                            .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity)
                            .addOptional(LootContextParameters.THIS_ENTITY, this.entity);

                    if (this.destructionType == DestructionType.DESTROY)
                        builder.add(LootContextParameters.EXPLOSION_RADIUS, this.power);

                    blockState.getDroppedStacks(builder).forEach(stack -> tryMergeStack(objectArrayList, stack, blockPos2));
                }

                if (!world.isClient()) {
                    FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, blockPos, blockState);

                    Vec3d vel = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).subtract(new Vec3d(this.x, this.y, this.z)).normalize().multiply(1);

                    fallingBlockEntity.dropItem = false;
                    fallingBlockEntity.setVelocity(vel);
                    fallingBlockEntity.velocityModified = true;

                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);

                    // paint
                    BlockPos.Mutable mutable = new BlockPos.Mutable();

                    for (Direction direction : Direction.values()) {
                        BlockState adjacentBlockState = this.world.getBlockState(mutable.set(blockPos, direction));
                        FluidState fluidState = this.world.getFluidState(mutable.set(blockPos, direction));
                        Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(this, this.world, mutable.set(blockPos, direction), adjacentBlockState, fluidState);

                        if (optional.isPresent() && optional.get().floatValue() < 1200 && !this.affectedBlocks.contains(mutable.set(blockPos, direction))) {
                            world.setBlockState(mutable.set(blockPos, direction), BlastBlocks.FOLLY_RED_PAINT.getDefaultState());
                        }
                    }

                }

                block.onDestroyedByExplosion(this.world, blockPos, this);
                this.world.getProfiler().pop();
            }
            for (Pair pair : objectArrayList) {
                Block.dropStack(this.world, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
            }
        }
    }

    private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        int i = stacks.size();
        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = stacks.get(j);
            ItemStack itemStack = pair.getFirst();
            if (!ItemEntity.canMerge(itemStack, stack)) continue;
            ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
            stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
            if (!stack.isEmpty()) continue;
            return;
        }
        stacks.add(Pair.of(stack, pos));
    }

}

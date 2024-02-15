package ladysnake.blast.common.world;

import com.luxintrus.befoul.common.block.InkBlock;
import com.luxintrus.befoul.core.BefoulParticles;
import com.luxintrus.befoul.core.BefoulSounds;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.init.BlastParticles;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
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
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SmilesweeperExplosion extends CustomExplosion {
    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();

    private final Block inkBlock;

    public SmilesweeperExplosion(World world, Entity entity, double x, double y, double z, float power, DestructionType destructionType, Block inkBlock) {
        super(world, entity, x, y, z, power, null, destructionType);
        this.inkBlock = inkBlock;
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
                    if (entity.world.raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float) i / (float) j;
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

    public void collectBlocksAndDamageEntities() {
//        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new BlockPos(this.x, this.y, this.z));

        BlockPos expPos = new BlockPos(this.x, this.y, this.z);
        if (!world.isClient()) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            int inkSize = 4;
            for (float x = -inkSize; x <= inkSize; x++) {
                for (float y = -inkSize; y <= inkSize; y++) {
                    for (float z = -inkSize; z <= inkSize; z++) {
                        if (mutable.set(this.x + x, this.y + y, this.z + z).isWithinDistance(expPos, inkSize)) {
                            // falling blocks
                            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(world, this.entity.getBlockPos(), inkBlock.getDefaultState().with(InkBlock.LAYERS, InkBlock.MAX_LAYERS));

                            Vec3d vel = new Vec3d(mutable.getX(), mutable.getY(), mutable.getZ()).subtract(new Vec3d(this.x, this.y, this.z)).normalize().multiply(2f);

                            fallingBlockEntity.dropItem = false;
                            fallingBlockEntity.setVelocity(new Vec3d(vel.x * random.nextFloat(), vel.y * random.nextFloat(), vel.z * random.nextFloat()));
                            fallingBlockEntity.velocityModified = true;
                            fallingBlockEntity.velocityDirty = true;

                            BlockState blockState = world.getBlockState(mutable);
                            if (blockState.getBlock().getBlastResistance() < 1200) {
                                blockState.getBlock().onDestroyedByExplosion(this.world, mutable, this);
                                this.world.setBlockState(mutable, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param particles whether this explosion should emit explosion or explosion emitter particles around the source of the explosion
     */
    public void affectWorld(boolean particles) {
        boolean bl = this.destructionType != DestructionType.NONE;
        if (particles) {
            if (this.power < 2.0f || !bl) {
                this.world.addParticle(BlastParticles.INK_EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(BlastParticles.INK_EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
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
                    LootContext.Builder builder = new LootContext.Builder((ServerWorld) this.world).random(this.world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, this.entity);
                    if (this.destructionType == DestructionType.DESTROY) {
                        builder.parameter(LootContextParameters.EXPLOSION_RADIUS, Float.valueOf(this.power));
                    }
                    blockState.getDroppedStacks(builder).forEach(stack -> tryMergeStack(objectArrayList, stack, blockPos2));
                }

                if (!world.isClient()) {
                    FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(world, blockPos, blockState);

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
                            world.setBlockState(mutable.set(blockPos, direction), inkBlock.getDefaultState().with(InkBlock.LAYERS, InkBlock.MAX_LAYERS));
                        }
                    }
                }

                block.onDestroyedByExplosion(this.world, blockPos, this);
                this.world.getProfiler().pop();
            }
            for (Pair pair : objectArrayList) {
                Block.dropStack(this.world, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
            }

            if (world instanceof ServerWorld serverWorld) {

                for (int i = 0; i < 50; i++) {
                    this.spawnEntityForExplosion(FallingBlockEntity.fall(serverWorld, this.entity.getBlockPos(), inkBlock.getDefaultState().with(InkBlock.LAYERS, InkBlock.MAX_LAYERS)));
                }
            }
        }
    }

    protected void spawnEntityForExplosion(@Nullable Entity entity) {
        if (entity == null) return;
        float speedmul = .5f;
        float dY = this.world.getRandom().nextFloat() * speedmul + 0.1f;
        float dXZ = this.world.getRandom().nextFloat() * speedmul + 0.1f;
        float yaw = this.world.getRandom().nextFloat() * 2f * MathHelper.PI;
        entity.setVelocity(new Vec3d(random.nextGaussian() * speedmul, dY, MathHelper.cos(yaw) * dXZ));
        entity.refreshPositionAndAngles(this.x, this.y + .5f, this.z, this.world.getRandom().range(-180, 180), 0f);
        this.world.spawnEntity(entity);
    }

}

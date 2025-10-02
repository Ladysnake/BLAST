package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Optional;

public class BonesburrierEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new CustomExplosionBehavior() {
        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
            return super.getBlastResistance(explosion, world, pos, blockState, fluidState).map(resistance -> resistance > 100 ? resistance / 2 : 0);
        }

        @Override
        public boolean customBlockDestruction(Explosion explosion, ServerWorld world, Vec3d sourcePos, List<BlockPos> positions) {
            Util.shuffle(positions, world.getRandom());
            for (BlockPos pos : positions) {
                BlockState state = world.getBlockState(pos);
                if (!state.isAir()) {
                    FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, pos, state);
                    fallingBlock.setVelocity(new Vec3d(pos.getX(), pos.getY(), pos.getZ()).subtract(sourcePos).normalize());
                    fallingBlock.dropItem = false;
                    fallingBlock.velocityModified = true;
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    // paint
                    BlockPos.Mutable mutable = new BlockPos.Mutable();
                    for (Direction direction : Direction.values()) {
                        BlockState adjacentBlockState = world.getBlockState(mutable.set(pos, direction));
                        FluidState fluidState = world.getFluidState(mutable);
                        Optional<Float> resistance = getBlastResistance(explosion, world, mutable, adjacentBlockState, fluidState);
                        if (resistance.isPresent() && resistance.get() < 1200 && !positions.contains(mutable)) {
                            world.setBlockState(mutable, BlastBlocks.FOLLY_RED_PAINT.getDefaultState());
                        }
                    }
                }
            }
            return true;
        }
    };

    public BonesburrierEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setFuse(80);
        setExplosionPower(8);
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }

    @Override
    public void explode() {
        super.explode();
        getEntityWorld().playSound(null, getX(), getBodyY(0.0625), getZ(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.BLOCKS, 5, 1);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.BONESBURRIER.asItem();
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }
}

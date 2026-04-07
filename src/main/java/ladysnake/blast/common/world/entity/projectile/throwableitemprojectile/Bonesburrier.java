package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class Bonesburrier extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new CustomExplosionDamageCalculator() {
        @Override
        public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState) {
            return super.getBlockExplosionResistance(explosion, level, pos, blockState, fluidState).map(resistance -> resistance > 100 ? resistance / 2 : 0);
        }

        @Override
        public boolean customBlockDestruction(Explosion explosion, ServerLevel level, Vec3 sourcePos, List<BlockPos> positions) {
            Util.shuffle(positions, level.getRandom());
            for (BlockPos pos : positions) {
                BlockState state = level.getBlockState(pos);
                if (!state.isAir()) {
                    FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, state);
                    fallingBlock.setDeltaMovement(new Vec3(pos.getX(), pos.getY(), pos.getZ()).subtract(sourcePos).normalize());
                    fallingBlock.dropItem = false;
                    fallingBlock.hurtMarked = true;
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    // paint
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (Direction direction : Direction.values()) {
                        BlockState adjacentBlockState = level.getBlockState(mutable.setWithOffset(pos, direction));
                        FluidState fluidState = level.getFluidState(mutable);
                        Optional<Float> resistance = getBlockExplosionResistance(explosion, level, mutable, adjacentBlockState, fluidState);
                        if (resistance.isPresent() && resistance.get() < 1200 && !positions.contains(mutable)) {
                            level.setBlockAndUpdate(mutable, BlastBlocks.FOLLY_RED_PAINT.defaultBlockState());
                        }
                    }
                }
            }
            return true;
        }
    };

    public Bonesburrier(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setFuse(80);
        setExplosionPower(8);
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }

    @Override
    public void explode() {
        super.explode();
        level().playSound(null, getX(), getY(0.0625), getZ(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 5, 1);
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

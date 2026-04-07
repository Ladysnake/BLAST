package ladysnake.blast.common.world.entity.item;

import com.mojang.datafixers.util.Pair;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.StripMinerExplosionDamageCalculator;
import ladysnake.blast.common.world.level.block.StripminerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;

public class ColdDigger extends Stripminer {
    public static final CustomExplosionDamageCalculator CALCULATOR = new StripMinerExplosionDamageCalculator() {
        private static final Pair<BlockState, Boolean> FILL_STATE = Pair.of(BlastBlocks.DRY_ICE.defaultBlockState(), false);

        @Override
        public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (!fluidState.isEmpty()) {
                return Optional.of(0F);
            }
            return super.getBlockExplosionResistance(explosion, level, pos, blockState, fluidState);
        }

        @Override
        public Pair<BlockState, Boolean> getFillState() {
            return FILL_STATE;
        }
    };

    public ColdDigger(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setExplosionPower(3.5f);
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }

    @Override
    public void explode() {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(blockPosition());
        for (int i = 0; i <= 24; i++) {
            if (level().getBlockState(mutable).getBlock().getExplosionResistance() < 1200) {
                CustomExplosionDamageCalculator calculator = getExplosionCalculator();
                createExplosion(calculator, mutable.getCenter(), calculator.getPower().orElse(getExplosionPower()), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE.value());
            } else {
                break;
            }
            level().playSound(null, mutable.getX(), mutable.getY(), mutable.getZ(), SoundEvents.SHIELD_BREAK, SoundSource.BLOCKS, 1f, 1.5f);
            mutable.move(getNearestViewDirection());
        }
        mutable.set(blockPosition());
        for (int i = 0; i <= 24; i++) {
            if (level().getBlockState(mutable).getBlock().getExplosionResistance() < 1200) {
                CustomExplosionDamageCalculator calculator = getExplosionCalculator();
                createExplosion(calculator, mutable.getCenter(), 1, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE.value());
            } else {
                break;
            }
            mutable.move(getNearestViewDirection());
        }
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (FACING.equals(accessor)) {
            cachedState = BlastBlocks.COLD_DIGGER.defaultBlockState().setValue(StripminerBlock.FACING, getNearestViewDirection());
        }
    }

    @Override
    public BlockState getState() {
        if (cachedState == null) {
            cachedState = BlastBlocks.COLD_DIGGER.defaultBlockState().setValue(StripminerBlock.FACING, getNearestViewDirection());
        }
        return cachedState;
    }
}

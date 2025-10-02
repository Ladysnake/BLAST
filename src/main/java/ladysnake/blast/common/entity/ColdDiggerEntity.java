package ladysnake.blast.common.entity;

import com.mojang.datafixers.util.Pair;
import ladysnake.blast.common.block.StripminerBlock;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.StripMinerExplosionBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Optional;

public class ColdDiggerEntity extends StripminerEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new StripMinerExplosionBehavior() {
        private static final Pair<BlockState, Boolean> FILL_STATE = Pair.of(BlastBlocks.DRY_ICE.getDefaultState(), false);

        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (!fluidState.isEmpty()) {
                return Optional.of(0F);
            }
            return super.getBlastResistance(explosion, world, pos, blockState, fluidState);
        }

        @Override
        public Pair<BlockState, Boolean> getCustomFillState() {
            return FILL_STATE;
        }
    };

    public ColdDiggerEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionPower(3.5f);
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }

    @Override
    public void explode() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(getBlockPos());
        for (int i = 0; i <= 24; i++) {
            if (getEntityWorld().getBlockState(mutable).getBlock().getBlastResistance() < 1200) {
                CustomExplosionBehavior behavior = getExplosionBehavior();
                createExplosion(behavior, mutable.toCenterPos(), behavior.getPower().orElse(getExplosionPower()), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE.value());
            } else {
                break;
            }
            getEntityWorld().playSound(null, mutable.getX(), mutable.getY(), mutable.getZ(), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1f, 1.5f);
            mutable.move(getFacing());
        }
        mutable.set(getBlockPos());
        for (int i = 0; i <= 24; i++) {
            if (getEntityWorld().getBlockState(mutable).getBlock().getBlastResistance() < 1200) {
                CustomExplosionBehavior behavior = super.getExplosionBehavior();
                createExplosion(behavior, mutable.toCenterPos(), 1, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE.value());
            } else {
                break;
            }
            mutable.move(getFacing());
        }
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        super.onTrackedDataSet(trackedData);
        if (FACING.equals(trackedData)) {
            cachedState = BlastBlocks.COLD_DIGGER.getDefaultState().with(StripminerBlock.FACING, getFacing());
        }
    }

    @Override
    public BlockState getState() {
        if (cachedState == null) {
            cachedState = BlastBlocks.COLD_DIGGER.getDefaultState().with(StripminerBlock.FACING, getFacing());
        }
        return cachedState;
    }
}

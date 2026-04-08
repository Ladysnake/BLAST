/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.item;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.StripMinerExplosionDamageCalculator;
import ladysnake.blast.common.world.level.block.StripminerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Stripminer extends Bomb {
    public static final EntityDataSerializer<Direction> FACING_TYPE = EntityDataSerializer.forValueType(Direction.STREAM_CODEC);
    protected static final EntityDataAccessor<Direction> FACING = SynchedEntityData.defineId(Stripminer.class, FACING_TYPE);

    protected BlockState cachedState;

    public Stripminer(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setFuse(80);
        setExplosionPower(2.5f);
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return StripMinerExplosionDamageCalculator.INSTANCE;
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
            level().playSound(null, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, SoundEvents.SHIELD_BREAK, SoundSource.BLOCKS, 1f, 0.025f);
            mutable.move(getNearestViewDirection());
        }
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (FACING.equals(accessor)) {
            cachedState = BlastBlocks.STRIPMINER.defaultBlockState().setValue(StripminerBlock.FACING, getNearestViewDirection());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FACING, Direction.UP);
    }

    @Override
    protected Item getDefaultItem() {
        return getType() == BlastEntityTypes.COLD_DIGGER ? BlastBlocks.COLD_DIGGER.asItem() : BlastBlocks.STRIPMINER.asItem();
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

    public Direction getNearestViewDirection() {
        return entityData.get(FACING);
    }

    public void setFacing(Direction facing) {
        entityData.set(FACING, facing);
    }

    public BlockState getState() {
        if (cachedState == null) {
            cachedState = BlastBlocks.STRIPMINER.defaultBlockState().setValue(StripminerBlock.FACING, getNearestViewDirection());
        }
        return cachedState;
    }
}

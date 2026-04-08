/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Bomb extends ThrowableItemProjectile {
    private static final WeightedList<ExplosionParticleInfo> EMPTY_PARTICLES = WeightedList.<ExplosionParticleInfo>builder().build();

    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(Bomb.class, EntityDataSerializers.INT);
    private float explosionPower = 3f;
    public int ticksUntilRemoval;
    private int fuseTimer;

    public Bomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setFuse(40);
        setExplosionPower(3);
        ticksUntilRemoval = -1;
    }

    @Override
    public void tick() {
        if (ticksUntilRemoval > 0) {
            ticksUntilRemoval--;
            if (ticksUntilRemoval <= 0) {
                remove(RemovalReason.DISCARDED);
            }
        } else {
            super.tick();
            if (level().getBlockState(blockPosition()).isCollisionShapeFullBlock(level(), blockPosition())) {
                setPos(xo, yo, zo);
            }
            // drop item if in water
            if (isUnderWater() && disableInLiquid()) {
                level().addFreshEntity(new ItemEntity(level(), getX(), getY(), getZ(), new ItemStack(getDefaultItem())));
                remove(RemovalReason.DISCARDED);
            }
            // tick down the fuse, then blow up
            if (getTriggerType() == BombTriggerType.FUSE) {
                // smoke particle for lit fuse
                if (level().isClientSide()) {
                    level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.3, getZ(), 0, 0, 0);
                }
                // shorten the fuse
                setFuse(getFuse() - 1);
                if (getFuse() <= 0) {
                    explode();
                }
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.BOMB;
    }

    @Override
    public void setItem(ItemStack source) {
        super.setItem(source.getItem().getDefaultInstance());
        setFuse(source.getOrDefault(BlastComponentTypes.FUSE, getFuse()));
        setExplosionPower(source.getOrDefault(BlastComponentTypes.EXPLOSION_POWER, getExplosionPower()));
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (tickCount > 1) {
            setDeltaMovement(0, 0, 0);
            if (getTriggerType() == BombTriggerType.IMPACT) {
                explode();
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (FUSE.equals(accessor)) {
            fuseTimer = getFuse();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FUSE, 40);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Fuse", getFuseTimer());
        output.putFloat("ExplosionPower", getExplosionPower());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setFuse(input.getIntOr("Fuse", 0));
        setExplosionPower(input.getFloatOr("ExplosionPower", 0));
    }

    protected void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            CustomExplosionDamageCalculator calculator = getExplosionCalculator();
            createExplosion(calculator, position(), calculator.getPower().orElse(getExplosionPower()), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE.value());
        }
    }

    protected void createExplosion(CustomExplosionDamageCalculator calculator, Vec3 pos, float power, ParticleOptions smallParticle, ParticleOptions largeParticle, SoundEvent sound) {
        level().explode(
            getOwner(),
            null,
            calculator,
            pos.x(), pos.y(), pos.z(),
            power,
            calculator.createsFire(),
            Level.ExplosionInteraction.TNT,
            smallParticle,
            largeParticle,
            calculator.createsPoof() ? Level.DEFAULT_EXPLOSION_BLOCK_PARTICLES : EMPTY_PARTICLES,
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound));
    }

    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return new CustomExplosionDamageCalculator();
    }

    protected BombTriggerType getTriggerType() {
        return BombTriggerType.FUSE;
    }

    public boolean disableInLiquid() {
        return true;
    }

    public int getFuse() {
        return entityData.get(FUSE);
    }

    public void setFuse(int fuse) {
        entityData.set(FUSE, fuse);
        fuseTimer = fuse;
    }

    public int getFuseTimer() {
        return fuseTimer;
    }

    public float getExplosionPower() {
        return explosionPower;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public enum BombTriggerType {
        FUSE,
        IMPACT
    }
}

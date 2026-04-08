/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;

public class PipeBomb extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(PipeBomb.class, EntityDataSerializers.INT);
    private static final int MAX_FUSE = 20;
    private static final float BOUNCINESS = 0.3f;
    private ItemStack stack = getDefaultPickupItem();
    private final List<ItemStack> fireworks = new ArrayList<>();

    public PipeBomb(EntityType<PipeBomb> type, Level level) {
        super(type, level);
        setFuse(MAX_FUSE);
        setBaseDamage(0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (!fireworks.isEmpty()) {
            stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(fireworks));
        }
        output.store("Stack", ItemStack.CODEC, stack);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        fireworks.clear();
        input.read("Stack", ItemStack.CODEC).ifPresentOrElse(stack -> this.stack = stack, () -> stack = getDefaultPickupItem());
        if (stack.has(DataComponents.CHARGED_PROJECTILES)) {
            fireworks.addAll(stack.get(DataComponents.CHARGED_PROJECTILES).itemCopies());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FUSE, 40);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return BlastItems.PIPE_BOMB.getDefaultInstance();
    }

    @Override
    public ItemStack getItem() {
        return stack;
    }

    @Override
    public void tick() {
        pickup = Pickup.DISALLOWED;
        if (tickCount >= 18000) {
            discard();
        }
        super.tick();
        if (getFuse() % 5 == 0) {
            playSound(BlastSoundEvents.PIPE_BOMB_TICK, 1, 1 + Math.abs((float) (getFuse() - MAX_FUSE) / MAX_FUSE));
        }
        // shorten the fuse
        setFuse(getFuse() - 1);
        if (getFuse() <= 0 && explode()) {
            discard();
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.COPPER_HIT;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (getDeltaMovement().length() > 0.3f) {
            float xMod = BOUNCINESS;
            float yMod = BOUNCINESS;
            float zMod = BOUNCINESS;
            switch (hitResult.getDirection()) {
                case DOWN, UP -> yMod = -yMod;
                case NORTH, SOUTH -> xMod = -xMod;
                case WEST, EAST -> zMod = -zMod;
            }
            setDeltaMovement(getDeltaMovement().x() * xMod, getDeltaMovement().y() * yMod, getDeltaMovement().z() * zMod);
            playSound(getDefaultHitGroundSoundEvent(), 1, 1.5f);
        } else {
            super.onHitBlock(hitResult);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
    }

    public void setItem(ItemStack item) {
        stack = item;
        if (item.has(DataComponents.CHARGED_PROJECTILES)) {
            fireworks.addAll(item.get(DataComponents.CHARGED_PROJECTILES).itemCopies());
        }
    }

    public int getFuse() {
        return entityData.get(FUSE);
    }

    public void setFuse(int fuse) {
        entityData.set(FUSE, fuse);
    }

    private boolean explode() {
        if (level() instanceof ServerLevel level) {
            if (random.nextInt(5) == 0 || !isInvisible()) {
                ItemStack stack = null;
                if (!fireworks.isEmpty()) {
                    stack = fireworks.getFirst();
                }
                float rad = 1.2f;
                float randX = (float) random.nextGaussian() * rad;
                float randY = random.nextFloat() * rad;
                float randZ = (float) random.nextGaussian() * rad;
                if (!isInvisible()) {
                    setInvisible(true);
                    randX = 0;
                    randY = 0;
                    randZ = 0;
                    if (stack == null) {
                        playSound(SoundEvents.CANDLE_EXTINGUISH, 3, 1);
                        level.sendParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 50, 0.1, 0.1, 0.1, 0);
                    }
                }
                if (stack != null) {
                    ItemStack firework = stack.copy();
                    for (int i = 0; i < firework.getCount(); i++) {
                        FireworkRocketEntity rocket = new FireworkRocketEntity(level, getX() + randX, getY() + randY, getZ() + randZ, stack);
                        level.addFreshEntity(rocket);
                        rocket.explode(level);
                        playSound(BlastSoundEvents.PIPE_BOMB_EXPLODE, 5, (float) (1 + random.nextGaussian() / 10f));
                    }
                    fireworks.removeFirst();
                    return fireworks.isEmpty();
                }
                return true;
            }
        }
        return false;
    }
}

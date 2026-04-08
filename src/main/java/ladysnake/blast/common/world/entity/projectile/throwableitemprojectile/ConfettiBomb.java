/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ConfettiBomb extends Bomb {
    public ConfettiBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setExplosionPower(500);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.CONFETTI_BOMB;
    }

    @Override
    public void explode() {
        if (level().isClientSide()) {
            for (int i = 0; i < 15; i++) {
                level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), random.nextGaussian() / 10f, Math.abs(random.nextGaussian() / 10f), random.nextGaussian() / 10f);
            }
            for (int i = 0; i < Math.round(getExplosionPower()); i++) {
                level().addParticle(BlastParticleTypes.CONFETTI, getX(), getY(), getZ(), random.nextGaussian() / 8f, Math.abs(random.nextGaussian() / 8f), random.nextGaussian() / 8f);
            }
            remove(RemovalReason.DISCARDED);
        }
        // TW: disgusting hack
        // since the server removes the bomb too early, we have to manually remove it after a delay
        // so we set the age to a normally unobtainable value (value) to mark it as "to be removed" for tick()
        if (tickCount > 0) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 4.0f, (1.5F + (level().getRandom().nextFloat() - level().getRandom().nextFloat()) * 0.2F) * 0.7F);
            tickCount = -1000;
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickCount += 1;
        if (tickCount < 0 && tickCount > -995) {
            remove(RemovalReason.DISCARDED);
        }
    }
}

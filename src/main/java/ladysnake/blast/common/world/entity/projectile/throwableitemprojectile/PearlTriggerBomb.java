/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class PearlTriggerBomb extends TriggerBomb {
    public PearlTriggerBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.PEARL_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return PearlBomb.CALCULATOR;
    }

    @Override
    public void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            CustomExplosionDamageCalculator calculator = getExplosionCalculator();
            createExplosion(calculator, position(), calculator.getPower().orElse(getExplosionPower()), ParticleTypes.REVERSE_PORTAL, ParticleTypes.REVERSE_PORTAL, SoundEvents.ENDERMAN_TELEPORT);
            for (int i = 0; i < 100; i++) {
                level().addParticle(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(), random.nextGaussian() / 8f, random.nextGaussian() / 8f, random.nextGaussian() / 8f);
            }
        }
    }
}

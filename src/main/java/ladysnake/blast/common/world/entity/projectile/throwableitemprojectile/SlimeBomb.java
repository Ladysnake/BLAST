/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.PowerlessExplosionDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SlimeBomb extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new PowerlessExplosionDamageCalculator() {
        @Override
        public void affectEntity(Vec3 pos, Entity entity) {
            double distance = Math.sqrt(entity.distanceToSqr(pos)) / 6;
            if (distance <= 1) {
                double dX = entity.getX() - pos.x();
                double dY = entity.getEyeY() - pos.y();
                double dZ = entity.getZ() - pos.z();
                double product = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                if (product != 0) {
                    dX /= product;
                    dY /= product;
                    dZ /= product;
                    double strength = (1 - distance) * 3;
                    entity.push(dX * strength, dY * strength, dZ * strength);
                    entity.hurtMarked = true;
                }
            }
        }
    };

    public SlimeBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.SLIME_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }

    @Override
    public void explode() {
        super.explode();
        for (int i = 0; i < 500; i++) {
            level().addParticle(ParticleTypes.SNEEZE, getX(), getY(), getZ(), random.nextGaussian() / 5, random.nextGaussian() / 5, random.nextGaussian() / 5);
        }
    }
}

/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level;

import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;

public class EntityExplosion {
    public void spawnEntities(Bomb bomb, EntityType<?> type, int amount, float delta) {
        if (bomb.level() instanceof ServerLevel level) {
            for (int i = 0; i < amount; i++) {
                Entity entity = type.create(level, EntitySpawnReason.TRIGGERED);
                if (entity instanceof Projectile projectile) {
                    projectile.setOwner(bomb.getOwner());
                    entity.setPos(bomb.position());
                    projectile.shoot(bomb.getRandom().nextGaussian() * delta, bomb.getRandom().nextGaussian() * delta, bomb.getRandom().nextGaussian() * delta, delta, 0);
                    level.addFreshEntity(entity);
                }
            }
        }
    }
}

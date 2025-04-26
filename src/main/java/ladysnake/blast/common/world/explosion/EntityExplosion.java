package ladysnake.blast.common.world.explosion;

import ladysnake.blast.common.entity.BombEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;

public class EntityExplosion {
    public void spawnEntities(BombEntity bomb, EntityType<?> type, int amount, float velocity) {
        if (bomb.getWorld() instanceof ServerWorld world) {
            for (int i = 0; i < amount; i++) {
                Entity entity = type.create(world, SpawnReason.TRIGGERED);
                if (entity instanceof ProjectileEntity projectileEntity) {
                    projectileEntity.setOwner(bomb.getOwner());
                    entity.setPosition(bomb.getPos());
                    projectileEntity.setVelocity(bomb.getRandom().nextGaussian() * velocity, bomb.getRandom().nextGaussian() * velocity, bomb.getRandom().nextGaussian() * velocity, velocity, 0);
                    world.spawnEntity(entity);
                }
            }
        }
    }
}

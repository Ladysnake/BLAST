package ladysnake.blast.common.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;

public class EntityExplosion extends CustomExplosion {
    public final EntityType<? extends Entity> entityToSpawn;
    public final int amount;
    public final float velocity;

    public EntityExplosion(World world, Entity entity, double x, double y, double z, EntityType<? extends Entity> entityToSpawn, int amount, float velocity) {
        super(world, entity, x, y, z, 0f, null, DestructionType.KEEP);
        this.entityToSpawn = entityToSpawn;
        this.amount = amount;
        this.velocity = velocity;
    }

    @Override
    public void affectWorld(boolean particles) {
        super.affectWorld(particles);
        if (!world.isClient) {
            for (int i = 0; i < amount; i++) {
                Entity entity = entityToSpawn.create(world);
                if (entity instanceof ProjectileEntity projectileEntity) {
                    projectileEntity.setOwner(this.entity);
                }
                entity.setPosition(getPosition());
                entity.setVelocity(random.nextGaussian() * velocity, random.nextGaussian() * velocity, random.nextGaussian() * velocity);
                world.spawnEntity(entity);
            }
        }
    }
}

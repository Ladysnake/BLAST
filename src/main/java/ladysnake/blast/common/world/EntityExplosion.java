package ladysnake.blast.common.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class EntityExplosion extends CustomExplosion {
    public EntityType<? extends Entity> entityToSpawn;
    public int amount;
    public float velocity;

    public EntityExplosion(World world, Entity entity, double x, double y, double z, EntityType<? extends Entity> entityToSpawn, int amount, float velocity) {
        super(world, entity, x, y, z, 0f, null, DestructionType.KEEP);
        this.entityToSpawn = entityToSpawn;
        this.amount = amount;
        this.velocity = velocity;
    }

    @Override
    public void collectBlocksAndDamageEntities() {
        super.collectBlocksAndDamageEntities();

        for (int i = 0; i < amount; i++) {
            Entity entity = entityToSpawn.create(world);
            if (entity instanceof ProjectileEntity) {
                ((ProjectileEntity) entity).setOwner(this.entity);

            }
            entity.setPos(this.x, this.y, this.z);
            entity.updateTrackedPosition(this.x, this.y + .5, this.z);
            entity.setVelocity(random.nextGaussian() * velocity, random.nextGaussian() * velocity, random.nextGaussian() * velocity);

            world.spawnEntity(entity);
        }
    }
}

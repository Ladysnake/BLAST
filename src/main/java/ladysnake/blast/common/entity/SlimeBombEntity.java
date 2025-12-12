package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.PowerlessExplosionBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SlimeBombEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new PowerlessExplosionBehavior() {
        @Override
        public void affectEntity(Vec3d pos, Entity entity) {
            double distance = Math.sqrt(entity.squaredDistanceTo(pos)) / 6;
            if (distance <= 1) {
                double dX = entity.getX() - pos.getX();
                double dY = entity.getEyeY() - pos.getY();
                double dZ = entity.getZ() - pos.getZ();
                double product = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                if (product != 0) {
                    dX /= product;
                    dY /= product;
                    dZ /= product;
                    double strength = (1 - distance) * 3;
                    entity.addVelocity(dX * strength, dY * strength, dZ * strength);
                    entity.knockedBack = true;
                }
            }
        }
    };

    public SlimeBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.SLIME_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }

    @Override
    public void explode() {
        super.explode();
        for (int i = 0; i < 500; i++) {
            getEntityWorld().addParticleClient(ParticleTypes.SNEEZE, getX(), getY(), getZ(), random.nextGaussian() / 5, random.nextGaussian() / 5, random.nextGaussian() / 5);
        }
    }
}

package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class SlimeTriggerBombEntity extends TriggerBombEntity {
    public SlimeTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.SLIME_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return SlimeBombEntity.BEHAVIOR;
    }

    @Override
    public void explode() {
        super.explode();
        for (int i = 0; i < 500; i++) {
            getWorld().addParticleClient(ParticleTypes.SNEEZE, getX(), getY(), getZ(), random.nextGaussian() / 5, random.nextGaussian() / 5, random.nextGaussian() / 5);
        }
    }
}

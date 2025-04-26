package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class PearlTriggerBombEntity extends TriggerBombEntity {
    public PearlTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.PEARL_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return PearlBombEntity.BEHAVIOR;
    }

    @Override
    public void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            CustomExplosionBehavior behavior = getExplosionBehavior();
            createExplosion(behavior, getPos(), behavior.getPower().orElse(getExplosionPower()), ParticleTypes.REVERSE_PORTAL, ParticleTypes.REVERSE_PORTAL, SoundEvents.ENTITY_ENDERMAN_TELEPORT);
            for (int i = 0; i < 100; i++) {
                getWorld().addParticleClient(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(), random.nextGaussian() / 8f, random.nextGaussian() / 8f, random.nextGaussian() / 8f);
            }
        }
    }
}

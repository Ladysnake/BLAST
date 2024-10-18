package ladysnake.blast.common.entity;

import ladysnake.blast.client.BlastClient;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class ConfettiTriggerBombEntity extends TriggerBombEntity {
    public ConfettiTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionRadius(500);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.CONFETTI_TRIGGER_BOMB;
    }

    @Override
    public void explode() {
        if (getWorld().isClient) {
            for (int i = 0; i < 15; i++) {
                getWorld().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), random.nextGaussian() / 10f, Math.abs(random.nextGaussian() / 10f), random.nextGaussian() / 10f);
            }
            for (int i = 0; i < Math.round(getExplosionRadius()); i++) {
                getWorld().addParticle(BlastClient.CONFETTI, getX(), getY(), getZ(), random.nextGaussian() / 8f, Math.abs(random.nextGaussian() / 8f), random.nextGaussian() / 8f);
            }
            remove(RemovalReason.DISCARDED);
        }
        // TW: disgusting hack
        // since the server removes the bomb too early, we have to manually remove it after a delay
        // so we set the age to a normally unobtainable value (value) to mark it as "to be removed" for tick()
        if (age > 0) {
            getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 4.0f, (1.5F + (getWorld().random.nextFloat() - getWorld().random.nextFloat()) * 0.2F) * 0.7F);
            age = -1000;
        }
    }

    @Override
    public void tick() {
        super.tick();
        age += 1;
        if (age < 0 && age > -995) {
            remove(RemovalReason.DISCARDED);
        }
    }
}

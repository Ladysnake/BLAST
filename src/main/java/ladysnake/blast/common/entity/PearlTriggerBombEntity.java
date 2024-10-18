package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.EnderExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class PearlTriggerBombEntity extends TriggerBombEntity {
    public PearlTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.PEARL_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new EnderExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), getExplosionRadius(), Explosion.DestructionType.DESTROY);
    }

    @Override
    public void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            for (int i = 0; i < 100; i++) {
                getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(), random.nextGaussian() / 8f, random.nextGaussian() / 8f, random.nextGaussian() / 8f);
            }
            CustomExplosion explosion = getExplosion();
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(getWorld().isClient);
        }
    }
}

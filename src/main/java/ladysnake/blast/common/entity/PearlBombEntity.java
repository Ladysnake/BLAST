package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.EnderExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class PearlBombEntity extends BombEntity {
    public PearlBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public PearlBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.PEARL_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new EnderExplosion(this.world, this.getOwner(), this.getX(), this.getY(), this.getZ(), 3f, Explosion.DestructionType.BREAK);
    }

    @Override
    public void explode() {
        if (this.ticksUntilRemoval == -1) {
            this.ticksUntilRemoval = 1;

            for (int i = 0; i < 100; i++) {
                this.world.addParticle(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian()/8f, this.random.nextGaussian()/8f, this.random.nextGaussian()/8f);
            }

            CustomExplosion explosion = this.getExplosion();
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);
        }
    }

}

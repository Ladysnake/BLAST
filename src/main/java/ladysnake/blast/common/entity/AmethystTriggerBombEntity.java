package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.EntityExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class AmethystTriggerBombEntity extends TriggerBombEntity {
    public AmethystTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setExplosionRadius(70f);
    }

    public AmethystTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
        this.setExplosionRadius(70f);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.AMETHYST_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new EntityExplosion(this.world, this.getOwner(), this.getX(), this.getY(), this.getZ(), BlastEntities.AMETHYST_SHARD, Math.round(this.getExplosionRadius()), 1.4f);
    }

}

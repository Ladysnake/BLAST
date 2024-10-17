package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.EntityExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class AmethystTriggerBombEntity extends TriggerBombEntity {
    public AmethystTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionRadius(70);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.AMETHYST_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new EntityExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), BlastEntities.AMETHYST_SHARD, Math.round(getExplosionRadius()), 1.4f);
    }

}

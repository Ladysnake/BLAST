package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.EntityExplosion;
import ladysnake.blast.common.world.explosion.PowerlessExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class FrostBombEntity extends BombEntity {
    public FrostBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionPower(70);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.FROST_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return PowerlessExplosionBehavior.INSTANCE;
    }

    @Override
    public void explode() {
        super.explode();
        new EntityExplosion().spawnEntities(this, BlastEntities.ICICLE, Math.round(getExplosionPower()), 1.4F);
    }
}

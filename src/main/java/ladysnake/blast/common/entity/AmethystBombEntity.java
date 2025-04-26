package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.EntityExplosion;
import ladysnake.blast.common.world.explosion.PowerlessExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class AmethystBombEntity extends BombEntity {
    public AmethystBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionPower(70);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.AMETHYST_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return PowerlessExplosionBehavior.INSTANCE;
    }

    @Override
    public void explode() {
        super.explode();
        new EntityExplosion().spawnEntities(this, BlastEntities.AMETHYST_SHARD, Math.round(getExplosionPower()), 1.4F);
    }
}

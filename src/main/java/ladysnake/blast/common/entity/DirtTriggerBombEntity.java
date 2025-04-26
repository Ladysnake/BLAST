package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DirtTriggerBombEntity extends TriggerBombEntity {
    public DirtTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionPower(2);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIRT_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return DirtBombEntity.BEHAVIOR;
    }
}

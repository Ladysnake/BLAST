package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DiamondTriggerBombEntity extends TriggerBombEntity {
    public DiamondTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIAMOND_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return DiamondBombEntity.BEHAVIOR;
    }
}

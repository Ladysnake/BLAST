package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GoldenTriggerBombEntity extends TriggerBombEntity {
    public GoldenTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.GOLDEN_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return GoldenBombEntity.BEHAVIOR;
    }
}

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class DirtTriggerBomb extends TriggerBomb {
    public DirtTriggerBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setExplosionPower(2);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIRT_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return DirtBomb.CALCULATOR;
    }
}

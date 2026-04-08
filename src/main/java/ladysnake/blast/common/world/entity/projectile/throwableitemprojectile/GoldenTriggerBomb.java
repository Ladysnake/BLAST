/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class GoldenTriggerBomb extends TriggerBomb {
    public GoldenTriggerBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.GOLDEN_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return GoldenBomb.CALCULATOR;
    }
}

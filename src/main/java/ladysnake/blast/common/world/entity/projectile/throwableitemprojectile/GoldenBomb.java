/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.EnchantedExplosionDamageCalculator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class GoldenBomb extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new EnchantedExplosionDamageCalculator(Enchantments.FORTUNE, 3);

    public GoldenBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.GOLDEN_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }
}

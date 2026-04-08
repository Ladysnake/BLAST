/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.EntityExplosion;
import ladysnake.blast.common.world.level.PowerlessExplosionDamageCalculator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class AmethystBomb extends Bomb {
    public AmethystBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setExplosionPower(70);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.AMETHYST_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return PowerlessExplosionDamageCalculator.INSTANCE;
    }

    @Override
    public void explode() {
        super.explode();
        new EntityExplosion().spawnEntities(this, BlastEntityTypes.AMETHYST_SHARD, Math.round(getExplosionPower()), 1.4F);
    }
}

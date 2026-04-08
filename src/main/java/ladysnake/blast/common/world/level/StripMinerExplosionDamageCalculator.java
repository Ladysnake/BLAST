/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;

public class StripMinerExplosionDamageCalculator extends CustomExplosionDamageCalculator {
    public static final CustomExplosionDamageCalculator INSTANCE = new StripMinerExplosionDamageCalculator();

    @Override
    public boolean pushesEntity(Entity entity) {
        if (entity instanceof ExperienceOrb || entity instanceof ItemEntity) {
            return false;
        }
        return super.pushesEntity(entity);
    }
}

/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.item;

import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TriggerBombItem extends BombItem {
    public TriggerBombItem(Properties properties, EntityType<Bomb> type) {
        super(properties, type);
    }

    @Override
    protected void playSoundEffects(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundSource.NEUTRAL, 0.8F, 0.5F);
    }
}

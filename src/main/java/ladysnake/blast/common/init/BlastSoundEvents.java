/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import net.minecraft.sounds.SoundEvent;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerSoundEvent;

public class BlastSoundEvents {
    public static final SoundEvent PIPE_BOMB_TICK = registerSoundEvent("entity.pipe_bomb.tick");
    public static final SoundEvent PIPE_BOMB_EXPLODE = registerSoundEvent("entity.pipe_bomb.explode");

    public static void initialize() {
    }
}

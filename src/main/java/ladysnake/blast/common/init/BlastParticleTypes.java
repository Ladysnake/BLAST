/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerParticleType;

public class BlastParticleTypes {
    public static SimpleParticleType CONFETTI = registerParticleType("confetti", FabricParticleTypes.simple(true));
    public static SimpleParticleType DRY_ICE = registerParticleType("dry_ice", FabricParticleTypes.simple(true));
    public static SimpleParticleType DRIPPING_FOLLY_RED_PAINT_DROP = registerParticleType("dripping_folly_red_paint_drop", FabricParticleTypes.simple(true));
    public static SimpleParticleType FALLING_FOLLY_RED_PAINT_DROP = registerParticleType("falling_folly_red_paint_drop", FabricParticleTypes.simple(true));
    public static SimpleParticleType LANDING_FOLLY_RED_PAINT_DROP = registerParticleType("landing_folly_red_paint_drop", FabricParticleTypes.simple(true));

    public static void init() {
    }
}

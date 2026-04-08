/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class DryIceParticle extends SingleQuadParticle {
    private final float maxAlpha;

    public DryIceParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
        super(level, x, y, z, xa, ya, za, sprites.first());
        quadSize *= 0.1F + level.getRandom().nextFloat() * 0.5F;
        lifetime = level.getRandom().nextIntBetweenInclusive(20, 100);
        hasPhysics = true;
        setSpriteFromAge(sprites);
        alpha = 0;
        maxAlpha = level.getRandom().nextFloat() / 25;
        yd = level.getRandom().nextFloat() / 25;
        xd = 0;
        zd = 0;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        BlockPos pos = BlockPos.containing(x, y, z);

        // fade and die if old enough
        if (age++ >= lifetime) {
            alpha -= 0.001f;
            if (alpha < 0) {
                remove();
            }
        } else {
            if (alpha <= maxAlpha) {
                alpha = Math.min(maxAlpha, alpha + 0.01F);
            }
        }

        if (!level.getBlockState(pos.offset(0, (int) -quadSize, 0)).isAir()) {
            yd /= 1.05;
        }

        if (!level.getBlockState(pos).isAir()) {
            lifetime = 0;
        }

        move(xd, -yd, zd);
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new DryIceParticle(level, x, y, z, xAux, yAux, zAux, sprites());
        }
    }
}

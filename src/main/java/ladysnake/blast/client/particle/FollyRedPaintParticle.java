package ladysnake.blast.client.particle;

import ladysnake.blast.client.BlastClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FluidState;

public class FollyRedPaintParticle extends SingleQuadParticle {
    FollyRedPaintParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        setSize(0.01f, 0.01f);
        gravity = 0.06f;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        updateAge();
        if (removed) {
            return;
        }
        yd -= gravity;
        move(xd, yd, zd);
        updateDeltaMovement();
        if (removed) {
            return;
        }
        xd *= 0.98f;
        yd *= 0.98f;
        zd *= 0.98f;
        BlockPos blockPos = BlockPos.containing(x, y, z);
        FluidState fluidState = level.getFluidState(blockPos);
        if (y < (double) ((float) blockPos.getY() + fluidState.getHeight(level, blockPos))) {
            remove();
        }
    }

    protected void updateAge() {
        if (lifetime-- <= 0) {
            remove();
        }
    }

    protected void updateDeltaMovement() {
    }

    @Environment(EnvType.CLIENT)
    static class Dripping extends FollyRedPaintParticle {
        private final ParticleOptions nextParticle;

        Dripping(ClientLevel level, double x, double y, double z, SpriteSet sprites, ParticleOptions nextParticle) {
            super(level, x, y, z, sprites);
            this.nextParticle = nextParticle;
            gravity *= 0.02f;
            lifetime = 40;
        }

        @Override
        protected void updateAge() {
            if (lifetime-- <= 0) {
                remove();
                level.addParticle(nextParticle, x, y, z, xd, yd, zd);
            }
        }

        @Override
        protected void updateDeltaMovement() {
            xd *= 0.02;
            yd *= 0.02;
            zd *= 0.02;
        }
    }

    @Environment(EnvType.CLIENT)
    static class Falling extends FollyRedPaintParticle {
        protected final ParticleOptions nextParticle;

        Falling(ClientLevel level, double x, double y, double z, SpriteSet sprites, ParticleOptions nextParticle) {
            this(level, x, y, z, (int) (64 / (level.getRandom().nextDouble() * 0.8 + 0.2)), sprites, nextParticle);
        }

        Falling(ClientLevel level, double x, double y, double z, int maxAge, SpriteSet sprites, ParticleOptions nextParticle) {
            super(level, x, y, z, sprites);
            this.nextParticle = nextParticle;
            lifetime = maxAge;
        }

        @Override
        protected void updateDeltaMovement() {
            if (onGround) {
                level.addParticle(nextParticle, x, y, z, 0, 0, 0);
                remove();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class Landing extends FollyRedPaintParticle {
        Landing(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
            super(level, x, y, z, sprites);
            lifetime = (int) (16 / (level.getRandom().nextDouble() * 0.8 + 0.2));
        }
    }

    @Environment(EnvType.CLIENT)
    public record DrippingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            Dripping dripping = new Dripping(level, x, y, z, sprites(), BlastClient.FALLING_FOLLY_RED_PAINT_DROP);
            dripping.gravity *= 0.01f;
            dripping.setLifetime(100);
            dripping.setColor(1f, 0f, 0.35f);
            return dripping;
        }
    }

    @Environment(EnvType.CLIENT)
    public record FallingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            Falling falling = new Falling(level, x, y, z, sprites(), BlastClient.LANDING_FOLLY_RED_PAINT_DROP);
            falling.gravity = 0.01f;
            falling.setColor(1f, 0f, 0.35f);
            return falling;
        }
    }

    @Environment(EnvType.CLIENT)
    public record LandingProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            Landing landing = new Landing(level, x, y, z, sprites());
            landing.setLifetime((int) (28 / (level.getRandom().nextDouble() * 0.8 + 0.2)));
            landing.setColor(1f, 0f, 0.35f);
            return landing;
        }
    }
}

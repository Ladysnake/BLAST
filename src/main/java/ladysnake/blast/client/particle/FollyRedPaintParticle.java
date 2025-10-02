package ladysnake.blast.client.particle;

import ladysnake.blast.client.BlastClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class FollyRedPaintParticle extends BillboardParticle {
    FollyRedPaintParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider.getFirst());
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.gravityStrength = 0.06f;
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    @Override
    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        this.updateAge();
        if (this.dead) {
            return;
        }
        this.velocityY -= this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.updateVelocity();
        if (this.dead) {
            return;
        }
        this.velocityX *= 0.98f;
        this.velocityY *= 0.98f;
        this.velocityZ *= 0.98f;
        BlockPos blockPos = BlockPos.ofFloored(this.x, this.y, this.z);
        FluidState fluidState = this.world.getFluidState(blockPos);
        if (this.y < (double) ((float) blockPos.getY() + fluidState.getHeight(this.world, blockPos))) {
            this.markDead();
        }
    }

    protected void updateAge() {
        if (this.maxAge-- <= 0) {
            this.markDead();
        }
    }

    protected void updateVelocity() {
    }

    @Environment(EnvType.CLIENT)
    public record LandingFollyRedPaintDropFactory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            Landing blockLeakParticle = new Landing(world, x, y, z, spriteProvider());
            blockLeakParticle.setMaxAge((int) (28 / (world.getRandom().nextDouble() * 0.8 + 0.2)));
            blockLeakParticle.setColor(1f, 0f, 0.35f);
            return blockLeakParticle;
        }
    }

    @Environment(EnvType.CLIENT)
    public record FallingFollyRedPaintDropFactory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            ContinuousFalling blockLeakParticle = new ContinuousFalling(world, x, y, z, spriteProvider(), BlastClient.LANDING_FOLLY_RED_PAINT_DROP);
            blockLeakParticle.gravityStrength = 0.01f;
            blockLeakParticle.setColor(1f, 0f, 0.35f);
            return blockLeakParticle;
        }
    }

    @Environment(EnvType.CLIENT)
    public record DrippingFollyRedPaintDropFactory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            Dripping dripping = new Dripping(world, x, y, z, spriteProvider(), BlastClient.FALLING_FOLLY_RED_PAINT_DROP);
            dripping.gravityStrength *= 0.01f;
            dripping.setMaxAge(100);
            dripping.setColor(1f, 0f, 0.35f);
            return dripping;
        }
    }

    @Environment(EnvType.CLIENT)
    static class Dripping extends FollyRedPaintParticle {
        private final ParticleEffect nextParticle;

        Dripping(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, ParticleEffect nextParticle) {
            super(world, x, y, z, spriteProvider);
            this.nextParticle = nextParticle;
            this.gravityStrength *= 0.02f;
            this.maxAge = 40;
        }

        @Override
        protected void updateAge() {
            if (this.maxAge-- <= 0) {
                this.markDead();
                this.world.addParticleClient(this.nextParticle, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
            }
        }

        @Override
        protected void updateVelocity() {
            this.velocityX *= 0.02;
            this.velocityY *= 0.02;
            this.velocityZ *= 0.02;
        }
    }

    @Environment(EnvType.CLIENT)
    static class ContinuousFalling extends Falling {
        protected final ParticleEffect nextParticle;

        ContinuousFalling(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, ParticleEffect nextParticle) {
            super(world, x, y, z, spriteProvider);
            this.nextParticle = nextParticle;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
                this.world.addParticleClient(this.nextParticle, this.x, this.y, this.z, 0, 0, 0);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class Falling extends FollyRedPaintParticle {
        Falling(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
            this(world, x, y, z, (int) (64 / (world.getRandom().nextDouble() * 0.8 + 0.2)), spriteProvider);
        }

        Falling(ClientWorld world, double x, double y, double z, int maxAge, SpriteProvider spriteProvider) {
            super(world, x, y, z, spriteProvider);
            this.maxAge = maxAge;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static class Landing extends FollyRedPaintParticle {
        Landing(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
            super(world, x, y, z, spriteProvider);
            this.maxAge = (int) (16 / (world.getRandom().nextDouble() * 0.8 + 0.2));
        }
    }
}

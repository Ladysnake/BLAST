package ladysnake.blast.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class DryIceParticle extends BillboardParticle {
    private final float maxAlpha;

    public DryIceParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.getFirst());

        this.scale *= 0.1f + world.getRandom().nextFloat() * 0.5f;
        this.maxAge = world.getRandom().nextBetween(20, 100);
        this.collidesWithWorld = true;
        this.updateSprite(spriteProvider);
        this.alpha = 0f;
        this.maxAlpha = world.getRandom().nextFloat() / 25f;
        this.velocityY = world.getRandom().nextFloat() / 25f;
        this.velocityX = 0;
        this.velocityZ = 0;
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;

        BlockPos pos = BlockPos.ofFloored(this.x, this.y, this.z);

        // fade and die if old enough
        if (this.age++ >= this.maxAge) {
            this.alpha -= 0.001f;
            if (alpha < 0f) {
                this.markDead();
            }
        } else {
            if (this.alpha <= this.maxAlpha) {
                this.alpha = Math.min(this.maxAlpha, this.alpha + 0.01f);
            }
        }

        if (!world.getBlockState(pos.add(0, (int) -this.scale, 0)).isAir()) {
            this.velocityY /= 1.05;
        }

        if (!world.getBlockState(pos).isAir()) {
            this.maxAge = 0;
        }

        this.move(velocityX, -velocityY, velocityZ);
    }

    @Environment(EnvType.CLIENT)
    public record DefaultFactory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new DryIceParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider());
        }
    }
}

package ladysnake.blast.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DryIceParticle extends SpriteBillboardParticle {

    private static final Random RANDOM = new Random();
    private final SpriteProvider spriteProvider;
    private final float maxAlpha;

    public DryIceParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;

        this.scale *= 0.1f + new Random().nextFloat() * 0.5f;
        this.maxAge = ThreadLocalRandom.current().nextInt(20, 100);
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);
        this.colorAlpha = 0f;
        this.maxAlpha = RANDOM.nextFloat() / 25f;
        this.velocityY = RANDOM.nextFloat() / 25f;
        this.velocityX = 0;
        this.velocityZ = 0;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    public void tick() {

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        BlockPos pos = new BlockPos(this.x, this.y, this.z);

        // fade and die if old enough
        if (this.age++ >= this.maxAge) {
            this.colorAlpha -= 0.001f;
            if (this.colorAlpha < 0f) {
                this.markDead();
            }
        } else {
            if (this.colorAlpha <= this.maxAlpha) {
                this.colorAlpha = Math.min(this.maxAlpha, this.colorAlpha + 0.01f);
            }
        }

        if (!world.getBlockState(pos.add(0, -this.scale, 0)).isAir()) {
            this.velocityY /= 1.05;
        }

        if (!world.getBlockState(pos).isAir()) {
            this.maxAge = 0;
        }

        this.move(velocityX, -velocityY, velocityZ);
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DryIceParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

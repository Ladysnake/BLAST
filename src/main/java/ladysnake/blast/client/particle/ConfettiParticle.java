package ladysnake.blast.client.particle;

import ladysnake.blast.mixin.client.ParticleAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ConfettiParticle extends SpriteBillboardParticle {
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private final float rotationXmod;
    private final float rotationYmod;
    private final float rotationZmod;
    private final float groundOffset;

    public ConfettiParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.scale *= 0.1f + world.getRandom().nextFloat() * 0.5f;
        this.maxAge = world.getRandom().nextBetween(20, 100);
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);
        this.alpha = 1f;

        this.maxAge = 1200; // live one minute
        this.red = world.getRandom().nextFloat();
        this.blue = world.getRandom().nextFloat();
        this.green = world.getRandom().nextFloat();


        this.gravityStrength = 0.1f;
        this.velocityX = velocityX * 10f;
        this.velocityY = velocityY * 10f;
        this.velocityZ = velocityZ * 10f;
        this.velocityMultiplier = 0.5f;

        this.rotationX = world.getRandom().nextFloat() * 360f;
        this.rotationY = world.getRandom().nextFloat() * 360f;
        this.rotationZ = world.getRandom().nextFloat() * 360f;
        this.rotationXmod = world.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);
        this.rotationYmod = world.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);
        this.rotationZmod = world.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);

        this.groundOffset = world.getRandom().nextFloat() / 100f + 0.001f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
        float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
        float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());

        Vector3f[] Vec3fs = new Vector3f[]{new Vector3f(-1, -1, 0), new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0)};
        float siZe = this.getSize(tickDelta);

        if (!this.onGround) {
            rotationX += rotationXmod;
            rotationY += rotationYmod;
            rotationZ += rotationZmod;

            for (int k = 0; k < 4; ++k) {
                Vector3f Vec3f2 = Vec3fs[k];
                Vec3f2.rotate(new Quaternionf(rotationX, rotationY, rotationZ, 1));
                Vec3f2.normalize(siZe);
                Vec3f2.add(x, y, z);
            }
        } else {
            rotationX = 90f;
            rotationY = 0;

            for (int k = 0; k < 4; ++k) {
                Vector3f Vec3f2 = Vec3fs[k];
                Vec3f2.rotate(new Quaternionf(rotationX, rotationY, rotationZ, 1));
                Vec3f2.normalize(siZe);
                Vec3f2.add(x, y + this.groundOffset, z);
            }
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightness(tickDelta);

        vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).texture(maxU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).texture(minU, maxV).color(red, green, blue, alpha).light(light);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            if (this.world.getFluidState(BlockPos.ofFloored(this.x, (this.y + 0.2), this.z)).isEmpty()) {
                if (this.world.getFluidState(BlockPos.ofFloored(this.x, (this.y - 0.01), this.z)).isIn(FluidTags.WATER)) {
                    this.onGround = true;
                    this.velocityY = 0;
                } else {
                    this.velocityY -= 0.04D * (double) this.gravityStrength;
                    ((ParticleAccessor) this).setStopped(false);
                    this.move(this.velocityX, this.velocityY, this.velocityZ);
                    if (this.ascending && this.y == this.prevPosY) {
                        this.velocityX *= 1.1D;
                        this.velocityZ *= 1.1D;
                    }

                    this.velocityX *= this.velocityMultiplier;
                    this.velocityY *= this.velocityMultiplier;
                    this.velocityZ *= this.velocityMultiplier;

                    this.velocityMultiplier = Math.min(0.98f, this.velocityMultiplier * 1.15f);

                    if (this.onGround) {
                        this.velocityX *= 0.699999988079071D;
                        this.velocityZ *= 0.699999988079071D;
                    }
                }
            } else {
                this.markDead();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ConfettiParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}

package ladysnake.blast.client.particle;

import ladysnake.blast.mixin.client.ParticleAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;

public class ConfettiParticle extends BillboardParticle {
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private final float rotationXmod;
    private final float rotationYmod;
    private final float rotationZmod;
    private final float groundOffset;

    public ConfettiParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.getFirst());

        this.scale *= 0.1f + world.getRandom().nextFloat() * 0.5f;
        this.maxAge = world.getRandom().nextBetween(20, 100);
        this.collidesWithWorld = true;
        this.updateSprite(spriteProvider);
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
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    @Override
    public void render(BillboardParticleSubmittable submittable, Camera camera, float tickProgress) {
        if (onGround) {
            rotationX = 90;
            rotationY = 0;
        } else {
            rotationX += rotationXmod;
            rotationY += rotationYmod;
            rotationZ += rotationZmod;
        }
        render(submittable, camera, eulerToQuaternion(rotationX, rotationY, rotationZ), tickProgress);
        render(submittable, camera, eulerToQuaternion(-rotationX, -rotationY, -rotationZ), tickProgress);
    }

    @Override
    protected void renderVertex(BillboardParticleSubmittable submittable, Quaternionf rotation, float x, float y, float z, float tickProgress) {
        super.renderVertex(submittable, rotation, x, y + (onGround ? groundOffset : 0), z, tickProgress);
    }

    public Quaternionf eulerToQuaternion(float x, float y, float z) {
        x *= ((float) Math.PI / 180F);
        y *= ((float) Math.PI / 180F);
        z *= ((float) Math.PI / 180F);

        float f = MathHelper.sin(0.5F * x);
        float g = MathHelper.cos(0.5F * x);
        float h = MathHelper.sin(0.5F * y);
        float i = MathHelper.cos(0.5F * y);
        float j = MathHelper.sin(0.5F * z);
        float k = MathHelper.cos(0.5F * z);
        x = f * i * k + g * h * j;
        y = g * h * k - f * i * j;
        z = f * h * k + g * i * j;
        float w = g * i * k - f * h * j;

        return new Quaternionf(x, y, z, w);
    }

    @Override
    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
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
                    if (this.ascending && this.y == this.lastY) {
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
    public record DefaultFactory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new ConfettiParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider());
        }
    }
}

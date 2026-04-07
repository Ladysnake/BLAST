package ladysnake.blast.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public class ConfettiParticle extends SingleQuadParticle {
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private final float rotationXmod;
    private final float rotationYmod;
    private final float rotationZmod;
    private final float groundOffset;

    public ConfettiParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
        super(level, x, y, z, xa, ya, za, sprites.first());
        quadSize *= 0.1f + level.getRandom().nextFloat() * 0.5f;
        lifetime = level.getRandom().nextIntBetweenInclusive(20, 100);
        hasPhysics = true;
        setSpriteFromAge(sprites);
        alpha = 1f;

        lifetime = 1200; // live one minute
        rCol = level.getRandom().nextFloat();
        bCol = level.getRandom().nextFloat();
        gCol = level.getRandom().nextFloat();

        gravity = 0.1f;
        xd = xa * 10f;
        yd = ya * 10f;
        zd = za * 10f;
        friction = 0.5f;

        rotationX = level.getRandom().nextFloat() * 360f;
        rotationY = level.getRandom().nextFloat() * 360f;
        rotationZ = level.getRandom().nextFloat() * 360f;
        rotationXmod = level.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);
        rotationYmod = level.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);
        rotationZmod = level.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);

        groundOffset = level.getRandom().nextFloat() / 100f + 0.001f;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
        if (onGround) {
            rotationX = 90;
            rotationY = 0;
        } else {
            rotationX += rotationXmod;
            rotationY += rotationYmod;
            rotationZ += rotationZmod;
        }
        extractRotatedQuad(particleTypeRenderState, camera, eulerToQuaternion(rotationX, rotationY, rotationZ), partialTickTime);
        extractRotatedQuad(particleTypeRenderState, camera, eulerToQuaternion(-rotationX, -rotationY, -rotationZ), partialTickTime);
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Quaternionf rotation, float x, float y, float z, float partialTickTime) {
        super.extractRotatedQuad(particleTypeRenderState, rotation, x, y + (onGround ? groundOffset : 0), z, partialTickTime);
    }

    public Quaternionf eulerToQuaternion(float x, float y, float z) {
        x *= ((float) Math.PI / 180F);
        y *= ((float) Math.PI / 180F);
        z *= ((float) Math.PI / 180F);

        float f = Mth.sin(0.5F * x);
        float g = Mth.cos(0.5F * x);
        float h = Mth.sin(0.5F * y);
        float i = Mth.cos(0.5F * y);
        float j = Mth.sin(0.5F * z);
        float k = Mth.cos(0.5F * z);
        x = f * i * k + g * h * j;
        y = g * h * k - f * i * j;
        z = f * h * k + g * i * j;
        float w = g * i * k - f * h * j;

        return new Quaternionf(x, y, z, w);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) {
            remove();
        } else {
            if (level.getFluidState(BlockPos.containing(x, (y + 0.2), z)).isEmpty()) {
                if (level.getFluidState(BlockPos.containing(x, (y - 0.01), z)).is(FluidTags.WATER)) {
                    onGround = true;
                    yd = 0;
                } else {
                    yd -= 0.04D * (double) gravity;
                    stoppedByCollision = false;
                    move(xd, yd, zd);
                    if (speedUpWhenYMotionIsBlocked && y == yo) {
                        xd *= 1.1D;
                        zd *= 1.1D;
                    }

                    xd *= friction;
                    yd *= friction;
                    zd *= friction;

                    friction = Math.min(0.98f, friction * 1.15f);

                    if (onGround) {
                        xd *= 0.699999988079071D;
                        zd *= 0.699999988079071D;
                    }
                }
            } else {
                remove();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new ConfettiParticle(level, x, y, z, xAux, yAux, zAux, sprites());
        }
    }
}

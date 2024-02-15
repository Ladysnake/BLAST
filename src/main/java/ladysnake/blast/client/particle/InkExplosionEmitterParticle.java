package ladysnake.blast.client.particle;

import ladysnake.blast.common.init.BlastParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class InkExplosionEmitterParticle extends NoRenderParticle {
    private int age_;
    private final int maxAge_ = 8;

    InkExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 6; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double e = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            this.world.addParticle(BlastParticles.INK_EXPLOSION, d, e, f, (float) this.age_ / (float) this.maxAge_, 0.0, 0.0);
        }

        ++this.age_;
        if (this.age_ == this.maxAge_) {
            this.markDead();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {

        public DefaultFactory(SpriteProvider spriteProvider) {
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new InkExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}

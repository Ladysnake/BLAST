package ladysnake.blast.common.init;

import ladysnake.blast.client.particle.*;
import ladysnake.blast.common.Blast;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;

public interface BlastParticles {
    DefaultParticleType DRY_ICE = FabricParticleTypes.simple(true);
    DefaultParticleType CONFETTI = FabricParticleTypes.simple(true);
    DefaultParticleType DRIPPING_FOLLY_RED_PAINT_DROP = FabricParticleTypes.simple(true);
    DefaultParticleType FALLING_FOLLY_RED_PAINT_DROP = FabricParticleTypes.simple(true);
    DefaultParticleType LANDING_FOLLY_RED_PAINT_DROP = FabricParticleTypes.simple(true);
    DefaultParticleType INK_EXPLOSION_EMITTER = FabricParticleTypes.simple(true);
    DefaultParticleType INK_EXPLOSION = FabricParticleTypes.simple(true);

    static void initialize() {
        initParticles(bind(Registry.PARTICLE_TYPE));
    }

    static void registerFactories() {
        ParticleFactoryRegistry.getInstance().register(DRY_ICE, DryIceParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(CONFETTI, ConfettiParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(DRIPPING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.DrippingFollyRedPaintDropFactory::new);
        ParticleFactoryRegistry.getInstance().register(FALLING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.FallingFollyRedPaintDropFactory::new);
        ParticleFactoryRegistry.getInstance().register(LANDING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.LandingFollyRedPaintDropFactory::new);
        ParticleFactoryRegistry.getInstance().register(INK_EXPLOSION_EMITTER, InkExplosionEmitterParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(INK_EXPLOSION, InkExplosionLargeParticle.Factory::new);
    }

    private static void initParticles(BiConsumer<ParticleType<?>, Identifier> registry) {
        registry.accept(DRY_ICE, Blast.id("dry_ice"));
        registry.accept(CONFETTI, Blast.id("confetti"));
        registry.accept(DRIPPING_FOLLY_RED_PAINT_DROP, Blast.id("dripping_folly_red_paint_drop"));
        registry.accept(FALLING_FOLLY_RED_PAINT_DROP, Blast.id("falling_folly_red_paint_drop"));
        registry.accept(LANDING_FOLLY_RED_PAINT_DROP, Blast.id("landing_folly_red_paint_drop"));
        registry.accept(INK_EXPLOSION_EMITTER, Blast.id("ink_explosion_emitter"));
        registry.accept(INK_EXPLOSION, Blast.id("ink_explosion"));
    }

    private static <T> BiConsumer<T, Identifier> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}

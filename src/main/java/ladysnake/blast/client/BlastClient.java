package ladysnake.blast.client;

import ladysnake.blast.client.particle.ConfettiParticle;
import ladysnake.blast.client.particle.DryIceParticle;
import ladysnake.blast.client.particle.FollyRedPaintParticle;
import ladysnake.blast.client.renderer.entity.AmethystShardRenderer;
import ladysnake.blast.client.renderer.entity.BlastBlockEntityRenderer;
import ladysnake.blast.client.renderer.entity.IcicleRenderer;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.world.entity.item.ColdDigger;
import ladysnake.blast.common.world.entity.item.Stripminer;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerParticleType;

@Environment(EnvType.CLIENT)
public class BlastClient implements ClientModInitializer {
    // particle types
    public static SimpleParticleType DRY_ICE = registerParticleType("dry_ice", FabricParticleTypes.simple(true));
    public static SimpleParticleType CONFETTI = registerParticleType("confetti", FabricParticleTypes.simple(true));
    public static SimpleParticleType DRIPPING_FOLLY_RED_PAINT_DROP = registerParticleType("dripping_folly_red_paint_drop", FabricParticleTypes.simple(true));
    public static SimpleParticleType FALLING_FOLLY_RED_PAINT_DROP = registerParticleType("falling_folly_red_paint_drop", FabricParticleTypes.simple(true));
    public static SimpleParticleType LANDING_FOLLY_RED_PAINT_DROP = registerParticleType("landing_folly_red_paint_drop", FabricParticleTypes.simple(true));

    @Override
    public void onInitializeClient() {
        initRenderers();
        initParticles();
    }

    private void initRenderers() {
        registerItemEntityRenders(
            BlastEntityTypes.BOMB, BlastEntityTypes.TRIGGER_BOMB,
            BlastEntityTypes.GOLDEN_BOMB, BlastEntityTypes.GOLDEN_TRIGGER_BOMB,
            BlastEntityTypes.DIAMOND_BOMB, BlastEntityTypes.DIAMOND_TRIGGER_BOMB,
            BlastEntityTypes.NAVAL_MINE,
            BlastEntityTypes.CONFETTI_BOMB, BlastEntityTypes.CONFETTI_TRIGGER_BOMB,
            BlastEntityTypes.DIRT_BOMB, BlastEntityTypes.DIRT_TRIGGER_BOMB,
            BlastEntityTypes.PEARL_BOMB, BlastEntityTypes.PEARL_TRIGGER_BOMB,
            BlastEntityTypes.AMETHYST_BOMB, BlastEntityTypes.AMETHYST_TRIGGER_BOMB,
            BlastEntityTypes.FROST_BOMB, BlastEntityTypes.FROST_TRIGGER_BOMB,
            BlastEntityTypes.SLIME_BOMB, BlastEntityTypes.SLIME_TRIGGER_BOMB,
            BlastEntityTypes.PIPE_BOMB
        );
        registerBlockEntityRender(BlastEntityTypes.GUNPOWDER_BLOCK, _ -> BlastBlocks.GUNPOWDER_BLOCK.defaultBlockState());
        registerBlockEntityRender(BlastEntityTypes.STRIPMINER, Stripminer::getState);
        registerBlockEntityRender(BlastEntityTypes.COLD_DIGGER, ColdDigger::getState);
        registerBlockEntityRender(BlastEntityTypes.BONESBURRIER, _ -> BlastBlocks.BONESBURRIER.defaultBlockState());

        EntityRenderers.register(BlastEntityTypes.AMETHYST_SHARD, AmethystShardRenderer::new);
        EntityRenderers.register(BlastEntityTypes.ICICLE, IcicleRenderer::new);
    }

    private void initParticles() {
        ParticleProviderRegistry.getInstance().register(DRY_ICE, DryIceParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CONFETTI, ConfettiParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(DRIPPING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.DrippingProvider::new);
        ParticleProviderRegistry.getInstance().register(FALLING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.FallingProvider::new);
        ParticleProviderRegistry.getInstance().register(LANDING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.LandingProvider::new);
    }

    @SafeVarargs
    private static void registerItemEntityRenders(EntityType<? extends ItemSupplier>... types) {
        for (EntityType<? extends ItemSupplier> entityType : types) {
            registerItemEntityRender(entityType);
        }
    }

    private static <T extends Entity & ItemSupplier> void registerItemEntityRender(EntityType<T> type) {
        EntityRenderers.register(type, ThrownItemRenderer::new);
    }

    private static <T extends Bomb> void registerBlockEntityRender(EntityType<T> type, Function<T, BlockState> stateGetter) {
        EntityRenderers.register(type, ctx -> new BlastBlockEntityRenderer<>(ctx, stateGetter));
    }
}

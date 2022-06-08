package ladysnake.blast.client;

import ladysnake.blast.client.particle.ConfettiParticle;
import ladysnake.blast.client.particle.DryIceParticle;
import ladysnake.blast.client.particle.FollyRedPaintParticle;
import ladysnake.blast.client.renderers.AmethystShardEntityRenderer;
import ladysnake.blast.client.renderers.BlastBlockEntityRenderer;
import ladysnake.blast.client.renderers.IcicleEntityRenderer;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.entity.ColdDiggerEntity;
import ladysnake.blast.common.entity.StripminerEntity;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastClient implements ClientModInitializer {

    // particle types
    public static DefaultParticleType DRY_ICE;
    public static DefaultParticleType CONFETTI;
    public static DefaultParticleType DRIPPING_FOLLY_RED_PAINT_DROP;
    public static DefaultParticleType FALLING_FOLLY_RED_PAINT_DROP;
    public static DefaultParticleType LANDING_FOLLY_RED_PAINT_DROP;

    public static void registerRenders() {
        registerItemEntityRenders(
                BlastEntities.BOMB,
                BlastEntities.TRIGGER_BOMB,
                BlastEntities.GOLDEN_BOMB,
                BlastEntities.GOLDEN_TRIGGER_BOMB,
                BlastEntities.DIAMOND_BOMB,
                BlastEntities.DIAMOND_TRIGGER_BOMB,
                BlastEntities.NAVAL_MINE,
                BlastEntities.CONFETTI_BOMB,
                BlastEntities.CONFETTI_TRIGGER_BOMB,
                BlastEntities.DIRT_BOMB,
                BlastEntities.DIRT_TRIGGER_BOMB,
                BlastEntities.PEARL_BOMB,
                BlastEntities.PEARL_TRIGGER_BOMB,
                BlastEntities.AMETHYST_BOMB,
                BlastEntities.AMETHYST_TRIGGER_BOMB,
                BlastEntities.FROST_BOMB,
                BlastEntities.FROST_TRIGGER_BOMB,
                BlastEntities.SLIME_BOMB,
                BlastEntities.SLIME_TRIGGER_BOMB
        );
        registerBlockEntityRender(BlastEntities.GUNPOWDER_BLOCK, e -> BlastBlocks.GUNPOWDER_BLOCK.getDefaultState());
        registerBlockEntityRender(BlastEntities.STRIPMINER, StripminerEntity::getState);
        registerBlockEntityRender(BlastEntities.COLD_DIGGER, ColdDiggerEntity::getState);
        registerBlockEntityRender(BlastEntities.BONESBURRIER, e -> BlastBlocks.BONESBURRIER.getDefaultState());

        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.GUNPOWDER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.STRIPMINER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.COLD_DIGGER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.BONESBURRIER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.REMOTE_DETONATOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.DRY_ICE, RenderLayer.getTranslucent());

        EntityRendererRegistry.register(BlastEntities.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
        EntityRendererRegistry.register(BlastEntities.ICICLE, IcicleEntityRenderer::new);

    }

    @SafeVarargs
    private static void registerItemEntityRenders(EntityType<? extends FlyingItemEntity>... entityTypes) {
        for (EntityType<? extends FlyingItemEntity> entityType : entityTypes) {
            registerItemEntityRender(entityType);
        }
    }

    private static <T extends Entity & FlyingItemEntity> void registerItemEntityRender(EntityType<T> entityType) {
        EntityRendererRegistry.register(entityType, ctx -> new FlyingItemEntityRenderer<>(ctx));
    }

    private static <T extends BombEntity> void registerBlockEntityRender(EntityType<T> block, Function<T, BlockState> stateGetter) {
        EntityRendererRegistry.register(block, ctx -> new BlastBlockEntityRenderer<>(ctx, stateGetter));
    }

    @Override
    public void onInitializeClient() {
        registerRenders();

        // particles
        DRY_ICE = Registry.register(Registry.PARTICLE_TYPE, "blast:dry_ice", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(DRY_ICE, DryIceParticle.DefaultFactory::new);
        CONFETTI = Registry.register(Registry.PARTICLE_TYPE, "blast:confetti", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(CONFETTI, ConfettiParticle.DefaultFactory::new);

        DRIPPING_FOLLY_RED_PAINT_DROP = Registry.register(Registry.PARTICLE_TYPE, "blast:dripping_folly_red_paint_drop", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(DRIPPING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.DrippingFollyRedPaintDropFactory::new);
        FALLING_FOLLY_RED_PAINT_DROP = Registry.register(Registry.PARTICLE_TYPE, "blast:falling_folly_red_paint_drop", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(FALLING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.FallingFollyRedPaintDropFactory::new);
        LANDING_FOLLY_RED_PAINT_DROP = Registry.register(Registry.PARTICLE_TYPE, "blast:landing_folly_red_paint_drop", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(LANDING_FOLLY_RED_PAINT_DROP, FollyRedPaintParticle.LandingFollyRedPaintDropFactory::new);
    }
}

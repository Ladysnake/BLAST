package ladysnake.blast.client;

import ladysnake.blast.client.particle.DryIceParticle;
import ladysnake.blast.client.renderers.BlastBlockEntityRenderer;
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
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
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

    @Override
    public void onInitializeClient() {
        registerRenders();

        // particles
        DRY_ICE = Registry.register(Registry.PARTICLE_TYPE, "blast:dry_ice", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(DRY_ICE, DryIceParticle.DefaultFactory::new);
    }

    public static void registerRenders() {
        registerItemEntityRenders(
                BlastEntities.BOMB,
                BlastEntities.GOLDEN_BOMB,
                BlastEntities.DIAMOND_BOMB,
                BlastEntities.NAVAL_MINE,
                BlastEntities.TRIGGER_BOMB,
                BlastEntities.GOLDEN_TRIGGER_BOMB,
                BlastEntities.DIAMOND_TRIGGER_BOMB
        );
        registerBlockEntityRender(BlastEntities.GUNPOWDER_BLOCK, e -> BlastBlocks.GUNPOWDER_BLOCK.getDefaultState());
        registerBlockEntityRender(BlastEntities.STRIPMINER, StripminerEntity::getState);
        registerBlockEntityRender(BlastEntities.COLD_DIGGER, ColdDiggerEntity::getState);

        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.GUNPOWDER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.STRIPMINER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.COLD_DIGGER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.DRY_ICE, RenderLayer.getTranslucent());
    }

    @SafeVarargs
    private static void registerItemEntityRenders(EntityType<? extends FlyingItemEntity>... entityTypes) {
        for (EntityType<? extends FlyingItemEntity> entityType : entityTypes) {
            registerItemEntityRender(entityType);
        }
    }

    private static <T extends Entity & FlyingItemEntity> void registerItemEntityRender(EntityType<T> entityType) {
        EntityRendererRegistry.INSTANCE.register(entityType, ctx -> new FlyingItemEntityRenderer<>(ctx));
    }

    private static <T extends BombEntity> void registerBlockEntityRender(EntityType<T> block, Function<T, BlockState> stateGetter) {
        EntityRendererRegistry.INSTANCE.register(block, ctx -> new BlastBlockEntityRenderer<>(ctx, stateGetter));
    }
}

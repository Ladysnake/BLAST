package ladysnake.blast.client;

import ladysnake.blast.client.renderers.AmethystShardEntityRenderer;
import ladysnake.blast.client.renderers.BlastBlockEntityRenderer;
import ladysnake.blast.client.renderers.IcicleEntityRenderer;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.entity.ColdDiggerEntity;
import ladysnake.blast.common.entity.StripminerEntity;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastClient implements ClientModInitializer {
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
                BlastEntities.SLIME_TRIGGER_BOMB,
                BlastEntities.PIPE_BOMB
        );

        registerBlockEntityRender(BlastEntities.GUNPOWDER_BLOCK, e -> BlastBlocks.GUNPOWDER_BLOCK.getDefaultState());
        registerBlockEntityRender(BlastEntities.STRIPMINER, StripminerEntity::getState);
        registerBlockEntityRender(BlastEntities.COLD_DIGGER, ColdDiggerEntity::getState);
        registerBlockEntityRender(BlastEntities.BONESBURRIER, e -> BlastBlocks.BONESBURRIER.getDefaultState());
        registerBlockEntityRender(BlastEntities.SMILESWEEPER, e -> BlastBlocks.SMILESWEEPER.getDefaultState());

        BlockRenderLayerMap.put(RenderLayer.getCutout(), BlastBlocks.GUNPOWDER_BLOCK, BlastBlocks.STRIPMINER, BlastBlocks.COLD_DIGGER, BlastBlocks.BONESBURRIER, BlastBlocks.SMILESWEEPER, BlastBlocks.REMOTE_DETONATOR);
        BlockRenderLayerMap.put(RenderLayer.getTranslucent(), BlastBlocks.DRY_ICE);

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
        EntityRendererRegistry.register(entityType, FlyingItemEntityRenderer::new);
    }

    private static <T extends BombEntity> void registerBlockEntityRender(EntityType<T> block, Function<T, BlockState> stateGetter) {
        EntityRendererRegistry.register(block, ctx -> new BlastBlockEntityRenderer<>(ctx, stateGetter));
    }

    @Override
    public void onInitializeClient(ModContainer modContainer) {
        registerRenders();

        // particle renderers registration
        BlastParticles.registerFactories();
    }
}

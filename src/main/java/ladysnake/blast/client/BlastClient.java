package ladysnake.blast.client;

import ladysnake.blast.client.network.EntityDispatcher;
import ladysnake.blast.client.renderers.BlastBlockEntityRenderer;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.entity.ColdDiggerEntity;
import ladysnake.blast.common.entity.StripminerEntity;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.network.Packets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerRenders();
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

        ClientSidePacketRegistry.INSTANCE.register(Packets.SPAWN, EntityDispatcher::spawnFrom);

        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.GUNPOWDER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.STRIPMINER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.COLD_DIGGER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlastBlocks.DRY_ICE, RenderLayer.getTranslucent());
    }

    private static void registerItemEntityRenders(EntityType<?>... entityTypes) {
        for (EntityType<?> entityType : entityTypes) {
            registerItemEntityRender(entityType);
        }
    }

    private static <T extends Entity> void registerItemEntityRender(EntityType<T> entityType) {
        EntityRendererRegistry.INSTANCE.register(entityType, (manager, context) -> new FlyingItemEntityRenderer<>(manager, context.getItemRenderer()));
    }

    private static <T extends BombEntity> void registerBlockEntityRender(EntityType<T> block, Function<T, BlockState> stateGetter) {
        EntityRendererRegistry.INSTANCE.register(block, (manager, context) -> new BlastBlockEntityRenderer<>(manager, stateGetter));
    }
}

/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.block.RemoteDetonatorBlock;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;

public class BlastModelGenerator extends FabricModelProvider {
    private static final Model TEMPLATE_STRIPMINER = new Model(Optional.of(Blast.id("block/template_stripminer")), Optional.empty(), TextureKey.PARTICLE);

    public BlastModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(BlastBlocks.GUNPOWDER_BLOCK);
        registerStripminer(generator, BlastBlocks.STRIPMINER, Blast.id("block/stripminer"));
        registerStripminer(generator, BlastBlocks.COLD_DIGGER, Blast.id("block/cold_digger"));
        generator.registerSingleton(BlastBlocks.BONESBURRIER, TexturedModel.CUBE_BOTTOM_TOP);
        registerRemoteDetonator(generator);
        generator.registerAxisRotated(BlastBlocks.DRY_ICE, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerSimpleCubeAll(BlastBlocks.FOLLY_RED_PAINT);
        generator.registerParented(BlastBlocks.FOLLY_RED_PAINT, BlastBlocks.FRESH_FOLLY_RED_PAINT);
        generator.registerSimpleCubeAll(BlastBlocks.DRIED_FOLLY_RED_PAINT);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(BlastItems.BOMB, Models.GENERATED);
        generator.register(BlastItems.TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.GOLDEN_BOMB, Models.GENERATED);
        generator.register(BlastItems.GOLDEN_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.DIAMOND_BOMB, Models.GENERATED);
        generator.register(BlastItems.DIAMOND_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.NAVAL_MINE, Models.GENERATED);
        generator.register(BlastItems.CONFETTI_BOMB, Models.GENERATED);
        generator.register(BlastItems.CONFETTI_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.DIRT_BOMB, Models.GENERATED);
        generator.register(BlastItems.DIRT_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.PEARL_BOMB, Models.GENERATED);
        generator.register(BlastItems.PEARL_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.SLIME_BOMB, Models.GENERATED);
        generator.register(BlastItems.SLIME_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.AMETHYST_BOMB, Models.GENERATED);
        generator.register(BlastItems.AMETHYST_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.FROST_BOMB, Models.GENERATED);
        generator.register(BlastItems.FROST_TRIGGER_BOMB, Models.GENERATED);
        generator.register(BlastItems.PIPE_BOMB, Models.GENERATED);
    }

    private void registerStripminer(BlockStateModelGenerator generator, Block block, Identifier textureId) {
        TEMPLATE_STRIPMINER.upload(block, TextureMap.of(TextureKey.PARTICLE, textureId), generator.modelCollector);
        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockModelId(block))).coordinate(BlockStateModelGenerator.UP_DEFAULT_ROTATION_OPERATIONS));
    }

    private void registerRemoteDetonator(BlockStateModelGenerator generator) {
        WeightedVariant filled = createWeightedVariant(TextureMap.getSubId(BlastBlocks.REMOTE_DETONATOR, "_filled"));
        WeightedVariant normal = createWeightedVariant(TextureMap.getId(BlastBlocks.REMOTE_DETONATOR));
        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(BlastBlocks.REMOTE_DETONATOR).with(BlockStateModelGenerator.createBooleanModelMap(RemoteDetonatorBlock.FILLED, filled, normal)));
    }
}

package ladysnake.blast.data.provider;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.block.RemoteDetonatorBlock;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class BlastModelProvider extends FabricModelProvider {
    private static final ModelTemplate TEMPLATE_STRIPMINER = new ModelTemplate(Optional.of(Blast.id("block/template_stripminer")), Optional.empty(), TextureSlot.PARTICLE);

    public BlastModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createTrivialCube(BlastBlocks.GUNPOWDER_BLOCK);
        registerStripminer(generators, BlastBlocks.STRIPMINER, Blast.id("block/stripminer"));
        registerStripminer(generators, BlastBlocks.COLD_DIGGER, Blast.id("block/cold_digger"));
        generators.createTrivialBlock(BlastBlocks.BONESBURRIER, TexturedModel.CUBE_TOP_BOTTOM);
        registerRemoteDetonator(generators);
        generators.createRotatedPillarWithHorizontalVariant(BlastBlocks.DRY_ICE, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        generators.createTrivialCube(BlastBlocks.FOLLY_RED_PAINT);
        generators.copyModel(BlastBlocks.FOLLY_RED_PAINT, BlastBlocks.FRESH_FOLLY_RED_PAINT);
        generators.createTrivialCube(BlastBlocks.DRIED_FOLLY_RED_PAINT);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(BlastItems.BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.GOLDEN_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.GOLDEN_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.DIAMOND_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.DIAMOND_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.NAVAL_MINE, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.CONFETTI_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.CONFETTI_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.DIRT_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.DIRT_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.PEARL_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.PEARL_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.SLIME_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.SLIME_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.AMETHYST_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.AMETHYST_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.FROST_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.FROST_TRIGGER_BOMB, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(BlastItems.PIPE_BOMB, ModelTemplates.FLAT_ITEM);
    }

    private void registerStripminer(BlockModelGenerators generators, Block block, Identifier textureId) {
        TEMPLATE_STRIPMINER.create(block, TextureMapping.singleSlot(TextureSlot.PARTICLE, new Material(textureId)), generators.modelOutput);
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block))).with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
    }

    private void registerRemoteDetonator(BlockModelGenerators generators) {
        MultiVariant filled = plainVariant(ModelLocationUtils.getModelLocation(BlastBlocks.REMOTE_DETONATOR, "_filled"));
        MultiVariant normal = plainVariant(ModelLocationUtils.getModelLocation(BlastBlocks.REMOTE_DETONATOR));
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlastBlocks.REMOTE_DETONATOR).with(BlockModelGenerators.createBooleanModelDispatch(RemoteDetonatorBlock.FILLED, filled, normal)));
    }
}

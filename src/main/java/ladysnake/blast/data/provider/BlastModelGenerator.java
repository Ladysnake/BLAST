/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class BlastModelGenerator extends FabricModelProvider {
    public BlastModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BlastBlocks.GUNPOWDER_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(BlastBlocks.FOLLY_RED_PAINT);
        blockStateModelGenerator.registerSimpleCubeAll(BlastBlocks.DRIED_FOLLY_RED_PAINT);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(BlastItems.BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.GOLDEN_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.GOLDEN_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.DIAMOND_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.DIAMOND_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.NAVAL_MINE, Models.GENERATED);
        itemModelGenerator.register(BlastItems.CONFETTI_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.CONFETTI_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.DIRT_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.DIRT_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.PEARL_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.PEARL_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.SLIME_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.SLIME_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.AMETHYST_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.AMETHYST_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.FROST_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.FROST_TRIGGER_BOMB, Models.GENERATED);
        itemModelGenerator.register(BlastItems.PIPE_BOMB, Models.GENERATED);
    }
}

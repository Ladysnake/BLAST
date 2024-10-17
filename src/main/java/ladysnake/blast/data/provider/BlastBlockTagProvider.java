/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlastBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlastBlockTagProvider(FabricDataOutput output) {
        super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
            .add(BlastBlocks.STRIPMINER)
            .add(BlastBlocks.COLD_DIGGER);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(BlastBlocks.DRY_ICE)
            .add(BlastBlocks.REMOTE_DETONATOR)
            .add(BlastBlocks.BONESBURRIER);
        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
            .add(BlastBlocks.GUNPOWDER_BLOCK)
            .add(BlastBlocks.FOLLY_RED_PAINT)
            .add(BlastBlocks.FRESH_FOLLY_RED_PAINT)
            .add(BlastBlocks.DRIED_FOLLY_RED_PAINT);
    }
}

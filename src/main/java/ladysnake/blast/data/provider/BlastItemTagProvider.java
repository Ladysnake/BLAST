/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BlastItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public BlastItemTagProvider(FabricDataOutput output) {
        super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.PIGLIN_LOVED)
            .add(BlastItems.GOLDEN_BOMB)
            .add(BlastItems.GOLDEN_TRIGGER_BOMB);
    }
}

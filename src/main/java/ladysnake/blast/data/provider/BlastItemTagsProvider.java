/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BlastItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public BlastItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ItemTags.PIGLIN_LOVED)
            .add(BlastItems.GOLDEN_BOMB)
            .add(BlastItems.GOLDEN_TRIGGER_BOMB);
    }
}

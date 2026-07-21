package ladysnake.blast.fabric.datagen.provider;

import ladysnake.blast.common.references.BlastItemIds;
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
        builder(ItemTags.PIGLIN_LOVED)
            .add(BlastItemIds.GOLDEN_BOMB)
            .add(BlastItemIds.GOLDEN_TRIGGER_BOMB);
    }
}

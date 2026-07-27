package ladysnake.blast.datagen.provider;

import ladysnake.blast.common.references.BlastBlockItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlastBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public BlastBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(BlockTags.MINEABLE_WITH_AXE)
            .add(BlastBlockItemIds.STRIPMINER)
            .add(BlastBlockItemIds.COLD_DIGGER);
        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BlastBlockItemIds.DRY_ICE)
            .add(BlastBlockItemIds.REMOTE_DETONATOR)
            .add(BlastBlockItemIds.BONESBURRIER);
        builder(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(BlastBlockItemIds.GUNPOWDER_BLOCK)
            .add(BlastBlockItemIds.FOLLY_RED_PAINT)
            .add(BlastBlockItemIds.FRESH_FOLLY_RED_PAINT)
            .add(BlastBlockItemIds.DRIED_FOLLY_RED_PAINT);
    }
}

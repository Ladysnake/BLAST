package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastBlocks;
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
        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
            .add(BlastBlocks.STRIPMINER)
            .add(BlastBlocks.COLD_DIGGER);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BlastBlocks.DRY_ICE)
            .add(BlastBlocks.REMOTE_DETONATOR)
            .add(BlastBlocks.BONESBURRIER);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(BlastBlocks.GUNPOWDER_BLOCK)
            .add(BlastBlocks.FOLLY_RED_PAINT)
            .add(BlastBlocks.FRESH_FOLLY_RED_PAINT)
            .add(BlastBlocks.DRIED_FOLLY_RED_PAINT);
    }
}

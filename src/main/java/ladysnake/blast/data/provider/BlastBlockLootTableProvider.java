/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.block.RemoteDetonatorBlock;
import ladysnake.blast.common.init.BlastBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.BuiltinRegistries;

import java.util.concurrent.CompletableFuture;

public class BlastBlockLootTableProvider extends FabricBlockLootTableProvider {
    public BlastBlockLootTableProvider(FabricDataOutput output) {
        super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
    }

    @Override
    public void generate() {
        addDrop(BlastBlocks.BONESBURRIER);
        addDrop(BlastBlocks.COLD_DIGGER);
        addDrop(BlastBlocks.DRIED_FOLLY_RED_PAINT);
        addDropWithSilkTouch(BlastBlocks.DRY_ICE);
        addDrop(BlastBlocks.FOLLY_RED_PAINT);
        addDrop(BlastBlocks.FRESH_FOLLY_RED_PAINT);
        addDrop(BlastBlocks.GUNPOWDER_BLOCK);
        addDrop(BlastBlocks.REMOTE_DETONATOR, drops(BlastBlocks.REMOTE_DETONATOR)
            .pool(
                addSurvivesExplosionCondition(
                    BlastBlocks.REMOTE_DETONATOR,
                    LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(
                            ItemEntry.builder(Items.ENDER_EYE)
                                .conditionally(BlockStatePropertyLootCondition.builder(BlastBlocks.REMOTE_DETONATOR).properties(StatePredicate.Builder.create().exactMatch(RemoteDetonatorBlock.FILLED, true)))
                        )
                )
            ));
        addDrop(BlastBlocks.STRIPMINER);

    }
}

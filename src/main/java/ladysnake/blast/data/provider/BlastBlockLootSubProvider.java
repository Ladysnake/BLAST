/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.level.block.RemoteDetonatorBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class BlastBlockLootSubProvider extends FabricBlockLootSubProvider {
    public BlastBlockLootSubProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(BlastBlocks.BONESBURRIER);
        dropSelf(BlastBlocks.COLD_DIGGER);
        dropSelf(BlastBlocks.DRIED_FOLLY_RED_PAINT);
        dropWhenSilkTouch(BlastBlocks.DRY_ICE);
        dropSelf(BlastBlocks.FOLLY_RED_PAINT);
        dropSelf(BlastBlocks.FRESH_FOLLY_RED_PAINT);
        dropSelf(BlastBlocks.GUNPOWDER_BLOCK);
        add(BlastBlocks.REMOTE_DETONATOR, createSingleItemTable(BlastBlocks.REMOTE_DETONATOR)
            .withPool(
                applyExplosionCondition(
                    BlastBlocks.REMOTE_DETONATOR,
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            LootItem.lootTableItem(Items.ENDER_EYE)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(BlastBlocks.REMOTE_DETONATOR).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RemoteDetonatorBlock.FILLED, true)))
                        )
                )
            ));
        dropSelf(BlastBlocks.STRIPMINER);
    }
}

/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BlastRecipeProvider extends FabricRecipeProvider {
    public BlastRecipeProvider(FabricDataOutput output) {
        super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.IRON_INGOTS).input(ConventionalItemTags.STRINGS).criterion("has_iron", conditionsFromTag(ConventionalItemTags.IRON_INGOTS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.IRON_INGOTS).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_iron", conditionsFromTag(ConventionalItemTags.IRON_INGOTS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.GOLDEN_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.GOLD_INGOTS).input(ConventionalItemTags.STRINGS).criterion("has_gold", conditionsFromTag(ConventionalItemTags.GOLD_INGOTS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.GOLDEN_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.GOLD_INGOTS).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_gold", conditionsFromTag(ConventionalItemTags.GOLD_INGOTS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.DIAMOND_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.DIAMOND_GEMS).input(ConventionalItemTags.STRINGS).criterion("has_diamond", conditionsFromTag(ConventionalItemTags.DIAMOND_GEMS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.DIAMOND_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.DIAMOND_GEMS).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_diamond", conditionsFromTag(ConventionalItemTags.DIAMOND_GEMS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.NAVAL_MINE, 4).input(Items.GUNPOWDER).input(Items.PRISMARINE_SHARD).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_prismarine", conditionsFromItem(Items.PRISMARINE_SHARD)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.CONFETTI_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.STRINGS).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).criterion("has_paper", conditionsFromItem(Items.PAPER)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.CONFETTI_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.REDSTONE_DUSTS).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).input(Items.PAPER).criterion("has_paper", conditionsFromItem(Items.PAPER)).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.DIRT_BOMB).input('D', ItemTags.DIRT).input('B', BlastItems.BOMB).pattern("DDD").pattern("DBD").pattern("DDD").criterion("has_bomb", conditionsFromItem(BlastItems.BOMB)).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.DIRT_TRIGGER_BOMB).input('D', ItemTags.DIRT).input('B', BlastItems.TRIGGER_BOMB).pattern("DDD").pattern("DBD").pattern("DDD").criterion("has_bomb", conditionsFromItem(BlastItems.TRIGGER_BOMB)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.PEARL_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.ENDER_PEARLS).input(ConventionalItemTags.STRINGS).criterion("has_pearl", conditionsFromTag(ConventionalItemTags.ENDER_PEARLS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.PEARL_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.ENDER_PEARLS).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_pearl", conditionsFromTag(ConventionalItemTags.ENDER_PEARLS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.SLIME_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.SLIME_BALLS).input(ConventionalItemTags.STRINGS).criterion("has_slime", conditionsFromTag(ConventionalItemTags.SLIME_BALLS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, BlastItems.SLIME_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(ConventionalItemTags.SLIME_BALLS).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_slime", conditionsFromTag(ConventionalItemTags.SLIME_BALLS)).offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, BlastItems.AMETHYST_BOMB, 4).input(Items.GUNPOWDER).input(Items.AMETHYST_BLOCK).input(ConventionalItemTags.STRINGS).criterion("has_amethyst", conditionsFromItem(Items.AMETHYST_BLOCK)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, BlastItems.AMETHYST_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(Items.AMETHYST_BLOCK).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_amethyst", conditionsFromItem(Items.AMETHYST_BLOCK)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, BlastItems.FROST_BOMB, 4).input(Items.GUNPOWDER).input(Items.PACKED_ICE).input(ConventionalItemTags.STRINGS).criterion("has_ice", conditionsFromItem(Items.PACKED_ICE)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, BlastItems.FROST_TRIGGER_BOMB, 4).input(Items.GUNPOWDER).input(Items.PACKED_ICE).input(ConventionalItemTags.REDSTONE_DUSTS).criterion("has_ice", conditionsFromItem(Items.PACKED_ICE)).offerTo(exporter);

        offerReversibleCompactingRecipesWithReverseRecipeGroup(exporter, RecipeCategory.MISC, Items.GUNPOWDER, RecipeCategory.BUILDING_BLOCKS, BlastBlocks.GUNPOWDER_BLOCK, "gunpowder_block_from_gunpowder", "gunpowder");
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, BlastBlocks.STRIPMINER).input('P', ItemTags.PLANKS).input('C', Items.STONECUTTER).input('T', Items.TNT).input('S', ItemTags.WOODEN_SLABS).pattern("PCP").pattern("PTP").pattern("PSP").criterion("has_tnt", conditionsFromItem(Items.TNT)).offerTo(exporter, "stripminer_from_tnt");
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, BlastBlocks.STRIPMINER).input('P', ItemTags.PLANKS).input('C', Items.STONECUTTER).input('G', BlastBlocks.GUNPOWDER_BLOCK).input('S', ItemTags.WOODEN_SLABS).pattern("PCP").pattern("PGP").pattern("PSP").criterion("has_gunpowder", conditionsFromItem(BlastBlocks.GUNPOWDER_BLOCK)).offerTo(exporter, "stripminer_from_gunpowder_block");
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, BlastBlocks.COLD_DIGGER).input('P', Items.PACKED_ICE).input('S', BlastBlocks.STRIPMINER).pattern(" P ").pattern("PSP").pattern(" P ").criterion("has_stripminer", conditionsFromItem(BlastBlocks.STRIPMINER)).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, BlastBlocks.BONESBURRIER).input('B', Items.BONE).input('R', Items.RED_DYE).input('H', Items.HONEY_BOTTLE).input('T', Items.TNT).input('P', Items.PINK_DYE).pattern("BRB").pattern("HTH").pattern("BPB").criterion("has_tnt", conditionsFromItem(Items.TNT)).offerTo(exporter, "bonesburrier_from_tnt");
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, BlastBlocks.BONESBURRIER).input('B', Items.BONE).input('R', Items.RED_DYE).input('H', Items.HONEY_BOTTLE).input('G', BlastBlocks.GUNPOWDER_BLOCK).input('P', Items.PINK_DYE).pattern("BRB").pattern("HGH").pattern("BPB").criterion("has_gunpowder", conditionsFromItem(BlastBlocks.GUNPOWDER_BLOCK)).offerTo(exporter, "bonesburrier_from_gunpowder_block");
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, BlastBlocks.REMOTE_DETONATOR).input('I', ConventionalItemTags.IRON_INGOTS).input('R', ConventionalItemTags.REDSTONE_DUSTS).input('E', ConventionalItemTags.ENDER_PEARLS).input('O', ConventionalItemTags.OBSIDIANS).pattern("IRI").pattern("RER").pattern("IOI").criterion("has_pearl", conditionsFromTag(ConventionalItemTags.ENDER_PEARLS)).offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, BlastBlocks.FRESH_FOLLY_RED_PAINT).input(BlastBlocks.FOLLY_RED_PAINT).input(Items.HONEY_BOTTLE).criterion("has_paint", conditionsFromItem(BlastBlocks.FOLLY_RED_PAINT)).offerTo(exporter, "fresh_folly_red_paint_from_folly_red_paint");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, BlastBlocks.FRESH_FOLLY_RED_PAINT).input(BlastBlocks.DRIED_FOLLY_RED_PAINT).input(Items.HONEY_BOTTLE).criterion("has_paint", conditionsFromItem(BlastBlocks.DRIED_FOLLY_RED_PAINT)).offerTo(exporter, "fresh_folly_red_paint_from_dried_folly_red_paint");
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(BlastBlocks.FOLLY_RED_PAINT), RecipeCategory.DECORATIONS, BlastBlocks.DRIED_FOLLY_RED_PAINT, 0.05F, 200).criterion("has_paint", conditionsFromItem(BlastBlocks.FOLLY_RED_PAINT)).offerTo(exporter, "dried_folly_red_paint_from_folly_red_paint");
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(BlastBlocks.FRESH_FOLLY_RED_PAINT), RecipeCategory.DECORATIONS, BlastBlocks.DRIED_FOLLY_RED_PAINT, 0.05F, 200).criterion("has_paint", conditionsFromItem(BlastBlocks.FRESH_FOLLY_RED_PAINT)).offerTo(exporter, "dried_folly_red_paint_from_fresh_folly_red_paint");
    }
}

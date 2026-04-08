/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.data.provider;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.item.crafting.PipeBombRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class BlastRecipeProvider extends FabricRecipeProvider {
    public BlastRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.TOOLS, BlastItems.BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.IRON_INGOTS).requires(ConventionalItemTags.STRINGS).unlockedBy("has_iron", has(ConventionalItemTags.IRON_INGOTS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.IRON_INGOTS).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_iron", has(ConventionalItemTags.IRON_INGOTS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.GOLDEN_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.GOLD_INGOTS).requires(ConventionalItemTags.STRINGS).unlockedBy("has_gold", has(ConventionalItemTags.GOLD_INGOTS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.GOLDEN_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.GOLD_INGOTS).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_gold", has(ConventionalItemTags.GOLD_INGOTS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.DIAMOND_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.DIAMOND_GEMS).requires(ConventionalItemTags.STRINGS).unlockedBy("has_diamond", has(ConventionalItemTags.DIAMOND_GEMS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.DIAMOND_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.DIAMOND_GEMS).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_diamond", has(ConventionalItemTags.DIAMOND_GEMS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.NAVAL_MINE, 4).requires(Items.GUNPOWDER).requires(Items.PRISMARINE_SHARD).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_prismarine", has(Items.PRISMARINE_SHARD)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.CONFETTI_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.STRINGS).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).unlockedBy("has_paper", has(Items.PAPER)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.CONFETTI_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.REDSTONE_DUSTS).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER).unlockedBy("has_paper", has(Items.PAPER)).save(output);
                shaped(RecipeCategory.TOOLS, BlastItems.DIRT_BOMB).define('D', ItemTags.DIRT).define('B', BlastItems.BOMB).pattern("DDD").pattern("DBD").pattern("DDD").unlockedBy("has_bomb", has(BlastItems.BOMB)).save(output);
                shaped(RecipeCategory.TOOLS, BlastItems.DIRT_TRIGGER_BOMB).define('D', ItemTags.DIRT).define('B', BlastItems.TRIGGER_BOMB).pattern("DDD").pattern("DBD").pattern("DDD").unlockedBy("has_bomb", has(BlastItems.TRIGGER_BOMB)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.PEARL_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.ENDER_PEARLS).requires(ConventionalItemTags.STRINGS).unlockedBy("has_pearl", has(ConventionalItemTags.ENDER_PEARLS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.PEARL_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.ENDER_PEARLS).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_pearl", has(ConventionalItemTags.ENDER_PEARLS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.SLIME_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.SLIME_BALLS).requires(ConventionalItemTags.STRINGS).unlockedBy("has_slime", has(ConventionalItemTags.SLIME_BALLS)).save(output);
                shapeless(RecipeCategory.TOOLS, BlastItems.SLIME_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(ConventionalItemTags.SLIME_BALLS).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_slime", has(ConventionalItemTags.SLIME_BALLS)).save(output);

                shapeless(RecipeCategory.COMBAT, BlastItems.AMETHYST_BOMB, 4).requires(Items.GUNPOWDER).requires(Items.AMETHYST_BLOCK).requires(ConventionalItemTags.STRINGS).unlockedBy("has_amethyst", has(Items.AMETHYST_BLOCK)).save(output);
                shapeless(RecipeCategory.COMBAT, BlastItems.AMETHYST_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(Items.AMETHYST_BLOCK).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_amethyst", has(Items.AMETHYST_BLOCK)).save(output);
                shapeless(RecipeCategory.COMBAT, BlastItems.FROST_BOMB, 4).requires(Items.GUNPOWDER).requires(Items.PACKED_ICE).requires(ConventionalItemTags.STRINGS).unlockedBy("has_ice", has(Items.PACKED_ICE)).save(output);
                shapeless(RecipeCategory.COMBAT, BlastItems.FROST_TRIGGER_BOMB, 4).requires(Items.GUNPOWDER).requires(Items.PACKED_ICE).requires(ConventionalItemTags.REDSTONE_DUSTS).unlockedBy("has_ice", has(Items.PACKED_ICE)).save(output);

                nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, Items.GUNPOWDER, RecipeCategory.BUILDING_BLOCKS, BlastBlocks.GUNPOWDER_BLOCK, "gunpowder_block_from_gunpowder", "gunpowder");
                shaped(RecipeCategory.REDSTONE, BlastBlocks.STRIPMINER).define('P', ItemTags.PLANKS).define('C', Items.STONECUTTER).define('T', Items.TNT).define('S', ItemTags.WOODEN_SLABS).pattern("PCP").pattern("PTP").pattern("PSP").unlockedBy("has_tnt", has(Items.TNT)).save(output, "stripminer_from_tnt");
                shaped(RecipeCategory.REDSTONE, BlastBlocks.STRIPMINER).define('P', ItemTags.PLANKS).define('C', Items.STONECUTTER).define('G', BlastBlocks.GUNPOWDER_BLOCK).define('S', ItemTags.WOODEN_SLABS).pattern("PCP").pattern("PGP").pattern("PSP").unlockedBy("has_gunpowder", has(BlastBlocks.GUNPOWDER_BLOCK)).save(output, "stripminer_from_gunpowder_block");
                shaped(RecipeCategory.REDSTONE, BlastBlocks.COLD_DIGGER).define('P', Items.PACKED_ICE).define('S', BlastBlocks.STRIPMINER).pattern(" P ").pattern("PSP").pattern(" P ").unlockedBy("has_stripminer", has(BlastBlocks.STRIPMINER)).save(output);
                shaped(RecipeCategory.REDSTONE, BlastBlocks.BONESBURRIER).define('B', Items.BONE).define('R', Items.RED_DYE).define('H', Items.HONEY_BOTTLE).define('T', Items.TNT).define('P', Items.PINK_DYE).pattern("BRB").pattern("HTH").pattern("BPB").unlockedBy("has_tnt", has(Items.TNT)).save(output, "bonesburrier_from_tnt");
                shaped(RecipeCategory.REDSTONE, BlastBlocks.BONESBURRIER).define('B', Items.BONE).define('R', Items.RED_DYE).define('H', Items.HONEY_BOTTLE).define('G', BlastBlocks.GUNPOWDER_BLOCK).define('P', Items.PINK_DYE).pattern("BRB").pattern("HGH").pattern("BPB").unlockedBy("has_gunpowder", has(BlastBlocks.GUNPOWDER_BLOCK)).save(output, "bonesburrier_from_gunpowder_block");
                shaped(RecipeCategory.REDSTONE, BlastBlocks.REMOTE_DETONATOR).define('I', ConventionalItemTags.IRON_INGOTS).define('R', ConventionalItemTags.REDSTONE_DUSTS).define('E', ConventionalItemTags.ENDER_PEARLS).define('O', ConventionalItemTags.OBSIDIANS).pattern("IRI").pattern("RER").pattern("IOI").unlockedBy("has_pearl", has(ConventionalItemTags.ENDER_PEARLS)).save(output);
                shapeless(RecipeCategory.DECORATIONS, BlastBlocks.FRESH_FOLLY_RED_PAINT).requires(BlastBlocks.FOLLY_RED_PAINT).requires(Items.HONEY_BOTTLE).unlockedBy("has_paint", has(BlastBlocks.FOLLY_RED_PAINT)).save(output, "fresh_folly_red_paint_from_folly_red_paint");
                shapeless(RecipeCategory.DECORATIONS, BlastBlocks.FRESH_FOLLY_RED_PAINT).requires(BlastBlocks.DRIED_FOLLY_RED_PAINT).requires(Items.HONEY_BOTTLE).unlockedBy("has_paint", has(BlastBlocks.DRIED_FOLLY_RED_PAINT)).save(output, "fresh_folly_red_paint_from_dried_folly_red_paint");
                SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlastBlocks.FOLLY_RED_PAINT), RecipeCategory.DECORATIONS, CookingBookCategory.BLOCKS, BlastBlocks.DRIED_FOLLY_RED_PAINT, 0.05F, 200).unlockedBy("has_paint", has(BlastBlocks.FOLLY_RED_PAINT)).save(output, "dried_folly_red_paint_from_folly_red_paint");
                SimpleCookingRecipeBuilder.smelting(Ingredient.of(BlastBlocks.FRESH_FOLLY_RED_PAINT), RecipeCategory.DECORATIONS, CookingBookCategory.BLOCKS, BlastBlocks.DRIED_FOLLY_RED_PAINT, 0.05F, 200).unlockedBy("has_paint", has(BlastBlocks.FRESH_FOLLY_RED_PAINT)).save(output, "dried_folly_red_paint_from_fresh_folly_red_paint");

                SpecialRecipeBuilder.special(PipeBombRecipe::new).save(output, Blast.id("pipe_bomb").toString());
            }
        };
    }

    @Override
    public String getName() {
        return Blast.MODID + "_recipes";
    }
}

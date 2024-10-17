/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package ladysnake.blast.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class PipeBombRecipe extends SpecialCraftingRecipe {

    private static final Ingredient PAPER = Ingredient.ofItems(Items.PAPER);
    private static final Ingredient DURATION_MODIFIER = Ingredient.ofItems(Items.GUNPOWDER);
    private static final Ingredient FIREWORK_STAR = Ingredient.ofItems(Items.FIREWORK_STAR);

    public PipeBombRecipe() {
        super(CraftingRecipeCategory.EQUIPMENT);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean bl = false;
        int i = 0;
        for (int j = 0; j < input.getSize(); ++j) {
            ItemStack itemStack = input.getStackInSlot(j);
            if (itemStack.isEmpty()) continue;
            if (PAPER.test(itemStack)) {
                if (bl) {
                    return false;
                }
                bl = true;
                continue;
            }
            if (!(DURATION_MODIFIER.test(itemStack) ? ++i > 3 : !FIREWORK_STAR.test(itemStack))) continue;
            return false;
        }
        return bl && i >= 1;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET, 3);
        // todo fix crafting
//        NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("Fireworks");
//        NbtList nbtList = new NbtList();
//        int i = 0;
//        for (int j = 0; j < input.getSize(); ++j) {
//            NbtCompound nbtCompound2;
//            ItemStack itemStack2 = input.getStackInSlot(j);
//            if (itemStack2.isEmpty()) continue;
//            if (DURATION_MODIFIER.test(itemStack2)) {
//                ++i;
//                continue;
//            }
//            if (!FIREWORK_STAR.test(itemStack2) || (nbtCompound2 = itemStack2.getSubNbt("Explosion")) == null) continue;
//            nbtList.add(nbtCompound2);
//        }
//        nbtCompound.putByte("Flight", (byte) i);
//        if (!nbtList.isEmpty()) {
//            nbtCompound.put("Explosions", nbtList);
//        }
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_ROCKET;
    }
}


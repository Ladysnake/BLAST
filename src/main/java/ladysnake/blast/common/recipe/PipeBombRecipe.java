/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package ladysnake.blast.common.recipe;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.init.BlastComponents;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PipeBombRecipe extends SpecialCraftingRecipe {

    private static final Ingredient BOMB = Ingredient.ofItems(BlastItems.BOMB);
    private static final Ingredient FIREWORK_ROCKET = Ingredient.ofItems(Items.FIREWORK_ROCKET);

    public PipeBombRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (BOMB.test(itemStack)) {
                bombCount++;
            } else if (FIREWORK_ROCKET.test(itemStack)) {
                fireworkCount++;
            } else if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return bombCount == 1 && fireworkCount > 0;
    }

    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack pipeBombStack = new ItemStack(BlastItems.PIPE_BOMB, 1);
        List<ItemStack> stackList = new ArrayList<>();

        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack invStack = craftingInventory.getStack(i);
            if (BOMB.test(invStack)) {
                bombCount++;
            } else if (FIREWORK_ROCKET.test(invStack)) {
                fireworkCount++;
                ItemStack stackToStore = craftingInventory.getStack(i).copy();
                stackToStore.setCount(1);
                stackList.add(stackToStore);
            } else if (!invStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        pipeBombStack.set(BlastComponents.FIREWORKS, stackList);

        return bombCount == 1 && fireworkCount > 0 ? pipeBombStack : ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Blast.PIPE_BOMB_RECIPE;
    }
}


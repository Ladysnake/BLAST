/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package ladysnake.blast.common.recipe;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastRecipeSerializers;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
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
    public boolean matches(CraftingRecipeInput input, World world) {
        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < input.getSize(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            if (BOMB.test(stack)) {
                bombCount++;
            } else if (FIREWORK_ROCKET.test(stack)) {
                fireworkCount++;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return bombCount == 1 && fireworkCount > 0;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack pipeBomb = BlastItems.PIPE_BOMB.getDefaultStack();
        List<ItemStack> fireworks = new ArrayList<>();
        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < input.getSize(); ++i) {
            ItemStack invStack = input.getStackInSlot(i);
            if (BOMB.test(invStack)) {
                bombCount++;
            } else if (FIREWORK_ROCKET.test(invStack)) {
                fireworkCount++;
                fireworks.add(input.getStackInSlot(i).copyWithCount(1));
            } else if (!invStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        pipeBomb.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(fireworks));
        return bombCount == 1 && fireworkCount > 0 ? pipeBomb : ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BlastRecipeSerializers.PIPE_BOMB;
    }
}


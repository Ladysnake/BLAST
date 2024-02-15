/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package ladysnake.blast.common.recipe;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PipeBombRecipe extends SpecialCraftingRecipe {
    private static final Ingredient BOMB = Ingredient.ofItems(BlastItems.BOMB);
    private static final Ingredient FIREWORK_ROCKET = Ingredient.ofItems(Items.FIREWORK_ROCKET);

    public PipeBombRecipe(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
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
    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack pipeBombStack = new ItemStack(BlastItems.PIPE_BOMB, 1);
        NbtList nbtList = new NbtList();

        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack invStack = craftingInventory.getStack(i);
            if (BOMB.test(invStack)) {
                bombCount++;
            } else if (FIREWORK_ROCKET.test(invStack)) {
                fireworkCount++;
                NbtCompound nbtCompound = new NbtCompound();
                ItemStack stackToStore = craftingInventory.getStack(i).copy();
                stackToStore.setCount(1);
                stackToStore.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            } else if (!invStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        pipeBombStack.getOrCreateNbt().put("Fireworks", nbtList);

        return bombCount == 1 && fireworkCount > 0 ? pipeBombStack : ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput() {
        return new ItemStack(BlastItems.PIPE_BOMB);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Blast.PIPE_BOMB_RECIPE;
    }
}


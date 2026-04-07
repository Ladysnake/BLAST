package ladysnake.blast.common.world.item.crafting;

import com.mojang.serialization.MapCodec;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastRecipeSerializers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class PipeBombRecipe extends CustomRecipe {
    public static final PipeBombRecipe INSTANCE = new PipeBombRecipe();
    public static final MapCodec<PipeBombRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, PipeBombRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private static final Ingredient BOMB = Ingredient.of(BlastItems.BOMB);
    private static final Ingredient FIREWORK_ROCKET = Ingredient.of(Items.FIREWORK_ROCKET);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
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
    public ItemStack assemble(CraftingInput input) {
        ItemStack pipeBomb = BlastItems.PIPE_BOMB.getDefaultInstance();
        List<ItemStack> fireworks = new ArrayList<>();
        int bombCount = 0;
        int fireworkCount = 0;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack invStack = input.getItem(i);
            if (BOMB.test(invStack)) {
                bombCount++;
            } else if (FIREWORK_ROCKET.test(invStack)) {
                fireworkCount++;
                fireworks.add(input.getItem(i).copyWithCount(1));
            } else if (!invStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        pipeBomb.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(fireworks));
        return bombCount == 1 && fireworkCount > 0 ? pipeBomb : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<PipeBombRecipe> getSerializer() {
        return BlastRecipeSerializers.PIPE_BOMB;
    }
}


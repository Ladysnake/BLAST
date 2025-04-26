package ladysnake.blast.common.init;

import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerRecipeSerializer;

public class BlastRecipeSerializers {
    public static final RecipeSerializer<PipeBombRecipe> PIPE_BOMB = registerRecipeSerializer("pipe_bomb", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(PipeBombRecipe::new));

    public static void init() {
    }
}

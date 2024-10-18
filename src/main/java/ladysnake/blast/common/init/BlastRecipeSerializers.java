package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlastRecipeSerializers {
    public static final RecipeSerializer<PipeBombRecipe> PIPE_BOMB = new SpecialRecipeSerializer<>(PipeBombRecipe::new);

    public static void init() {
        Registry.register(Registries.RECIPE_SERIALIZER, Blast.id("pipe_bomb"), PIPE_BOMB);
    }
}

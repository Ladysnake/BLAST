package ladysnake.blast.common;

import ladysnake.blast.common.init.*;
import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;

public class Blast implements ModInitializer {
    public static final String MODID = "blast";

    public static final SpecialRecipeSerializer<PipeBombRecipe> PIPE_BOMB_RECIPE = RecipeSerializer.register(
        "blast:crafting_special_pipe_bomb", new SpecialRecipeSerializer<>(PipeBombRecipe::new)
    );

    @Override
    public void onInitialize() {
        BlastSoundEvents.initialize();
        BlastComponents.init();
        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
    }
}



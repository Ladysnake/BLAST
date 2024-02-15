package ladysnake.blast.common;

import ladysnake.blast.common.init.*;
import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class Blast implements ModInitializer {
    public static final String MOD_ID = "blast";
    public static final SpecialRecipeSerializer<PipeBombRecipe> PIPE_BOMB_RECIPE = RecipeSerializer.register(
            "blast:crafting_special_pipe_bomb", new SpecialRecipeSerializer<>(PipeBombRecipe::new)
    );

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize(ModContainer modContainer) {
        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
        BlastSoundEvents.initialize();
        BlastParticles.initialize();
    }
}



package ladysnake.blast.common.init;

import ladysnake.blast.common.world.item.crafting.PipeBombRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerRecipeSerializer;

public class BlastRecipeSerializers {
    public static final RecipeSerializer<PipeBombRecipe> PIPE_BOMB = registerRecipeSerializer("pipe_bomb", new RecipeSerializer<>(PipeBombRecipe.MAP_CODEC, PipeBombRecipe.STREAM_CODEC));

    public static void init() {
    }
}

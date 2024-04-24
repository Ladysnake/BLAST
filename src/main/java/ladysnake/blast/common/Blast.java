package ladysnake.blast.common;

import ladysnake.blast.common.init.*;
import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Direction;

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



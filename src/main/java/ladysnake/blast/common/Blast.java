package ladysnake.blast.common;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.math.Direction;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class Blast implements ModInitializer {
	public static final String MODID = "blast";
	public static final SpecialRecipeSerializer<PipeBombRecipe> PIPE_BOMB_RECIPE = RecipeSerializer.register(
		"blast:crafting_special_pipe_bomb", new SpecialRecipeSerializer<>(PipeBombRecipe::new)
	);

	@Override
	public void onInitialize(ModContainer modContainer) {
		BlastSoundEvents.initialize();
		BlastEntities.init();
		BlastItems.init();
		BlastBlocks.init();
	}
}



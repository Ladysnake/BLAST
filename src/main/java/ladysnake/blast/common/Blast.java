package ladysnake.blast.common;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import ladysnake.blast.common.recipe.PipeBombRecipe;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Direction;

public class Blast implements ModInitializer {
    public static final String MODID = "blast";

    public static final SpecialRecipeSerializer<PipeBombRecipe> PIPE_BOMB_RECIPE = RecipeSerializer.register(
        "blast:crafting_special_pipe_bomb", new SpecialRecipeSerializer<>(PipeBombRecipe::new)
    );

    public static final TrackedDataHandler<Direction> FACING = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, Direction direction) {
            packetByteBuf.writeEnumConstant(direction);
        }

        public Direction read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readEnumConstant(Direction.class);
        }

        public Direction copy(Direction direction) {
            return direction;
        }
    };

    @Override
    public void onInitialize() {
        TrackedDataHandlerRegistry.register(FACING);
        BlastSoundEvents.initialize();
        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
    }
}



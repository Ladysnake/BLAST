package ladysnake.blast.common;

import ladysnake.blast.common.init.*;
import moriyashiine.strawberrylib.api.SLib;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class Blast implements ModInitializer {
    public static final String MODID = "blast";

    public static final TrackedDataHandler<Direction> FACING = new TrackedDataHandler<>() {
        @Override
        public PacketCodec<? super RegistryByteBuf, Direction> codec() {
            return Direction.PACKET_CODEC;
        }

        @Override
        public Direction copy(Direction value) {
            return value;
        }
    };

    @Override
    public void onInitialize() {
        SLib.init(MODID);
        FabricTrackedDataRegistry.register(id("facing"), FACING);
        BlastEntities.init();
        BlastBlocks.init();
        BlastItems.init();
        BlastComponentTypes.init();
        BlastRecipeSerializers.init();
        BlastSoundEvents.initialize();
    }

    public static Identifier id(String value) {
        return Identifier.of(MODID, value);
    }
}



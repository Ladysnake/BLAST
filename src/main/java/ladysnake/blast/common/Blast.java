package ladysnake.blast.common;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
        TrackedDataHandlerRegistry.register(FACING);
        BlastSoundEvents.initialize();
        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
    }

    public static Identifier id(String value) {
        return Identifier.of(MODID, value);
    }
}



package ladysnake.blast.common;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Direction;

public class Blast implements ModInitializer {
    public static final String MODID = "blast";

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

        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
    }
}



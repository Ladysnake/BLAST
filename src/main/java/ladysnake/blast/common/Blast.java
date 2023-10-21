package ladysnake.blast.common;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

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
        BlastSoundEvents.initialize();
        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
    }
}



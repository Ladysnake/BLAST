package ladysnake.blast.common.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface DetonatableBlock {
    void detonate(ServerLevel level, BlockPos pos);
}

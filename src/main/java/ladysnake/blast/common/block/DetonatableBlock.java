package ladysnake.blast.common.block;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface DetonatableBlock {
    void detonate(ServerWorld world, BlockPos pos);
}

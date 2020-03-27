package ladysnake.blast.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class GunpowderBlock extends FallingBlock {
    public GunpowderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if (world.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3f, Explosion.DestructionType.BREAK);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3f, Explosion.DestructionType.BREAK);
    }
}

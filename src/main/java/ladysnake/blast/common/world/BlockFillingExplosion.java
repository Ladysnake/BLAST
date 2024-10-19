package ladysnake.blast.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFillingExplosion extends CustomExplosion {
    public final BlockState blockStateToPlace;

    public BlockFillingExplosion(World world, Entity entity, double x, double y, double z, float power, BlockState blockStateToPlace) {
        super(world, entity, x, y, z, power, null, DestructionType.DESTROY);
        this.blockStateToPlace = blockStateToPlace;
    }

    @Override
    public void affectWorld(boolean particles) {
        super.affectWorld(particles);
        for (BlockPos pos : getAffectedBlocks()) {
            if (canPlace(pos)) {
                BlockState state = world.getBlockState(pos);
                if (state.isReplaceable()) {
                    if (!world.isClient) {
                        world.setBlockState(pos, blockStateToPlace);
                    }
                    state.getBlock().onDestroyedByExplosion(world, pos, this);
                }
            }
        }
    }
}

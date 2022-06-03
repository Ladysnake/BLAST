package ladysnake.blast.common.block;

import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.TntEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class RemoteDetonatorBlock extends Block {
    public static final BooleanProperty FILLED = BooleanProperty.of("filled");

    public RemoteDetonatorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FILLED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    public static void trigger(World world, BlockPos blockPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int x = -1; x < 1; x+=2) {
            for (int y = -1; y < 1; y+=2) {
                for (int z = -1; z < 1; z+=2) {
                    Block block = world.getBlockState(mutable.set(blockPos, x, y, z)).getBlock();
                    if (block instanceof DetonatableBlock detonatableBlock) {
                        detonatableBlock.detonate(world, blockPos);
                    }

                    if (block == Blocks.TNT) {
                        if (world.isClient) {
                            return;
                        }
                        TntEntity tntEntity = new TntEntity(world, (double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5, null);
                        tntEntity.setFuse(1);
                        world.spawnEntity(tntEntity);
                        world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        world.emitGameEvent(null, GameEvent.PRIME_FUSE, blockPos);
                    }
                }
            }
        }

        world.setBlockState(blockPos, world.getBlockState(blockPos).with(RemoteDetonatorBlock.FILLED, true));
    }
}

package ladysnake.blast.common.block;

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
import net.minecraft.util.math.Direction;
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

        for (Direction direction : Direction.values()) {
            Block block = world.getBlockState(mutable.set(blockPos, direction)).getBlock();

            if (block instanceof DetonatableBlock detonatableBlock) {
                detonatableBlock.detonate(world, mutable);
            }

            if (block == Blocks.TNT) {
                TntEntity tntEntity = new TntEntity(world, (double) mutable.getX() + 0.5, mutable.getY(), (double) mutable.getZ() + 0.5, null);
                tntEntity.setFuse(1);
                world.spawnEntity(tntEntity);
                world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.PRIME_FUSE, mutable);
                world.setBlockState(mutable, Blocks.AIR.getDefaultState());
            }
        }

        world.setBlockState(blockPos, world.getBlockState(blockPos).with(RemoteDetonatorBlock.FILLED, true));
    }
}
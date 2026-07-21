package ladysnake.blast.common.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

public class RemoteDetonatorBlock extends Block implements WorldlyContainerHolder {
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    public RemoteDetonatorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FILLED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        return state.getValue(FILLED) ? new FilledInventory(state, level, pos, new ItemStack(Items.ENDER_EYE)) : new DummyInventory();
    }

    public static void trigger(ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            Block block = level.getBlockState(mutable.setWithOffset(pos, direction)).getBlock();

            if (block instanceof DetonatableBlock detonatableBlock) {
                detonatableBlock.detonate(level, mutable);
            }
            if (block == Blocks.TNT) {
                PrimedTnt tnt = new PrimedTnt(level, mutable.getX() + 0.5, mutable.getY(), mutable.getZ() + 0.5, null);
                tnt.setFuse(0);
                level.addFreshEntity(tnt);
                level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1, 1);
                level.gameEvent(null, GameEvent.PRIME_FUSE, mutable);
                level.setBlockAndUpdate(mutable, Blocks.AIR.defaultBlockState());
            }
        }

        level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(RemoteDetonatorBlock.FILLED, true));
    }

    static class FilledInventory extends SimpleContainer implements WorldlyContainer {
        private static final int[] EMPTY_SLOTS = new int[0];

        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;
        private boolean changed;

        public FilledInventory(BlockState state, LevelAccessor level, BlockPos pos, ItemStack outputItem) {
            super(outputItem);
            this.state = state;
            this.level = level;
            this.pos = pos;
        }

        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int[] getSlotsForFace(Direction direction) {
            return direction == Direction.DOWN ? new int[]{0} : EMPTY_SLOTS;
        }

        @Override
        public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
            return !changed && direction == Direction.DOWN && itemStack.is(Items.ENDER_EYE);
        }

        @Override
        public void setChanged() {
            level.setBlock(pos, state.setValue(FILLED, false), Block.UPDATE_ALL);
            changed = true;
        }
    }

    static class DummyInventory extends SimpleContainer implements WorldlyContainer {
        private static final int[] EMPTY_SLOTS = new int[0];

        public DummyInventory() {
            super(0);
        }

        @Override
        public int[] getSlotsForFace(Direction direction) {
            return EMPTY_SLOTS;
        }

        @Override
        public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
            return false;
        }
    }
}

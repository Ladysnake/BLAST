package ladysnake.blast.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.TntEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class RemoteDetonatorBlock extends Block implements InventoryProvider {
    public static final BooleanProperty FILLED = BooleanProperty.of("filled");

    public RemoteDetonatorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FILLED, false));
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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return state.get(FILLED) ?
                new FilledInventory(state, world, pos, new ItemStack(Items.ENDER_EYE))
                : new DummyInventory();
    }

    static class FilledInventory extends SimpleInventory implements SidedInventory {
        private final BlockState state;
        private final WorldAccess world;
        private final BlockPos pos;
        private boolean dirty;

        public FilledInventory(BlockState state, WorldAccess world, BlockPos pos, ItemStack outputItem) {
            super(outputItem);
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        public int getMaxCountPerStack() {
            return 1;
        }

        public int[] getAvailableSlots(Direction side) {
            return side == Direction.DOWN ? new int[]{0} : new int[0];
        }

        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }

        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return !this.dirty && dir == Direction.DOWN && stack.isOf(Items.ENDER_EYE);
        }

        public void markDirty() {
            world.setBlockState(pos, state.with(FILLED, false), Block.NOTIFY_ALL);
            this.dirty = true;
        }
    }

    static class DummyInventory extends SimpleInventory implements SidedInventory {
        public DummyInventory() {
            super(0);
        }

        public int[] getAvailableSlots(Direction side) {
            return new int[0];
        }

        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }

        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return false;
        }
    }
}
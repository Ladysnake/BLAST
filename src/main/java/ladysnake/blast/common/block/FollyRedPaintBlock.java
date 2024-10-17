package ladysnake.blast.common.block;

import ladysnake.blast.client.BlastClient;
import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FollyRedPaintBlock extends Block {
    private final boolean canFreshen;

    public FollyRedPaintBlock(Settings settings, boolean canFreshen) {
        super(settings);
        this.canFreshen = canFreshen;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (canFreshen && stack.isOf(Items.HONEY_BOTTLE)) {
            if (world.isClient) {
                ParticleUtil.spawnParticle(world, pos, BlastClient.DRIPPING_FOLLY_RED_PAINT_DROP, UniformIntProvider.create(3, 5));
            } else {
                world.setBlockState(pos, BlastBlocks.FRESH_FOLLY_RED_PAINT.getDefaultState());
                ItemStack toGive = ItemUsage.exchangeStack(stack, player, stack.getRecipeRemainder());
                player.setStackInHand(hand, toGive);
            }
            world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return ItemActionResult.success(world.isClient);
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.getBlock() == BlastBlocks.FOLLY_RED_PAINT;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.getBlock() == BlastBlocks.FOLLY_RED_PAINT && random.nextInt(50) == 0) {
            world.setBlockState(pos, BlastBlocks.DRIED_FOLLY_RED_PAINT.getDefaultState());
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.getBlock() == BlastBlocks.DRIED_FOLLY_RED_PAINT) {
            return;
        }
        if (random.nextInt(5) != 0) {
            return;
        }
        Direction direction = Direction.random(random);
        if (direction == Direction.UP) {
            return;
        }
        BlockPos offset = pos.offset(direction);
        if (state.isOpaque() && world.getBlockState(offset).isSideSolidFullSquare(world, offset, direction.getOpposite())) {
            return;
        }
        double dX = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetX() * 0.6;
        double dY = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetY() * 0.6;
        double dZ = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetZ() * 0.6;
        world.addParticle(BlastClient.DRIPPING_FOLLY_RED_PAINT_DROP, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, 0.0, 0.0, 0.0);
    }
}


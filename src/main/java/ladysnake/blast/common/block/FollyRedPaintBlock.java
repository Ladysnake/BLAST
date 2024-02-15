package ladysnake.blast.common.block;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public class FollyRedPaintBlock extends Block {
    public FollyRedPaintBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
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
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        if (state.isOpaque() && blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
            return;
        }
        double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + (double) direction.getOffsetX() * 0.6;
        double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + (double) direction.getOffsetY() * 0.6;
        double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + (double) direction.getOffsetZ() * 0.6;
        world.addParticle(BlastParticles.DRIPPING_FOLLY_RED_PAINT_DROP, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.getBlock() == BlastBlocks.FOLLY_RED_PAINT;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.random.RandomGenerator random) {
        if (state.getBlock() == BlastBlocks.FOLLY_RED_PAINT && random.nextInt(50) == 0) {
            world.setBlockState(pos, BlastBlocks.DRIED_FOLLY_RED_PAINT.getDefaultState());
        }

        super.randomTick(state, world, pos, random);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).isOf(Items.HONEY_BOTTLE) && (state.getBlock() == BlastBlocks.FOLLY_RED_PAINT || state.getBlock() == BlastBlocks.DRIED_FOLLY_RED_PAINT)) {
            world.setBlockState(pos, BlastBlocks.FRESH_FOLLY_RED_PAINT.getDefaultState());
            ParticleUtil.spawnParticle(world, pos, BlastParticles.DRIPPING_FOLLY_RED_PAINT_DROP, UniformIntProvider.create(3, 5));
            world.playSound(null, pos, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);

            if (!player.isCreative()) {
                player.getStackInHand(hand).decrement(1);
                if (!player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE))) {
                    player.dropStack(new ItemStack(Items.GLASS_BOTTLE));
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}


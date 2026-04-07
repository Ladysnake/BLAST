package ladysnake.blast.common.world.level.block;

import ladysnake.blast.client.BlastClient;
import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FollyRedPaintBlock extends Block {
    private final boolean canFreshen;

    public FollyRedPaintBlock(Properties properties, boolean canFreshen) {
        super(properties);
        this.canFreshen = canFreshen;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (canFreshen && stack.is(Items.HONEY_BOTTLE)) {
            if (level.isClientSide()) {
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, BlastClient.DRIPPING_FOLLY_RED_PAINT_DROP, UniformInt.of(3, 5));
            } else {
                level.setBlockAndUpdate(pos, BlastBlocks.FRESH_FOLLY_RED_PAINT.defaultBlockState());
                ItemStack toGive = ItemUtils.createFilledResult(stack, player, stack.getCraftingRemainder().create());
                player.setItemInHand(hand, toGive);
            }
            level.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getBlock() == BlastBlocks.FOLLY_RED_PAINT;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getBlock() == BlastBlocks.FOLLY_RED_PAINT && random.nextInt(50) == 0) {
            level.setBlockAndUpdate(pos, BlastBlocks.DRIED_FOLLY_RED_PAINT.defaultBlockState());
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getBlock() == BlastBlocks.DRIED_FOLLY_RED_PAINT) {
            return;
        }
        if (random.nextInt(5) != 0) {
            return;
        }
        Direction direction = Direction.getRandom(random);
        if (direction == Direction.UP) {
            return;
        }
        BlockPos offset = pos.relative(direction);
        if (state.canOcclude() && level.getBlockState(offset).isFaceSturdy(level, offset, direction.getOpposite())) {
            return;
        }
        double dX = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + direction.getStepX() * 0.6;
        double dY = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + direction.getStepY() * 0.6;
        double dZ = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + direction.getStepZ() * 0.6;
        level.addParticle(BlastClient.DRIPPING_FOLLY_RED_PAINT_DROP, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, 0.0, 0.0, 0.0);
    }
}


package ladysnake.blast.common.block;

import ladysnake.blast.common.entity.StripminerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.concurrent.ThreadLocalRandom;

public class StripminerBlock extends Block implements DetonatableBlock {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public final EntityType<? extends StripminerEntity> type;

    public StripminerBlock(Settings settings, EntityType<? extends StripminerEntity> type) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
        this.type = type;
    }

    public static void primeStripminer(World world, BlockPos pos) {
        primeStripminer(world, pos, null);
    }

    private static void primeStripminer(World world, BlockPos pos, LivingEntity igniter) {
        if (!world.isClient && world.getBlockState(pos).getBlock() instanceof StripminerBlock) {
            StripminerEntity entity = ((StripminerBlock) world.getBlockState(pos).getBlock()).type.create(world);
            entity.setOwner(igniter);
            entity.setFacing(world.getBlockState(pos).get(FACING));
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(entity);
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

        }
    }

    @Override
    public void detonate(World world, BlockPos pos) {
        if (!world.isClient && world.getBlockState(pos).getBlock() instanceof StripminerBlock) {
            StripminerEntity entity = ((StripminerBlock) world.getBlockState(pos).getBlock()).type.create(world);
            entity.setFacing(world.getBlockState(pos).get(FACING));
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setFuse(0);
            world.spawnEntity(entity);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            if (world.isReceivingRedstonePower(pos)) {
                primeStripminer(world, pos);
                world.removeBlock(pos, false);
            }

        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            primeStripminer(world, pos);
            world.removeBlock(pos, false);
        }

    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient) {
            Direction randomDirection = switch (ThreadLocalRandom.current().nextInt(0, 6)) {
                case 0 -> Direction.UP;
                case 1 -> Direction.DOWN;
                case 2 -> Direction.NORTH;
                case 3 -> Direction.SOUTH;
                case 4 -> Direction.EAST;
                case 5 -> Direction.WEST;
                default -> Direction.NORTH;
            };

            StripminerEntity entity = this.type.create(world);
            entity.setFacing(randomDirection);
            entity.setFuse((short) (world.random.nextInt(entity.getFuseTimer() / 4) + entity.getFuseTimer() / 8));
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(entity);
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.onUse(state, world, pos, player, hand, hit);
        } else {
            primeStripminer(world, pos, player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            if (!player.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemStack.damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
                } else {
                    itemStack.decrement(1);
                }
            }

            return ActionResult.success(world.isClient);
        }
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire()) {
                BlockPos blockPos = hit.getBlockPos();
                primeStripminer(world, blockPos, entity instanceof LivingEntity ? (LivingEntity) entity : null);
                world.removeBlock(blockPos, false);
            }
        }

    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getPlayer() != null && ctx.getPlayer().isSneaking()) {
            return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
        } else {
            return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection());
        }
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}

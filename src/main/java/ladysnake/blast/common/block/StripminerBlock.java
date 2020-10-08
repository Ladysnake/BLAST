package ladysnake.blast.common.block;

import ladysnake.blast.common.entities.BombEntity;
import ladysnake.blast.common.entities.StripminerEntity;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
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
import net.minecraft.state.property.Properties;
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
import java.util.function.Consumer;

public class StripminerBlock extends Block {
    public static final DirectionProperty FACING = Properties.FACING;

    public StripminerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            if (world.isReceivingRedstonePower(pos)) {
                primeStripminer(world, pos);
                world.removeBlock(pos, false);
            }

        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            primeStripminer(world, pos);
            world.removeBlock(pos, false);
        }

    }

    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient) {
            Direction randomDirection = Direction.NORTH;
            switch (ThreadLocalRandom.current().nextInt(0, 6)) {
                case 0:
                    randomDirection = Direction.UP;
                    break;
                case 1:
                    randomDirection = Direction.DOWN;
                    break;
                case 2:
                    randomDirection = Direction.NORTH;
                    break;
                case 3:
                    randomDirection = Direction.SOUTH;
                    break;
                case 4:
                    randomDirection = Direction.EAST;
                    break;
                case 5:
                    randomDirection = Direction.WEST;
                    break;
            }

            StripminerEntity entity = (StripminerEntity) BlastEntities.STRIPMINER.create(world);
            entity.setFacing(randomDirection);
            entity.setPos(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
            world.spawnEntity(entity);
        }
    }

    public static void primeStripminer(World world, BlockPos pos) {
        primeStripminer(world, pos, (LivingEntity)null);
    }

    private static void primeStripminer(World world, BlockPos pos, LivingEntity igniter) {
        if (!world.isClient) {
            StripminerEntity entity = (StripminerEntity) BlastEntities.STRIPMINER.create(world);
            entity.setFacing(world.getBlockState(pos).get(FACING));
            entity.setPos(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
            world.spawnEntity(entity);
        }
    }

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
                    itemStack.damage(1, (LivingEntity)player, (playerEntity) -> {
                        ((PlayerEntity) playerEntity).sendToolBreakStatus(hand);
                    });
                } else {
                    itemStack.decrement(1);
                }
            }

            return ActionResult.success(world.isClient);
        }
    }

    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire()) {
                BlockPos blockPos = hit.getBlockPos();
                primeStripminer(world, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                world.removeBlock(blockPos, false);
            }
        }

    }

    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getPlayer() != null && ctx.getPlayer().isSneaking()) {
            return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
        } else {
            return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerLookDirection());
        }
    }

    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}

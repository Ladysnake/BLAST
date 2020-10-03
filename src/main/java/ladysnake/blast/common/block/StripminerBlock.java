package ladysnake.blast.common.block;

import ladysnake.blast.common.entities.StripminerEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Random;

public class StripminerBlock extends Block {
    public static final DirectionProperty FACING = Properties.FACING;

    public StripminerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if (world.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
            explode(world, pos);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (world.getBlockState(pos.add(-1, 0, 0)).getMaterial() == Material.FIRE ||
                world.getBlockState(pos.add(1, 0, 0)).getMaterial() == Material.FIRE ||
                world.getBlockState(pos.add(0, -1, 0)).getMaterial() == Material.FIRE ||
                world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.FIRE ||
                world.getBlockState(pos.add(0, 0, -1)).getMaterial() == Material.FIRE ||
                world.getBlockState(pos.add(0, 0, 1)).getMaterial() == Material.FIRE) {
            explode(world, pos);
        }
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BarrelBlockEntity) {
                ((BarrelBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }

    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient()) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        explode(world, pos);
    }

    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            if (projectile.isOnFire()) {
                BlockPos blockPos = hit.getBlockPos();
                explode(world, blockPos);
            }
        }
    }

    public void explode(World world, BlockPos pos) {
        if (!world.isClient) {
            StripminerEntity stripminerEntity = new StripminerEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, null, world.getBlockState(pos).get(FACING));
            world.spawnEntity(stripminerEntity);
        }

        world.removeBlock(pos, false);
//        // test for a blast resistant block behind the barrel
//        int x = 0;
//        int y = 0;
//        int z = 0;
//        switch (world.getBlockState(pos).get(FACING)) {
//            case DOWN:
//                y = 1;
//                break;
//            case UP:
//                y = -1;
//                break;
//            case NORTH:
//                z = 1;
//                break;
//            case SOUTH:
//                z = -1;
//                break;
//            case WEST:
//                x = 1;
//                break;
//            case EAST:
//                x = -1;
//                break;
//        }
//
//        world.removeBlock(pos, false);
//
//        for (int i = 0; i <= 24; i++) {
//            BlockPos bp = new BlockPos(pos.getX() + (-x) * (i), pos.getY() + (-y) * (i), pos.getZ() + (-z) * (i));
//            if (world.getBlockState(bp).getBlock().getBlastResistance() < 1200) {
//                CustomExplosion explosion = new CustomExplosion(world, null, bp.getX()+0.5, bp.getY() +0.5, bp.getZ() + 0.5, 2.5f, null, Explosion.DestructionType.BREAK);
//                explosion.collectBlocksAndDamageEntities();
//                explosion.affectWorld(true);
//            } else {
//                break;
//            }
//        }

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

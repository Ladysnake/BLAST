package ladysnake.blast.common.block;

import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Random;

public class GunpowderBlock extends FallingBlock {
    public static final BooleanProperty LIT = Properties.LIT;

    public GunpowderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
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

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient()) {
            world.setBlockState(pos, BlastBlocks.GUNPOWDER_BLOCK.getDefaultState().with(LIT, true));
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            explode(world, pos);
        } else {
            super.scheduledTick(state, world, pos, random);
        }
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
            world.removeBlock(pos, false);
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3f, true, Explosion.DestructionType.BREAK);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

}

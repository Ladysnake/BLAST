package ladysnake.blast.common.block;

import ladysnake.blast.common.entities.StripminerEntity;
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

            StripminerEntity stripminerEntity = new StripminerEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosion.getCausingEntity(), randomDirection);
            stripminerEntity.setFuse((short)(world.random.nextInt(stripminerEntity.getFuseTimer() / 4) + stripminerEntity.getFuseTimer() / 8));
            world.spawnEntity(stripminerEntity);
        }
    }

    public static void primeStripminer(World world, BlockPos pos) {
        primeStripminer(world, pos, (LivingEntity)null);
    }

    private static void primeStripminer(World world, BlockPos pos, LivingEntity igniter) {
        if (!world.isClient) {
            StripminerEntity stripminerEntity = new StripminerEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter, world.getBlockState(pos).get(FACING));
            world.spawnEntity(stripminerEntity);
            world.playSound((PlayerEntity)null, stripminerEntity.getX(), stripminerEntity.getY(), stripminerEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                    itemStack.damage(1, (LivingEntity)player, (Consumer)((playerEntity) -> {
                        ((PlayerEntity) playerEntity).sendToolBreakStatus(hand);
                    }));
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

    public void explode(World world, BlockPos pos, LivingEntity igniter) {
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

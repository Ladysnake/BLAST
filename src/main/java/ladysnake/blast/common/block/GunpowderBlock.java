package ladysnake.blast.common.block;

import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.init.BlastEntities;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class GunpowderBlock extends FallingBlock implements DetonatableBlock {
    public static final BooleanProperty LIT = Properties.LIT;

    public GunpowderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    public static void explode(World world, BlockPos pos) {
        explode(world, pos, null);
    }

    private static void explode(World world, BlockPos pos, LivingEntity igniter) {
        world.removeBlock(pos, false);

        if (!world.isClient) {
            BombEntity entity = BlastEntities.GUNPOWDER_BLOCK.create(world);
            entity.setOwner(igniter);
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(entity);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void detonate(World world, BlockPos pos) {
        explode(world, pos);
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

        if (world.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof FireBlock) {
            explode(world, pos);
        }
    }

    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        explode(world, pos, explosion.getCausingEntity());
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

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.onUse(state, world, pos, player, hand, hit);
        } else {
            explode(world, pos, player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            if (!player.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemStack.damage(1, player, (playerEntity) -> {
                        playerEntity.sendToolBreakStatus(hand);
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
            if (projectile.isOnFire()) {
                BlockPos blockPos = hit.getBlockPos();
                explode(world, blockPos);
            }
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

}

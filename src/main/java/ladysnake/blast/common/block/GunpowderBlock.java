package ladysnake.blast.common.block;

import com.mojang.serialization.MapCodec;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.init.BlastEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class GunpowderBlock extends FallingBlock implements DetonatableBlock {
    public static final MapCodec<GunpowderBlock> CODEC = createCodec(GunpowderBlock::new);

    public static final BooleanProperty LIT = Properties.LIT;

    public GunpowderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            if (world.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof FireBlock ||
                world.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof FireBlock) {
                explode(world, pos, null);
            }
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient) {
            explode(world, pos, explosion.getCausingEntity());
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if (!world.isClient && world.getBlockState(fromPos).isIn(BlockTags.FIRE)) {
            explode(world, pos, null);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            explode(world, pos, null);
        } else {
            super.scheduledTick(state, world, pos, random);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(Items.FLINT_AND_STEEL) || stack.isOf(Items.FIRE_CHARGE)) {
            if (!world.isClient) {
                explode(world, pos, player);
            }
            if (!player.isCreative()) {
                if (stack.isOf(Items.FLINT_AND_STEEL)) {
                    stack.damage(1, player, LivingEntity.getSlotForHand(hand));
                } else {
                    stack.decrement(1);
                }
            }
            return ItemActionResult.success(world.isClient);
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient && projectile.isOnFire()) {
            explode(world, hit.getBlockPos(), projectile.getOwner());
        }
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void detonate(ServerWorld world, BlockPos pos) {
        explode(world, pos, null);
    }

    private void explode(World world, BlockPos pos, Entity igniter) {
        BombEntity entity = BlastEntities.GUNPOWDER_BLOCK.create(world);
        entity.setOwner(igniter);
        entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        world.spawnEntity(entity);
        world.removeBlock(pos, false);
    }
}

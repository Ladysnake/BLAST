/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level.block;

import ladysnake.blast.common.world.entity.item.Stripminer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class StripminerBlock extends Block implements DetonatableBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public final EntityType<? extends Stripminer> type;

    public StripminerBlock(Properties properties, EntityType<? extends Stripminer> type) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        this.type = type;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
        } else {
            return defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide() && !oldState.is(state.getBlock()) && level.hasNeighborSignal(pos)) {
            prime(level, pos, null);
        }
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide()) {
            Stripminer stripminer = prime(level, pos, explosion.getIndirectSourceEntity());
            stripminer.setFuse(level.getRandom().nextInt(stripminer.getFuseTimer() / 4) + stripminer.getFuseTimer() / 8);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide() && level.hasNeighborSignal(pos)) {
            prime(level, pos, null);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            if (!level.isClientSide()) {
                prime(level, pos, player);
            }
            if (!player.isCreative()) {
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                } else {
                    stack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
        if (!level.isClientSide() && projectile.isOnFire()) {
            prime(level, blockHit.getBlockPos(), projectile.getOwner());
        }
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void detonate(ServerLevel level, BlockPos pos) {
        prime(level, pos, null).setFuse(0);
    }

    private Stripminer prime(Level level, BlockPos pos, Entity igniter) {
        Stripminer stripminer = type.create(level, EntitySpawnReason.TRIGGERED);
        stripminer.setOwner(igniter);
        if (level.getBlockState(pos).getBlock() instanceof StripminerBlock) {
            stripminer.setFacing(level.getBlockState(pos).getValue(FACING));
        } else {
            stripminer.setFacing(Direction.getRandom(level.getRandom()));
        }
        stripminer.setPosRaw(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(stripminer);
        level.playSound(null, stripminer.getX(), stripminer.getY(), stripminer.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1, 1);
        level.removeBlock(pos, false);
        return stripminer;
    }
}

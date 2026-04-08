/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level.block;

import com.mojang.serialization.MapCodec;
import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class GunpowderBlock extends FallingBlock implements DetonatableBlock {
    public static final MapCodec<GunpowderBlock> CODEC = simpleCodec(GunpowderBlock::new);

    public GunpowderBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.LIT, false));
    }

    @Override
    protected MapCodec<GunpowderBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getMapColor(level, pos).col;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) {
            if (level.getBlockState(pos.offset(-1, 0, 0)).getBlock() instanceof FireBlock ||
                level.getBlockState(pos.offset(1, 0, 0)).getBlock() instanceof FireBlock ||
                level.getBlockState(pos.offset(0, -1, 0)).getBlock() instanceof FireBlock ||
                level.getBlockState(pos.offset(0, 1, 0)).getBlock() instanceof FireBlock ||
                level.getBlockState(pos.offset(0, 0, -1)).getBlock() instanceof FireBlock ||
                level.getBlockState(pos.offset(0, 0, 1)).getBlock() instanceof FireBlock) {
                explode(level, pos, null);
            }
        }
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide()) {
            explode(level, pos, explosion.getIndirectSourceEntity());
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            for (Direction direction : Direction.values()) {
                if (level.getBlockState(pos.relative(direction)).is(Blocks.FIRE)) {
                    explode(level, pos, null);
                    return;
                }
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(BlockStateProperties.LIT)) {
            explode(level, pos, null);
        } else {
            super.tick(state, level, pos, random);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            if (!level.isClientSide()) {
                explode(level, pos, player);
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
            explode(level, blockHit.getBlockPos(), projectile.getOwner());
        }
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT);
    }

    @Override
    public void detonate(ServerLevel level, BlockPos pos) {
        explode(level, pos, null);
    }

    private void explode(Level level, BlockPos pos, Entity igniter) {
        Bomb bomb = BlastEntityTypes.GUNPOWDER_BLOCK.create(level, EntitySpawnReason.TRIGGERED);
        bomb.setOwner(igniter);
        bomb.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(bomb);
        level.removeBlock(pos, false);
    }
}

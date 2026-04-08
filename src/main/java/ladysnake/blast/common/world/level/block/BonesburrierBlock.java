/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level.block;

import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class BonesburrierBlock extends Block implements DetonatableBlock {
    public BonesburrierBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock())) {
            if (level.hasNeighborSignal(pos)) {
                prime(level, pos, null);
            }
        }
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide()) {
            Bomb bomb = prime(level, pos, explosion.getIndirectSourceEntity());
            bomb.setFuse(level.getRandom().nextInt(bomb.getFuseTimer() / 4) + bomb.getFuseTimer() / 8);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
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
    public void detonate(ServerLevel level, BlockPos pos) {
        prime(level, pos, null).setFuse(0);
    }

    private Bomb prime(Level level, BlockPos pos, Entity igniter) {
        Bomb bomb = BlastEntityTypes.BONESBURRIER.create(level, EntitySpawnReason.TRIGGERED);
        bomb.setOwner(igniter);
        bomb.setPosRaw(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(bomb);
        level.playSound(null, bomb.getX(), bomb.getY(), bomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1, 1);
        level.removeBlock(pos, false);
        return bomb;
    }
}

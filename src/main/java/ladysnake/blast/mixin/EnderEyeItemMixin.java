/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.mixin;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.level.block.RemoteDetonatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void blast$remoteDetonator(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos hitPos = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(160)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getBlockPos();

        BlockPos closestDetonatorPos = null;
        for (BlockPos foundPos : BlockPos.betweenClosed(hitPos.getX() - 10, hitPos.getY() - 10, hitPos.getZ() - 10, hitPos.getX() + 10, hitPos.getY() + 10, hitPos.getZ() + 10)) {
            if (closestDetonatorPos == null || foundPos.distSqr(hitPos) < closestDetonatorPos.distSqr(hitPos)) {
                BlockState state = level.getBlockState(foundPos);
                if (state.is(BlastBlocks.REMOTE_DETONATOR) && !state.getValue(RemoteDetonatorBlock.FILLED)) {
                    closestDetonatorPos = foundPos.immutable();
                }
            }
        }

        if (closestDetonatorPos != null) {
            ItemStack stack = player.getItemInHand(hand);
            player.getCooldowns().addCooldown(stack, 20);
            stack.consume(1, player);
            level.playSound(null, player.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1, 1);
            if (level instanceof ServerLevel serverLevel) {
                RemoteDetonatorBlock.trigger(serverLevel, closestDetonatorPos);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}

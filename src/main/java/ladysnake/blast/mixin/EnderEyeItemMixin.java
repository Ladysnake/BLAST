package ladysnake.blast.mixin;

import ladysnake.blast.common.block.RemoteDetonatorBlock;
import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> callbackInfoReturnable) {
        BlockPos hitPos = world.raycast(new RaycastContext(user.getEyePos(), user.getEyePos().add(user.getRotationVector().multiply(160)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user)).getBlockPos();

        BlockPos closestDetonatorPos = null;
        for (BlockPos foundPos : BlockPos.iterate(hitPos.getX() - 10, hitPos.getY() - 10, hitPos.getZ() - 10, hitPos.getX() + 10, hitPos.getY() + 10, hitPos.getZ() + 10)) {
            if (closestDetonatorPos == null || foundPos.getSquaredDistance(hitPos) < closestDetonatorPos.getSquaredDistance(hitPos)) {
                BlockState state = world.getBlockState(foundPos);
                if (state.isOf(BlastBlocks.REMOTE_DETONATOR) && !state.get(RemoteDetonatorBlock.FILLED)) {
                    closestDetonatorPos = foundPos.toImmutable();
                }
            }
        }

        if (closestDetonatorPos != null) {
            ItemStack stack = user.getStackInHand(hand);
            user.getItemCooldownManager().set(stack.getItem(), 20);
            stack.decrementUnlessCreative(1, user);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1, 1);
            if (world instanceof ServerWorld serverWorld) {
                RemoteDetonatorBlock.trigger(serverWorld, closestDetonatorPos);
            }
            callbackInfoReturnable.setReturnValue(TypedActionResult.success(stack));
        }
    }
}

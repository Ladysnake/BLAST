package ladysnake.blast.mixin;

import ladysnake.blast.common.block.RemoteDetonatorBlock;
import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
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
        user.getItemCooldownManager().set(Items.ENDER_EYE, 20);

        BlockHitResult raycast = world.raycast(new RaycastContext(user.getEyePos(), user.getEyePos().add(user.getRotationVector().multiply(160d)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user));
        BlockPos hitPos = raycast.getBlockPos();

        BlockPos closestDetonatorPos = null;
        for (BlockPos foundPos : BlockPos.iterate(hitPos.getX() - 10, hitPos.getY() - 10, hitPos.getZ() - 10, hitPos.getX() + 10, hitPos.getY() + 10, hitPos.getZ() + 10)) {
            if (closestDetonatorPos == null || foundPos.getSquaredDistance(hitPos) < closestDetonatorPos.getSquaredDistance(hitPos)) {
                if (world.getBlockState(foundPos).isOf(BlastBlocks.REMOTE_DETONATOR) && !world.getBlockState(foundPos).get(RemoteDetonatorBlock.FILLED)) {
                    closestDetonatorPos = foundPos;
                }
            }
        }
        
        if (closestDetonatorPos != null) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            System.out.println(world.getBlockState(closestDetonatorPos));

            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1.0f, 1.0f);

//            RemoteDetonatorBlock.trigger(world, closestDetonatorPos);

            callbackInfoReturnable.setReturnValue(TypedActionResult.consume(itemStack));
        }
    }
}
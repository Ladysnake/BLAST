package ladysnake.blast.mixin;

import ladysnake.blast.common.item.PipeBombItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @Inject(method = "onStackClicked", at = @At("HEAD"))
    private void blast$trackPipeBombPlayer(
        ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir
    ) {
        PipeBombItem.PIPE_PLAYER.set(player);
    }

    @Inject(method = "onStackClicked", at = @At("RETURN"))
    private void blast$stopTrackingPlayer(
        ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir
    ) {
        PipeBombItem.PIPE_PLAYER.remove();
    }

    @Inject(method = "onClicked", at = @At("HEAD"))
    private void blast$trackPipeBombPlayer(
        ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player,
        StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir
    ) {
        PipeBombItem.PIPE_PLAYER.set(player);
    }

    @Inject(method = "onClicked", at = @At("TAIL"))
    private void blast$stopTrackingPlayer(
        ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player,
        StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir
    ) {
        PipeBombItem.PIPE_PLAYER.set(player);
    }
}

package ladysnake.blast.mixin;

import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @Inject(method = "onStackClicked", at = @At("TAIL"))
    public void onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ItemStack itemStack = slot.getStack();
        if (itemStack.isOf(BlastItems.PIPE_BOMB)) {
            player.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.NEUTRAL, 0.5F, 1.0f);
        }
    }


    @Inject(method = "onClicked", at = @At("TAIL"))
    public void onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ItemStack itemStack = slot.getStack();
        if (itemStack.isOf(BlastItems.PIPE_BOMB)) {
            player.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.NEUTRAL, 0.5F, 1.0f);
        }
    }
}

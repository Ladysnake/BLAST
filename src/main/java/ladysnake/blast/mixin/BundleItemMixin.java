package ladysnake.blast.mixin;

import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @Inject(method = "onClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;add(Lnet/minecraft/item/ItemStack;)I"))
    public void onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (otherStack.isOf(BlastItems.PIPE_BOMB)) {
            player.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, 0.5F, 1);
        }
    }
}

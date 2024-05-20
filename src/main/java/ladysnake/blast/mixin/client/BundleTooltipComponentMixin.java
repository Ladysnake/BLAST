package ladysnake.blast.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ladysnake.blast.common.init.BlastComponents;
import ladysnake.blast.common.item.PipeBombItem;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentMixin {
    @ModifyExpressionValue(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent;get(I)Lnet/minecraft/item/ItemStack;"))
    public ItemStack blast$renderFakeItemInsteadOfPipeBomb(ItemStack itemStack) {
        if (itemStack.getItem() instanceof PipeBombItem) {
            if (itemStack.getComponents().contains(BlastComponents.FAKE_BUNDLE_DISPLAY_STACK)) {
                return itemStack.getComponents().get(BlastComponents.FAKE_BUNDLE_DISPLAY_STACK);
            }
        }

        return itemStack;
    }
}

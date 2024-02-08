package ladysnake.blast.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ladysnake.blast.common.item.PipeBombItem;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BundleTooltipComponent.class)
public abstract class BundleTooltipComponentMixin {
	@ModifyExpressionValue(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;"))
	public Object blast$renderFakeItemInsteadOfPipeBomb(Object object) {
		if (object instanceof ItemStack itemStack && itemStack.getItem() instanceof PipeBombItem) {
			if (itemStack.getOrCreateNbt().contains("FakeBundleDisplayStack")) {
				return ItemStack.fromNbt(itemStack.getOrCreateNbt().getCompound("FakeBundleDisplayStack"));
			}
		}

		return object;
	}
}

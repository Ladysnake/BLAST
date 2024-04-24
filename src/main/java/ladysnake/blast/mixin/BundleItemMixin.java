package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ladysnake.blast.common.init.BlastComponents;
import ladysnake.blast.common.item.PipeBombItem;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @WrapOperation(method = "onClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;add(Lnet/minecraft/item/ItemStack;)I"))
    private int blast$addPipeBombToBundle(BundleContentsComponent.Builder builder, ItemStack pipeBombStack, Operation<Integer> original) {
        return original.call(builder, createFakeDisplayStack(pipeBombStack));
    }

    @ModifyExpressionValue(method = "onStackClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack blast$addPipeBombToBundle2(ItemStack original) {
        return createFakeDisplayStack(original);
    }


    @Unique private static ItemStack createFakeDisplayStack(ItemStack original) {
        if (original.getItem() instanceof PipeBombItem) {
            // arm pipe bomb
            original.set(BlastComponents.ARMED, true);

            // give pipe bomb a fake display stack
            ItemStack fakeStack = ItemStack.EMPTY;
            while (fakeStack.getRarity() != Rarity.COMMON || fakeStack.isEmpty() || fakeStack.getCount() > fakeStack.getMaxCount()) {
                Random random = new Random();
                fakeStack = new ItemStack(Registries.ITEM.get(random.nextInt(Registries.ITEM.size())), original.getCount() * random.nextInt((64 / original.getMaxCount())));
                System.out.println(fakeStack);
            }


            original.set(BlastComponents.FAKE_BUNDLE_DISPLAY_STACK, fakeStack);
        }
        return original;
    }
}

package ladysnake.blast.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.item.PipeBombItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
@Mixin(BundleItem.class)
public abstract class ClientBundleItemMixin {

    @WrapOperation(method = "getTooltipData", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"))
    private Object blast$showFakeItem(ItemStack instance, ComponentType<BundleContentsComponent> componentType, Operation<Object> original) {
        Object component = original.call(instance, componentType);
        if (component instanceof BundleContentsComponent bundleContentsComponent) {
            return new BundleContentsComponent(
                bundleContentsComponent.stream().map(ItemStack::copy)
                    .map(stack -> {
                        if (stack.contains(BlastComponentTypes.FAKE_ITEM_ID)) {
                            var item = Registries.ITEM.getOrEmpty(stack.get(BlastComponentTypes.FAKE_ITEM_ID))
                                .orElse(PipeBombItem.getRandomFakeItem(Random.create()));
                            return new ItemStack(item, stack.getCount() * (64 / stack.getMaxCount()));
                        } else {
                            return stack;
                        }
                    })
                    .filter(Predicate.not(ItemStack::isEmpty))
                    .toList()
            );
        }
        return component;
    }
}

package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ladysnake.blast.common.item.PipeBombItem;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(BundleContentsComponent.Builder.class)
public class BundleContentsComponentBuilderMixin {
    @WrapOperation(method = "add(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"))
    private <E> void blast$primeBomb(List<E> instance, int i, E e, Operation<Void> original) {
        if (e instanceof ItemStack stack) {
            //noinspection unchecked
            e = (E) PipeBombItem.prime(stack);
        }
        original.call(instance, i, e);
    }
}

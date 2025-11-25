package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @Inject(method = "onStackClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;<init>(Lnet/minecraft/component/type/BundleContentsComponent;)V"))
    public void blast$primePipeBomb(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) ItemStack otherStack) {
        if (player == null || player.getWorld().isClient()) return;
        prime(otherStack, player);
    }

    @Inject(method = "onClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;add(Lnet/minecraft/item/ItemStack;)I"))
    public void blast$primePipeBomb(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (player == null || player.getWorld().isClient()) return;
        prime(otherStack, player);
    }

    @Unique
    private static void prime(ItemStack stack, PlayerEntity player) {
        if (stack.isOf(BlastItems.PIPE_BOMB)) {
            player.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, 0.5F, 1);
            stack.set(BlastComponentTypes.PRIMED, true);
            stack.set(BlastComponentTypes.FAKE_ITEM_ID, getRandomFakeItem(player.getRandom()));
        }
    }

    @Unique
    private static Identifier getRandomFakeItem(Random random) {
        Item item;
        do {
            item = Registries.ITEM.get(random.nextInt(Registries.ITEM.size()));
        }
        while (item.getMaxCount() != 64);
        return Registries.ITEM.getId(item);
    }
}

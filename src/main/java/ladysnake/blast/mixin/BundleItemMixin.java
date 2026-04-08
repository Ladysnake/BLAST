/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @ModifyExpressionValue(method = "overrideStackedOnOther", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack blast$primePipeBomb(ItemStack original, @Local(argsOnly = true) Player player) {
        prime(original, player);
        return original;
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;tryInsert(Lnet/minecraft/world/item/ItemStack;)I"))
    public void blast$primePipeBomb(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        prime(other, player);
    }

    @WrapOperation(method = "getTooltipImage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
    private Object blast$showFakeItem(ItemStack instance, DataComponentType<BundleContents> componentType, Operation<Object> original) {
        Object component = original.call(instance, componentType);
        if (component instanceof BundleContents bundleContents) {
            BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
            for (int i = 0; i < bundleContents.size(); i++) {
                ItemStackTemplate template = bundleContents.items().get(i);
                Identifier fakeItemId = template.get(BlastComponentTypes.FAKE_ITEM_ID);
                if (fakeItemId != null) {
                    mutable.items.set(i, new ItemStack(BuiltInRegistries.ITEM.getValue(fakeItemId), template.count() * (64 / template.getMaxStackSize())));
                }
            }
            return mutable.toImmutable();
        }
        return component;
    }

    @Unique
    private static void prime(ItemStack stack, Player player) {
        if (stack.is(BlastItems.PIPE_BOMB)) {
            player.playSound(SoundEvents.TRIPWIRE_CLICK_ON, 0.5F, 1);
            stack.set(BlastComponentTypes.PRIMED, true);
            stack.set(BlastComponentTypes.FAKE_ITEM_ID, getRandomFakeItem(player.getRandom()));
        }
    }

    @Unique
    private static Identifier getRandomFakeItem(RandomSource random) {
        Item item;
        do {
            item = BuiltInRegistries.ITEM.byId(random.nextInt(BuiltInRegistries.ITEM.size()));
        }
        while (item.getDefaultMaxStackSize() != 64);
        return BuiltInRegistries.ITEM.getKey(item);
    }
}

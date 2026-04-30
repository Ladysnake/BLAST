/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import ladysnake.blast.common.world.level.EnchantedExplosionDamageCalculator;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ApplyBonusCount.class)
public class ApplyBonusCountMixin {
    @ModifyExpressionValue(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/item/ItemInstance;)I"))
    private int blast$enchantedExplosion(int level, @Local(name = "tool") ItemInstance tool) {
        return level + EnchantedExplosionDamageCalculator.getFakeEnchantmentLevel(tool, Enchantments.FORTUNE);
    }
}

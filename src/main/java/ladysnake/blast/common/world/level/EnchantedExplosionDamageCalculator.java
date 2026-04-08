/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantedExplosionDamageCalculator extends CustomExplosionDamageCalculator {
    private final ResourceKey<Enchantment> enchantment;
    private final int enchantmentLevel;

    public EnchantedExplosionDamageCalculator(ResourceKey<Enchantment> enchantment, int enchantmentLevel) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
    }

    @Override
    public ItemStack getBreakTool(ServerLevel level) {
        ItemStack stack = Items.NETHERITE_PICKAXE.getDefaultInstance();
        stack.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), enchantmentLevel);
        return stack;
    }
}

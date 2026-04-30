/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.init.BlastComponentTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantedExplosionDamageCalculator extends CustomExplosionDamageCalculator {
    private static final Identifier PLACEHOLDER_ID = Blast.id("empty");

    private final ResourceKey<Enchantment> enchantment;
    private final int enchantmentLevel;

    public EnchantedExplosionDamageCalculator(ResourceKey<Enchantment> enchantment, int enchantmentLevel) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
    }

    @Override
    public ItemStack getBreakTool(ServerLevel level) {
        ItemStack stack = Items.NETHERITE_PICKAXE.getDefaultInstance();
        stack.set(BlastComponentTypes.FAKE_ENCHANTMENT_ID, enchantment.identifier());
        stack.set(BlastComponentTypes.FAKE_ENCHANTMENT_LEVEL, enchantmentLevel);
        return stack;
    }

    public static int getFakeEnchantmentLevel(ItemInstance tool, ResourceKey<Enchantment> enchantment) {
        if (tool.getOrDefault(BlastComponentTypes.FAKE_ENCHANTMENT_ID, PLACEHOLDER_ID) == enchantment.identifier()) {
            return tool.getOrDefault(BlastComponentTypes.FAKE_ENCHANTMENT_LEVEL, 0);
        }
        return 0;
    }
}

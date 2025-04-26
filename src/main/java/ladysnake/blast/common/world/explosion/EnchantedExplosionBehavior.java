package ladysnake.blast.common.world.explosion;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;

public class EnchantedExplosionBehavior extends CustomExplosionBehavior {
    private final RegistryKey<Enchantment> enchantment;
    private final int level;

    public EnchantedExplosionBehavior(RegistryKey<Enchantment> enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public ItemStack getBreakTool(ServerWorld world) {
        ItemStack stack = Items.NETHERITE_PICKAXE.getDefaultStack();
        stack.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment), level);
        return stack;
    }
}

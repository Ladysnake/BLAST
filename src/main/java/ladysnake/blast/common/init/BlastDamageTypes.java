package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class BlastDamageTypes {
    public static final RegistryKey<DamageType> AMETHYST_SHARD = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Blast.id("amethyst_shard"));
    public static final RegistryKey<DamageType> ICICLE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Blast.id("icicle"));
}

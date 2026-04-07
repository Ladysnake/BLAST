package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class BlastDamageTypes {
    public static final ResourceKey<DamageType> AMETHYST_SHARD = ResourceKey.create(Registries.DAMAGE_TYPE, Blast.id("amethyst_shard"));
    public static final ResourceKey<DamageType> ICICLE = ResourceKey.create(Registries.DAMAGE_TYPE, Blast.id("icicle"));

    public static void bootstrap(BootstrapContext<DamageType> registry) {
        registry.register(AMETHYST_SHARD, new DamageType("blast.amethyst_shard", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        registry.register(ICICLE, new DamageType("blast.icicle", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F, DamageEffects.FREEZING));
    }
}

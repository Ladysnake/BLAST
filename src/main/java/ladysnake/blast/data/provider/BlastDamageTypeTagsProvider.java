package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class BlastDamageTypeTagsProvider extends FabricTagsProvider<DamageType> {
    public BlastDamageTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(DamageTypeTags.BYPASSES_ARMOR)
            .addOptional(BlastDamageTypes.ICICLE);
        builder(DamageTypeTags.IS_FREEZING)
            .addOptional(BlastDamageTypes.ICICLE);
        builder(DamageTypeTags.IS_PROJECTILE)
            .addOptional(BlastDamageTypes.AMETHYST_SHARD)
            .addOptional(BlastDamageTypes.ICICLE);
    }
}

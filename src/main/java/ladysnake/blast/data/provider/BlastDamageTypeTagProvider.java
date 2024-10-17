/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package ladysnake.blast.data.provider;

import ladysnake.blast.common.init.BlastDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class BlastDamageTypeTagProvider extends FabricTagProvider<DamageType> {
    public BlastDamageTypeTagProvider(FabricDataOutput output) {
        super(output, RegistryKeys.DAMAGE_TYPE, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
            .addOptional(BlastDamageTypes.ICICLE);
        getOrCreateTagBuilder(DamageTypeTags.IS_FREEZING)
            .addOptional(BlastDamageTypes.ICICLE);
        getOrCreateTagBuilder(DamageTypeTags.IS_PROJECTILE)
            .addOptional(BlastDamageTypes.AMETHYST_SHARD)
            .addOptional(BlastDamageTypes.ICICLE);
    }
}

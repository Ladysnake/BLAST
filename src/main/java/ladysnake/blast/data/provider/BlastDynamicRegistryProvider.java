/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.data.provider;

import ladysnake.blast.common.Blast;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class BlastDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public BlastDynamicRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
    }

    @Override
    public String getName() {
        return Blast.MODID + "_dynamic";
    }
}

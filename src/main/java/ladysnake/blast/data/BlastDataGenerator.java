package ladysnake.blast.data;

import ladysnake.blast.common.init.BlastDamageTypes;
import ladysnake.blast.data.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class BlastDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(BlastBlockLootSubProvider::new);
        pack.addProvider(BlastBlockTagsProvider::new);
        pack.addProvider(BlastDamageTypeTagsProvider::new);
        pack.addProvider(BlastDynamicRegistryProvider::new);
        pack.addProvider(BlastItemTagsProvider::new);
        pack.addProvider(BlastModelProvider::new);
        pack.addProvider(BlastRecipeProvider::new);
        pack.addProvider(BlastSoundsProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.DAMAGE_TYPE, BlastDamageTypes::bootstrap);
    }
}

package ladysnake.blast.data;

import ladysnake.blast.data.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BlastDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(BlastBlockLootTableProvider::new);
        pack.addProvider(BlastBlockTagProvider::new);
        pack.addProvider(BlastDamageTypeTagProvider::new);
        pack.addProvider(BlastItemTagProvider::new);
        pack.addProvider(BlastModelGenerator::new);
        pack.addProvider(BlastRecipeProvider::new);
    }
}

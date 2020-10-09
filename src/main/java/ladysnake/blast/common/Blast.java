package ladysnake.blast.common;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import net.fabricmc.api.ModInitializer;

public class Blast implements ModInitializer {
    public static final String MODID = "blast";

    @Override
    public void onInitialize() {
        BlastEntities.init();
        BlastItems.init();
        BlastBlocks.init();
    }
}



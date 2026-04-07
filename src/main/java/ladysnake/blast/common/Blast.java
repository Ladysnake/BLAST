package ladysnake.blast.common;

import ladysnake.blast.common.init.*;
import moriyashiine.strawberrylib.api.SLib;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class Blast implements ModInitializer {
    public static final String MODID = "blast";

    @Override
    public void onInitialize() {
        SLib.init(MODID);
        BlastEntityTypes.init();
        BlastBlocks.init();
        BlastItems.init();
        BlastComponentTypes.init();
        BlastRecipeSerializers.init();
        BlastSoundEvents.initialize();
    }

    public static Identifier id(String value) {
        return Identifier.fromNamespaceAndPath(MODID, value);
    }
}



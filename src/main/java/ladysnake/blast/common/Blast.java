package ladysnake.blast.common;

import ladysnake.blast.client.network.EntityDispatcher;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.network.Packets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

public class Blast implements ModInitializer {
    public static final String MOD_ID = "blast";

    @Override
    public void onInitialize() {
        BlastEntities.init();
        BlastItems.init();
    }
}


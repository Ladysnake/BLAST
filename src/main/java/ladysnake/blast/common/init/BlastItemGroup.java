package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedHashSet;
import java.util.Set;

public class BlastItemGroup {
    public static final RegistryKey<ItemGroup> BLAST_ITEM_GROUP_KEY = RegistryKey.of(
            RegistryKeys.ITEM_GROUP, new Identifier(Blast.MODID, "blast")
    );

    private static final Set<Item> ITEM_QUEUE = new LinkedHashSet<>();

    static void queueItem(Item item) {
        ITEM_QUEUE.add(item);
    }

    public static void register() {
        ItemGroup exampleItemGroup = FabricItemGroup.builder()
                .icon(() -> new ItemStack(BlastItems.BOMB))
                .displayName(Text.translatable("itemGroup."))
                .build();

        Registry.register(Registries.ITEM_GROUP, BLAST_ITEM_GROUP_KEY, exampleItemGroup);

        ItemGroupEvents.modifyEntriesEvent(BLAST_ITEM_GROUP_KEY).register(
                (entries -> ITEM_QUEUE.forEach(entries::add))
        );
    }

}

package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.item.BombItem;
import ladysnake.blast.common.item.PipeBombItem;
import ladysnake.blast.common.item.TriggerBombItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BlastItems {
    public static Item BOMB;
    public static Item TRIGGER_BOMB;
    public static Item GOLDEN_BOMB;
    public static Item GOLDEN_TRIGGER_BOMB;
    public static Item DIAMOND_BOMB;
    public static Item DIAMOND_TRIGGER_BOMB;
    public static Item NAVAL_MINE;
    public static Item CONFETTI_BOMB;
    public static Item CONFETTI_TRIGGER_BOMB;
    public static Item DIRT_BOMB;
    public static Item DIRT_TRIGGER_BOMB;
    public static Item PEARL_BOMB;
    public static Item PEARL_TRIGGER_BOMB;
    public static Item SLIME_BOMB;
    public static Item SLIME_TRIGGER_BOMB;
    public static Item AMETHYST_BOMB;
    public static Item AMETHYST_TRIGGER_BOMB;
    public static Item FROST_BOMB;
    public static Item FROST_TRIGGER_BOMB;
    public static Item PIPE_BOMB;

    public static void init() {
        BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.BOMB), "bomb", ItemGroups.TOOLS);
        TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.TRIGGER_BOMB), "trigger_bomb", ItemGroups.TOOLS);
        GOLDEN_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.GOLDEN_BOMB), "golden_bomb", ItemGroups.TOOLS);
        GOLDEN_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.GOLDEN_TRIGGER_BOMB), "golden_trigger_bomb", ItemGroups.TOOLS);
        DIAMOND_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.DIAMOND_BOMB), "diamond_bomb", ItemGroups.TOOLS);
        DIAMOND_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.DIAMOND_TRIGGER_BOMB), "diamond_trigger_bomb", ItemGroups.TOOLS);
        NAVAL_MINE = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.NAVAL_MINE), "naval_mine", ItemGroups.TOOLS);
        CONFETTI_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.CONFETTI_BOMB), "confetti_bomb", ItemGroups.TOOLS);
        CONFETTI_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.CONFETTI_TRIGGER_BOMB), "confetti_trigger_bomb", ItemGroups.TOOLS);
        DIRT_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.DIRT_BOMB), "dirt_bomb", ItemGroups.TOOLS);
        DIRT_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.DIRT_TRIGGER_BOMB), "dirt_trigger_bomb", ItemGroups.TOOLS);
        PEARL_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.PEARL_BOMB), "pearl_bomb", ItemGroups.TOOLS);
        PEARL_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.PEARL_TRIGGER_BOMB), "pearl_trigger_bomb", ItemGroups.TOOLS);
        SLIME_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.SLIME_BOMB), "slime_bomb", ItemGroups.TOOLS);
        SLIME_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.SLIME_TRIGGER_BOMB), "slime_trigger_bomb", ItemGroups.TOOLS);
        AMETHYST_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.AMETHYST_BOMB), "amethyst_bomb", ItemGroups.TOOLS);
        AMETHYST_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.AMETHYST_TRIGGER_BOMB), "amethyst_trigger_bomb", ItemGroups.COMBAT);
        FROST_BOMB = registerItem(new BombItem(new Item.Settings().maxCount(16), BlastEntities.FROST_BOMB), "frost_bomb", ItemGroups.COMBAT);
        FROST_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().maxCount(16), BlastEntities.FROST_TRIGGER_BOMB), "frost_trigger_bomb", ItemGroups.COMBAT);
        PIPE_BOMB = registerItem(new PipeBombItem(new Item.Settings().maxCount(16)), "pipe_bomb", ItemGroups.COMBAT);
    }

    public static Item registerItem(Item item, String name, RegistryKey<ItemGroup> itemGroupKey) {
        Registry.register(Registries.ITEM, Blast.MODID + ":" + name, item);
        ItemGroupEvents.modifyEntriesEvent(itemGroupKey).register((entries) -> entries.add(item));

        return item;
    }

}

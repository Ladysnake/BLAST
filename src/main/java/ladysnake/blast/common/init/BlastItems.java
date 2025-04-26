package ladysnake.blast.common.init;

import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.item.BombItem;
import ladysnake.blast.common.item.PipeBombItem;
import ladysnake.blast.common.item.TriggerBombItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;

import java.util.function.Function;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerItem;

public class BlastItems {
    public static Item BOMB = registerBomb("bomb", BlastEntities.BOMB, ItemGroups.TOOLS);
    public static Item TRIGGER_BOMB = registerTriggerBomb("trigger_bomb", BlastEntities.TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item GOLDEN_BOMB = registerBomb("golden_bomb", BlastEntities.GOLDEN_BOMB, ItemGroups.TOOLS);
    public static Item GOLDEN_TRIGGER_BOMB = registerTriggerBomb("golden_trigger_bomb", BlastEntities.GOLDEN_TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item DIAMOND_BOMB = registerBomb("diamond_bomb", BlastEntities.DIAMOND_BOMB, ItemGroups.TOOLS);
    public static Item DIAMOND_TRIGGER_BOMB = registerTriggerBomb("diamond_trigger_bomb", BlastEntities.DIAMOND_TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item NAVAL_MINE = registerTriggerBomb("naval_mine", BlastEntities.NAVAL_MINE, ItemGroups.TOOLS);
    public static Item CONFETTI_BOMB = registerBomb("confetti_bomb", BlastEntities.CONFETTI_BOMB, ItemGroups.TOOLS);
    public static Item CONFETTI_TRIGGER_BOMB = registerTriggerBomb("confetti_trigger_bomb", BlastEntities.CONFETTI_TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item DIRT_BOMB = registerBomb("dirt_bomb", BlastEntities.DIRT_BOMB, ItemGroups.TOOLS);
    public static Item DIRT_TRIGGER_BOMB = registerTriggerBomb("dirt_trigger_bomb", BlastEntities.DIRT_TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item PEARL_BOMB = registerBomb("pearl_bomb", BlastEntities.PEARL_BOMB, ItemGroups.TOOLS);
    public static Item PEARL_TRIGGER_BOMB = registerTriggerBomb("pearl_trigger_bomb", BlastEntities.PEARL_TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item SLIME_BOMB = registerBomb("slime_bomb", BlastEntities.SLIME_BOMB, ItemGroups.TOOLS);
    public static Item SLIME_TRIGGER_BOMB = registerTriggerBomb("slime_trigger_bomb", BlastEntities.SLIME_TRIGGER_BOMB, ItemGroups.TOOLS);
    public static Item AMETHYST_BOMB = registerBomb("amethyst_bomb", BlastEntities.AMETHYST_BOMB, ItemGroups.COMBAT);
    public static Item AMETHYST_TRIGGER_BOMB = registerTriggerBomb("amethyst_trigger_bomb", BlastEntities.AMETHYST_TRIGGER_BOMB, ItemGroups.COMBAT);
    public static Item FROST_BOMB = registerBomb("frost_bomb", BlastEntities.FROST_BOMB, ItemGroups.COMBAT);
    public static Item FROST_TRIGGER_BOMB = registerTriggerBomb("frost_trigger_bomb", BlastEntities.FROST_TRIGGER_BOMB, ItemGroups.COMBAT);
    public static Item PIPE_BOMB = register("pipe_bomb", PipeBombItem::new, new Item.Settings().maxCount(16), ItemGroups.COMBAT);

    public static void init() {
    }

    public static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings, RegistryKey<ItemGroup> key) {
        Item item = registerItem(name, factory, settings);
        ItemGroupEvents.modifyEntriesEvent(key).register(entries -> entries.add(item));
        return item;
    }

    private static Item registerBomb(String name, EntityType<BombEntity> type, RegistryKey<ItemGroup> key) {
        return register(name, settings -> new BombItem(settings, type), new Item.Settings().maxCount(16), key);
    }

    private static Item registerTriggerBomb(String name, EntityType<BombEntity> type, RegistryKey<ItemGroup> key) {
        return register(name, settings -> new TriggerBombItem(settings, type), new Item.Settings().maxCount(16), key);
    }
}

/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import ladysnake.blast.common.world.item.BombItem;
import ladysnake.blast.common.world.item.PipeBombItem;
import ladysnake.blast.common.world.item.TriggerBombItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerItem;

public class BlastItems {
    public static final Item BOMB = registerBomb("bomb", BlastEntityTypes.BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item TRIGGER_BOMB = registerTriggerBomb("trigger_bomb", BlastEntityTypes.TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item GOLDEN_BOMB = registerBomb("golden_bomb", BlastEntityTypes.GOLDEN_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item GOLDEN_TRIGGER_BOMB = registerTriggerBomb("golden_trigger_bomb", BlastEntityTypes.GOLDEN_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIAMOND_BOMB = registerBomb("diamond_bomb", BlastEntityTypes.DIAMOND_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIAMOND_TRIGGER_BOMB = registerTriggerBomb("diamond_trigger_bomb", BlastEntityTypes.DIAMOND_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item NAVAL_MINE = registerTriggerBomb("naval_mine", BlastEntityTypes.NAVAL_MINE, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item CONFETTI_BOMB = registerBomb("confetti_bomb", BlastEntityTypes.CONFETTI_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item CONFETTI_TRIGGER_BOMB = registerTriggerBomb("confetti_trigger_bomb", BlastEntityTypes.CONFETTI_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIRT_BOMB = registerBomb("dirt_bomb", BlastEntityTypes.DIRT_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIRT_TRIGGER_BOMB = registerTriggerBomb("dirt_trigger_bomb", BlastEntityTypes.DIRT_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item PEARL_BOMB = registerBomb("pearl_bomb", BlastEntityTypes.PEARL_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item PEARL_TRIGGER_BOMB = registerTriggerBomb("pearl_trigger_bomb", BlastEntityTypes.PEARL_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item SLIME_BOMB = registerBomb("slime_bomb", BlastEntityTypes.SLIME_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item SLIME_TRIGGER_BOMB = registerTriggerBomb("slime_trigger_bomb", BlastEntityTypes.SLIME_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item AMETHYST_BOMB = registerBomb("amethyst_bomb", BlastEntityTypes.AMETHYST_BOMB, CreativeModeTabs.COMBAT);
    public static final Item AMETHYST_TRIGGER_BOMB = registerTriggerBomb("amethyst_trigger_bomb", BlastEntityTypes.AMETHYST_TRIGGER_BOMB, CreativeModeTabs.COMBAT);
    public static final Item FROST_BOMB = registerBomb("frost_bomb", BlastEntityTypes.FROST_BOMB, CreativeModeTabs.COMBAT);
    public static final Item FROST_TRIGGER_BOMB = registerTriggerBomb("frost_trigger_bomb", BlastEntityTypes.FROST_TRIGGER_BOMB, CreativeModeTabs.COMBAT);
    public static final Item PIPE_BOMB = register("pipe_bomb", PipeBombItem::new, new Item.Properties().stacksTo(16), CreativeModeTabs.COMBAT);

    public static void init() {
    }

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties, ResourceKey<CreativeModeTab> key) {
        Item item = registerItem(name, factory, properties);
        CreativeModeTabEvents.modifyOutputEvent(key).register(output -> output.accept(item));
        return item;
    }

    private static Item registerBomb(String name, EntityType<Bomb> type, ResourceKey<CreativeModeTab> key) {
        return register(name, properties -> new BombItem(properties, type), new Item.Properties().stacksTo(16), key);
    }

    private static Item registerTriggerBomb(String name, EntityType<Bomb> type, ResourceKey<CreativeModeTab> key) {
        return register(name, properties -> new TriggerBombItem(properties, type), new Item.Properties().stacksTo(16), key);
    }
}

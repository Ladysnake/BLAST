/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import ladysnake.blast.common.references.BlastItemIds;
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
    public static final Item BOMB = registerBomb(BlastItemIds.BOMB, BlastEntityTypes.BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.TRIGGER_BOMB, BlastEntityTypes.TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item GOLDEN_BOMB = registerBomb(BlastItemIds.GOLDEN_BOMB, BlastEntityTypes.GOLDEN_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item GOLDEN_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.GOLDEN_TRIGGER_BOMB, BlastEntityTypes.GOLDEN_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIAMOND_BOMB = registerBomb(BlastItemIds.DIAMOND_BOMB, BlastEntityTypes.DIAMOND_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIAMOND_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.DIAMOND_TRIGGER_BOMB, BlastEntityTypes.DIAMOND_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item NAVAL_MINE = registerTriggerBomb(BlastItemIds.NAVAL_MINE, BlastEntityTypes.NAVAL_MINE, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item CONFETTI_BOMB = registerBomb(BlastItemIds.CONFETTI_BOMB, BlastEntityTypes.CONFETTI_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item CONFETTI_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.CONFETTI_TRIGGER_BOMB, BlastEntityTypes.CONFETTI_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIRT_BOMB = registerBomb(BlastItemIds.DIRT_BOMB, BlastEntityTypes.DIRT_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item DIRT_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.DIRT_TRIGGER_BOMB, BlastEntityTypes.DIRT_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item PEARL_BOMB = registerBomb(BlastItemIds.PEARL_BOMB, BlastEntityTypes.PEARL_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item PEARL_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.PEARL_TRIGGER_BOMB, BlastEntityTypes.PEARL_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item SLIME_BOMB = registerBomb(BlastItemIds.SLIME_BOMB, BlastEntityTypes.SLIME_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item SLIME_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.SLIME_TRIGGER_BOMB, BlastEntityTypes.SLIME_TRIGGER_BOMB, CreativeModeTabs.TOOLS_AND_UTILITIES);
    public static final Item AMETHYST_BOMB = registerBomb(BlastItemIds.AMETHYST_BOMB, BlastEntityTypes.AMETHYST_BOMB, CreativeModeTabs.COMBAT);
    public static final Item AMETHYST_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.AMETHYST_TRIGGER_BOMB, BlastEntityTypes.AMETHYST_TRIGGER_BOMB, CreativeModeTabs.COMBAT);
    public static final Item FROST_BOMB = registerBomb(BlastItemIds.FROST_BOMB, BlastEntityTypes.FROST_BOMB, CreativeModeTabs.COMBAT);
    public static final Item FROST_TRIGGER_BOMB = registerTriggerBomb(BlastItemIds.FROST_TRIGGER_BOMB, BlastEntityTypes.FROST_TRIGGER_BOMB, CreativeModeTabs.COMBAT);
    public static final Item PIPE_BOMB = register(BlastItemIds.PIPE_BOMB, PipeBombItem::new, new Item.Properties().stacksTo(16), CreativeModeTabs.COMBAT);

    public static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties, ResourceKey<CreativeModeTab> tab) {
        Item item = registerItem(key, factory, properties);
        CreativeModeTabEvents.modifyOutputEvent(tab).register(output -> output.accept(item));
        return item;
    }

    private static Item registerBomb(ResourceKey<Item> key, EntityType<Bomb> type, ResourceKey<CreativeModeTab> tab) {
        return register(key, properties -> new BombItem(properties, type), new Item.Properties().stacksTo(16), tab);
    }

    private static Item registerTriggerBomb(ResourceKey<Item> key, EntityType<Bomb> type, ResourceKey<CreativeModeTab> tab) {
        return register(key, properties -> new TriggerBombItem(properties, type), new Item.Properties().stacksTo(16), tab);
    }

    public static void init() {
    }
}

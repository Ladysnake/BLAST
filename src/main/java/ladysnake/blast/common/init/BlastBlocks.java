/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import ladysnake.blast.common.world.level.block.*;
import moriyashiine.strawberrylib.api.module.SLibRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

public class BlastBlocks {
    public static final Block GUNPOWDER_BLOCK = registerBlock("gunpowder_block", GunpowderBlock::new, of().mapColor(DyeColor.BLACK).strength(0.5F, 0.5f).sound(SoundType.SAND), CreativeModeTabs.BUILDING_BLOCKS);
    public static final Block STRIPMINER = registerBlock("stripminer", properties -> new StripminerBlock(properties, BlastEntityTypes.STRIPMINER), of().strength(2.5f, 2.5f).sound(SoundType.WOOD).noOcclusion(), CreativeModeTabs.REDSTONE_BLOCKS);
    public static final Block COLD_DIGGER = registerBlock("cold_digger", properties -> new StripminerBlock(properties, BlastEntityTypes.COLD_DIGGER), of().strength(2.5f, 2.5f).sound(SoundType.WOOD).noOcclusion(), CreativeModeTabs.REDSTONE_BLOCKS);
    public static final Block BONESBURRIER = registerBlock("bonesburrier", BonesburrierBlock::new, ofFullCopy(Blocks.BONE_BLOCK), CreativeModeTabs.REDSTONE_BLOCKS);
    public static final Block REMOTE_DETONATOR = registerBlock("remote_detonator", RemoteDetonatorBlock::new, of().strength(2.5f, 2.5f).sound(SoundType.LANTERN).noOcclusion(), CreativeModeTabs.REDSTONE_BLOCKS);
    public static final Block DRY_ICE = registerBlock("dry_ice", DryIceBlock::new, of().mapColor(MapColor.COLOR_LIGHT_GRAY).friction(0.98F).strength(0.5F).sound(SoundType.GLASS).noOcclusion(), CreativeModeTabs.BUILDING_BLOCKS);
    public static final Block FOLLY_RED_PAINT = registerBlock("folly_red_paint", properties -> new FollyRedPaintBlock(properties, true), ofFullCopy(Blocks.HONEY_BLOCK).randomTicks().strength(0.2f).mapColor(MapColor.FIRE), CreativeModeTabs.BUILDING_BLOCKS);
    public static final Block FRESH_FOLLY_RED_PAINT = registerBlock("fresh_folly_red_paint", properties -> new FollyRedPaintBlock(properties, false), ofFullCopy(Blocks.HONEY_BLOCK).strength(0.2f).mapColor(MapColor.FIRE), CreativeModeTabs.BUILDING_BLOCKS);
    public static final Block DRIED_FOLLY_RED_PAINT = registerBlock("dried_folly_red_paint", properties -> new FollyRedPaintBlock(properties, true), of().mapColor(MapColor.FIRE).strength(0.4f).sound(SoundType.DRIPSTONE_BLOCK).mapColor(MapColor.FIRE), CreativeModeTabs.BUILDING_BLOCKS);

    public static void init() {
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties, ResourceKey<CreativeModeTab> key) {
        Block block = SLibRegistries.registerBlock(name, factory, properties);
        BlastItems.register(name, itemProperties -> new BlockItem(block, itemProperties), new Item.Properties().useBlockDescriptionPrefix(), key);
        return block;
    }
}

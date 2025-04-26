package ladysnake.blast.common.init;

import ladysnake.blast.common.block.*;
import moriyashiine.strawberrylib.api.module.SLibRegistries;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

import java.util.function.Function;

import static net.minecraft.block.AbstractBlock.Settings.copy;
import static net.minecraft.block.AbstractBlock.Settings.create;

public class BlastBlocks {
    public static Block GUNPOWDER_BLOCK = registerBlock("gunpowder_block", GunpowderBlock::new, create().mapColor(DyeColor.BLACK).strength(0.5F, 0.5f).sounds(BlockSoundGroup.SAND), ItemGroups.BUILDING_BLOCKS);
    public static Block STRIPMINER = registerBlock("stripminer", settings -> new StripminerBlock(settings, BlastEntities.STRIPMINER), create().strength(2.5f, 2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque(), ItemGroups.REDSTONE);
    public static Block COLD_DIGGER = registerBlock("cold_digger", settings -> new StripminerBlock(settings, BlastEntities.COLD_DIGGER), create().strength(2.5f, 2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque(), ItemGroups.REDSTONE);
    public static Block BONESBURRIER = registerBlock("bonesburrier", BonesburrierBlock::new, copy(Blocks.BONE_BLOCK), ItemGroups.REDSTONE);
    public static Block REMOTE_DETONATOR = registerBlock("remote_detonator", RemoteDetonatorBlock::new, create().strength(2.5f, 2.5f).sounds(BlockSoundGroup.LANTERN).nonOpaque(), ItemGroups.REDSTONE);
    public static Block DRY_ICE = registerBlock("dry_ice", DryIceBlock::new, create().mapColor(MapColor.LIGHT_GRAY).slipperiness(0.98F).strength(0.5F).sounds(BlockSoundGroup.GLASS).nonOpaque(), ItemGroups.BUILDING_BLOCKS);
    public static Block FOLLY_RED_PAINT = registerBlock("folly_red_paint", settings -> new FollyRedPaintBlock(settings, true), copy(Blocks.HONEY_BLOCK).ticksRandomly().strength(0.2f).mapColor(MapColor.BRIGHT_RED), ItemGroups.BUILDING_BLOCKS);
    public static Block FRESH_FOLLY_RED_PAINT = registerBlock("fresh_folly_red_paint", settings -> new FollyRedPaintBlock(settings, false), copy(Blocks.HONEY_BLOCK).strength(0.2f).mapColor(MapColor.BRIGHT_RED), ItemGroups.BUILDING_BLOCKS);
    public static Block DRIED_FOLLY_RED_PAINT = registerBlock("dried_folly_red_paint", settings -> new FollyRedPaintBlock(settings, true), create().mapColor(MapColor.BRIGHT_RED).strength(0.4f).sounds(BlockSoundGroup.DRIPSTONE_BLOCK).mapColor(MapColor.BRIGHT_RED), ItemGroups.BUILDING_BLOCKS);

    public static void init() {
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, RegistryKey<ItemGroup> key) {
        Block block = SLibRegistries.registerBlock(name, factory, settings);
        BlastItems.register(name, itemSettings -> new BlockItem(block, itemSettings), new Item.Settings().useBlockPrefixedTranslationKey(), key);
        return block;
    }
}

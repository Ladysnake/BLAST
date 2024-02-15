package ladysnake.blast.common.init;

import ladysnake.blast.common.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.QuiltLoader;

import static ladysnake.blast.common.Blast.MOD_ID;

public class BlastBlocks {

	public static Block GUNPOWDER_BLOCK;
	public static Block STRIPMINER;
	public static Block COLD_DIGGER;
	public static Block BONESBURRIER;
	public static Block SMILESWEEPER;
	public static Block REMOTE_DETONATOR;
	public static Block DRY_ICE;
	public static Block FOLLY_RED_PAINT;
	public static Block FRESH_FOLLY_RED_PAINT;
	public static Block DRIED_FOLLY_RED_PAINT;

	public static void init() {
		GUNPOWDER_BLOCK = registerBlock(new GunpowderBlock(FabricBlockSettings.of(Material.AGGREGATE, MapColor.BLACK).strength(0.5F, 0.5f).sounds(BlockSoundGroup.SAND)), "gunpowder_block", ItemGroup.BUILDING_BLOCKS);
		STRIPMINER = registerBlock(new StripminerBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f, 2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque(), BlastEntities.STRIPMINER), "stripminer", ItemGroup.REDSTONE);
		COLD_DIGGER = registerBlock(new StripminerBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f, 2.5f).sounds(BlockSoundGroup.WOOD).nonOpaque(), BlastEntities.COLD_DIGGER), "cold_digger", ItemGroup.REDSTONE);
		BONESBURRIER = registerBlock(new BonesburrierBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)), "bonesburrier", ItemGroup.REDSTONE);
		REMOTE_DETONATOR = registerBlock(new RemoteDetonatorBlock(FabricBlockSettings.of(Material.METAL).strength(2.5f, 2.5f).sounds(BlockSoundGroup.LANTERN).nonOpaque()), "remote_detonator", ItemGroup.REDSTONE);
		DRY_ICE = registerBlock(new DryIceBlock(FabricBlockSettings.of(Material.ICE).mapColor(MapColor.LIGHT_GRAY).slipperiness(0.98F).strength(0.5F).sounds(BlockSoundGroup.GLASS).nonOpaque()), "dry_ice", ItemGroup.BUILDING_BLOCKS);
		FOLLY_RED_PAINT = registerBlock(new FollyRedPaintBlock(FabricBlockSettings.copyOf(Blocks.HONEY_BLOCK).ticksRandomly().strength(0.2f).mapColor(MapColor.BRIGHT_RED)), "folly_red_paint", ItemGroup.BUILDING_BLOCKS);
		FRESH_FOLLY_RED_PAINT = registerBlock(new FollyRedPaintBlock(FabricBlockSettings.copyOf(Blocks.HONEY_BLOCK).strength(0.2f).mapColor(MapColor.BRIGHT_RED)), "fresh_folly_red_paint", ItemGroup.BUILDING_BLOCKS);
		DRIED_FOLLY_RED_PAINT = registerBlock(new FollyRedPaintBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC, MapColor.BRIGHT_RED).strength(0.4f).sounds(BlockSoundGroup.DRIPSTONE_BLOCK).mapColor(MapColor.BRIGHT_RED)), "dried_folly_red_paint", ItemGroup.BUILDING_BLOCKS);

		if (QuiltLoader.isModLoaded("befoul")) {
			SMILESWEEPER = registerBlock(new SmilesweeperBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK).nonOpaque()), "smilesweeper", ItemGroup.REDSTONE);
		}
	}

	private static Block registerBlock(Block block, String name, ItemGroup itemGroup) {
		Registry.register(Registry.BLOCK, MOD_ID + ":" + name, block);

		if (itemGroup != null) {
			BlockItem item = new BlockItem(block, new Item.Settings().group(itemGroup));
			item.appendBlocks(Item.BLOCK_ITEMS, item);
			BlastItems.registerItem(item, name);
		}

		return block;
	}

}

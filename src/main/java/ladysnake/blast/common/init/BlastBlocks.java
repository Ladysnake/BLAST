package ladysnake.blast.common.init;

import ladysnake.blast.common.block.GunpowderBlock;
import ladysnake.blast.common.block.StripminerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import static ladysnake.blast.common.Blast.MODID;

public class BlastBlocks {

    public static Block GUNPOWDER_BLOCK;
    public static Block STRIPMINER;

    public static void init() {
        GUNPOWDER_BLOCK = registerBlock(new GunpowderBlock(FabricBlockSettings.of(Material.AGGREGATE, MaterialColor.BLACK).strength(0.5F, 0.5f).sounds(BlockSoundGroup.SAND).breakByTool(FabricToolTags.SHOVELS)), "gunpowder_block", ItemGroup.BUILDING_BLOCKS);
        STRIPMINER = registerBlock(new StripminerBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f, 2.5f).sounds(BlockSoundGroup.WOOD).breakByTool(FabricToolTags.AXES).nonOpaque()), "stripminer", ItemGroup.REDSTONE);
    }

    private static Block registerBlock(Block block, String name, ItemGroup itemGroup) {
        Registry.register(Registry.BLOCK, MODID + ":" + name, block);

        if (itemGroup != null) {
            BlockItem item = new BlockItem(block, new Item.Settings().group(itemGroup));
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            BlastItems.registerItem(item, name);
        }

        return block;
    }

}

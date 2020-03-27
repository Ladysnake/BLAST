package ladysnake.blast.common.init;

import ladysnake.blast.common.block.GunpowderBlock;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

import static ladysnake.blast.common.Blast.MODID;

public class BlastBlocks {

    public static Block MEMBRANE;

    public static void init() {
        MEMBRANE = registerBlock(new GunpowderBlock(FabricBlockSettings.of(Material.SAND, MaterialColor.BLACK).strength(0.5F, 0.5f).sounds(BlockSoundGroup.SAND).build()), "gunpowder_block", ItemGroup.BUILDING_BLOCKS);

    }

    private static Block registerBlock(Block    block, String name, @Nullable ItemGroup itemGroup) {
        Registry.register(Registry.BLOCK, MODID + ":" + name, block);

        if (itemGroup != null) {
            BlockItem item = new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            BlastItems.registerItem(item, name);
        }

        return block;
    }

}

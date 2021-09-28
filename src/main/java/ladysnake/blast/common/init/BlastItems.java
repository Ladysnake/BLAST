package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.item.BombItem;
import ladysnake.blast.common.item.bombards.AmethystBombardItem;
import ladysnake.blast.common.item.bombards.BombardItem;
import ladysnake.blast.common.item.bombards.FrostBombardItem;
import ladysnake.blast.common.item.bombards.SlimeBombardItem;
import ladysnake.blast.common.item.TriggerBombItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BlastItems {

    public static Item BOMBARD;
    public static Item SLIME_BOMBARD;
    public static Item AMETHYST_BOMBARD;
    public static Item FROST_BOMBARD;

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

    public static void init() {
        BOMBARD = registerItem(new BombardItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(250)), "bombard");
        SLIME_BOMBARD = registerItem(new SlimeBombardItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(250)), "slime_bombard");
        AMETHYST_BOMBARD = registerItem(new AmethystBombardItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(250)), "amethyst_bombard");
        FROST_BOMBARD = registerItem(new FrostBombardItem(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(250)), "frost_bombard");

        BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.BOMB), "bomb");
        TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.TRIGGER_BOMB), "trigger_bomb");
        GOLDEN_BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.GOLDEN_BOMB), "golden_bomb");
        GOLDEN_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.GOLDEN_TRIGGER_BOMB), "golden_trigger_bomb");
        DIAMOND_BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.DIAMOND_BOMB), "diamond_bomb");
        DIAMOND_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.DIAMOND_TRIGGER_BOMB), "diamond_trigger_bomb");
        NAVAL_MINE = registerItem(new TriggerBombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.NAVAL_MINE), "naval_mine");
        CONFETTI_BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.CONFETTI_BOMB), "confetti_bomb");
        CONFETTI_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.CONFETTI_TRIGGER_BOMB), "confetti_trigger_bomb");
        DIRT_BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.DIRT_BOMB), "dirt_bomb");
        DIRT_TRIGGER_BOMB = registerItem(new TriggerBombItem(new Item.Settings().group(ItemGroup.TOOLS), BlastEntities.DIRT_TRIGGER_BOMB), "dirt_trigger_bomb");
    }

    public static Item registerItem(Item item, String name) {
        if (item instanceof BombItem) {
            registerItem(item, name, true);
        } else {
            registerItem(item, name, false);
        }

        return item;
    }

    public static Item registerItem(Item item, String name, boolean registerDispenserBehavior) {
        Registry.register(Registry.ITEM, Blast.MODID + ":" + name, item);
        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                    BombEntity bombEntity = ((BombItem) itemStack.getItem()).getType().create(world);
                    bombEntity.setPos(position.getX(), position.getY(), position.getZ());
                    itemStack.decrement(1);
                    return bombEntity;
                }
            });
        }

        return item;
    }

}
package ladysnake.blast.common.init;

import ladysnake.blast.common.entities.*;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

import static ladysnake.blast.common.Blast.MODID;

public class BlastEntities {

    public static EntityType<BombEntity> BOMB;
    public static EntityType<BombEntity> GOLDEN_BOMB;
    public static EntityType<BombEntity> DIAMOND_BOMB;
    public static EntityType<BombEntity> PULVERIS;
    public static EntityType<BombEntity> NAVAL_MINE;

    public static BlockEntityType<ExplosiveBarrelBlockEntity> EXPLOSIVE_BARREL;

    public static void init() {
        BOMB = Registry.register(Registry.ENTITY_TYPE, MODID + ":bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, BombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        GOLDEN_BOMB = Registry.register(Registry.ENTITY_TYPE, MODID + ":golden_bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, GoldenBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        DIAMOND_BOMB = Registry.register(Registry.ENTITY_TYPE, MODID + ":diamond_bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, DiamondBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        PULVERIS = Registry.register(Registry.ENTITY_TYPE, MODID + ":pulveris", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, PulverisEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        NAVAL_MINE = Registry.register(Registry.ENTITY_TYPE, MODID + ":naval_mine", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, NavalMineEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());

        EXPLOSIVE_BARREL = Registry.register(Registry.BLOCK_ENTITY_TYPE, MODID + ":explosive_barrel", BlockEntityType.Builder.create(ExplosiveBarrelBlockEntity::new, BlastBlocks.EXPLOSIVE_BARREL));
    }

}
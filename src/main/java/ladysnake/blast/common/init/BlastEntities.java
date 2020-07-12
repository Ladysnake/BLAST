package ladysnake.blast.common.init;

import ladysnake.blast.common.entities.*;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

import static ladysnake.blast.common.Blast.MODID;

public class BlastEntities {

    public static EntityType<BombEntity> BOMB;
    public static EntityType<BombEntity> GOLDEN_BOMB;
    public static EntityType<BombEntity> DIAMOND_BOMB;
    public static EntityType<BombEntity> PULVERIS;
    public static EntityType<BombEntity> NAVAL_MINE;

    public static BlockEntityType<ExplosiveBarrelBlockEntity> EXPLOSIVE_BARREL;

    public static void init() {
        BOMB = Registry.register(Registry.ENTITY_TYPE, MODID + ":bomb", FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, BombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        GOLDEN_BOMB = Registry.register(Registry.ENTITY_TYPE, MODID + ":golden_bomb", FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, GoldenBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        DIAMOND_BOMB = Registry.register(Registry.ENTITY_TYPE, MODID + ":diamond_bomb", FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, DiamondBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        PULVERIS = Registry.register(Registry.ENTITY_TYPE, MODID + ":pulveris", FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, PulverisEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        NAVAL_MINE = Registry.register(Registry.ENTITY_TYPE, MODID + ":naval_mine", FabricEntityTypeBuilder.<BombEntity>create(SpawnGroup.MISC, NavalMineEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());

//        EXPLOSIVE_BARREL = Registry.register(Registry.BLOCK_ENTITY_TYPE, MODID + ":explosive_barrel", BlockEntityType.Builder.create(ExplosiveBarrelBlockEntity::new, BlastBlocks.EXPLOSIVE_BARREL));
    }

}
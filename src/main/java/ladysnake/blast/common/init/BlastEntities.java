package ladysnake.blast.common.init;

import ladysnake.blast.common.entities.*;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class BlastEntities {

    public static EntityType<BombEntity> BOMB;
    public static EntityType<BombEntity> GOLDEN_BOMB;
    public static EntityType<BombEntity> DIAMOND_BOMB;
    public static EntityType<BombEntity> PULVERIS;
    public static EntityType<BombEntity> NAVAL_MINE;

    public static void init() {
        BOMB = Registry.register(Registry.ENTITY_TYPE, "blast:bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, BombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        GOLDEN_BOMB = Registry.register(Registry.ENTITY_TYPE, "blast:golden_bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, GoldenBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        DIAMOND_BOMB = Registry.register(Registry.ENTITY_TYPE, "blast:diamond_bomb", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, DiamondBombEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        PULVERIS = Registry.register(Registry.ENTITY_TYPE, "blast:pulveris", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, PulverisEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        NAVAL_MINE = Registry.register(Registry.ENTITY_TYPE, "blast:naval_mine", FabricEntityTypeBuilder.<BombEntity>create(EntityCategory.MISC, NavalMineEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
    }

}
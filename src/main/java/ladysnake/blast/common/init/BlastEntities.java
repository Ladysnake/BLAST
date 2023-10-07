package ladysnake.blast.common.init;

import ladysnake.blast.common.entity.*;
import ladysnake.blast.common.entity.projectiles.AmethystShardEntity;
import ladysnake.blast.common.entity.projectiles.IcicleEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

import static ladysnake.blast.common.Blast.MODID;

public class BlastEntities {

    public static EntityType<BombEntity> BOMB;
    public static EntityType<BombEntity> TRIGGER_BOMB;
    public static EntityType<BombEntity> GOLDEN_BOMB;
    public static EntityType<BombEntity> GOLDEN_TRIGGER_BOMB;
    public static EntityType<BombEntity> DIAMOND_BOMB;
    public static EntityType<BombEntity> DIAMOND_TRIGGER_BOMB;
    public static EntityType<BombEntity> NAVAL_MINE;
    public static EntityType<BombEntity> CONFETTI_BOMB;
    public static EntityType<BombEntity> CONFETTI_TRIGGER_BOMB;
    public static EntityType<BombEntity> DIRT_BOMB;
    public static EntityType<BombEntity> DIRT_TRIGGER_BOMB;
    public static EntityType<BombEntity> PEARL_BOMB;
    public static EntityType<BombEntity> PEARL_TRIGGER_BOMB;
    public static EntityType<BombEntity> SLIME_BOMB;
    public static EntityType<BombEntity> SLIME_TRIGGER_BOMB;
    public static EntityType<BombEntity> AMETHYST_BOMB;
    public static EntityType<BombEntity> AMETHYST_TRIGGER_BOMB;
    public static EntityType<BombEntity> FROST_BOMB;
    public static EntityType<BombEntity> FROST_TRIGGER_BOMB;
    public static EntityType<PipeBombEntity> PIPE_BOMB;

    public static EntityType<GunpowderBlockEntity> GUNPOWDER_BLOCK;
    public static EntityType<StripminerEntity> STRIPMINER;
    public static EntityType<ColdDiggerEntity> COLD_DIGGER;
    public static EntityType<BonesburrierEntity> BONESBURRIER;

    public static EntityType<AmethystShardEntity> AMETHYST_SHARD;
    public static EntityType<IcicleEntity> ICICLE;

    public static void init() {
        // throwable explosives
        BOMB = register("bomb", createBombEntityType(BombEntity::new));
        TRIGGER_BOMB = register("trigger_bomb", createBombEntityType(TriggerBombEntity::new));
        GOLDEN_BOMB = register("golden_bomb", createBombEntityType(GoldenBombEntity::new));
        GOLDEN_TRIGGER_BOMB = register("golden_trigger_bomb", createBombEntityType(GoldenTriggerBombEntity::new));
        DIAMOND_BOMB = register("diamond_bomb", createBombEntityType(DiamondBombEntity::new));
        DIAMOND_TRIGGER_BOMB = register("diamond_trigger_bomb", createBombEntityType(DiamondTriggerBombEntity::new));
        NAVAL_MINE = register("naval_mine", createBombEntityType(NavalMineEntity::new));
        CONFETTI_BOMB = register("confetti_bomb", createBombEntityType(ConfettiBombEntity::new));
        CONFETTI_TRIGGER_BOMB = register("confetti_trigger_bomb", createBombEntityType(ConfettiTriggerBombEntity::new));
        DIRT_BOMB = register("dirt_bomb", createBombEntityType(DirtBombEntity::new));
        DIRT_TRIGGER_BOMB = register("dirt_trigger_bomb", createBombEntityType(DirtTriggerBombEntity::new));
        PEARL_BOMB = register("pearl_bomb", createBombEntityType(PearlBombEntity::new));
        PEARL_TRIGGER_BOMB = register("pearl_trigger_bomb", createBombEntityType(PearlTriggerBombEntity::new));
        SLIME_BOMB = register("slime_bomb", createBombEntityType(SlimeBombEntity::new));
        SLIME_TRIGGER_BOMB = register("slime_trigger_bomb", createBombEntityType(SlimeTriggerBombEntity::new));
        AMETHYST_BOMB = register("amethyst_bomb", createBombEntityType(AmethystBombEntity::new));
        AMETHYST_TRIGGER_BOMB = register("amethyst_trigger_bomb", createBombEntityType(AmethystTriggerBombEntity::new));
        FROST_BOMB = register("frost_bomb", createBombEntityType(FrostBombEntity::new));
        FROST_TRIGGER_BOMB = register("frost_trigger_bomb", createBombEntityType(FrostTriggerBombEntity::new));
        PIPE_BOMB = register("pipe_bomb", createBombEntityType(PipeBombEntity::new));

        // explosive blocks
        GUNPOWDER_BLOCK = register("gunpowder_block", FabricEntityTypeBuilder.<GunpowderBlockEntity>create(SpawnGroup.MISC, GunpowderBlockEntity::new).dimensions(EntityDimensions.changing(1f, 1f)).forceTrackedVelocityUpdates(true).build());
        STRIPMINER = register("stripminer", FabricEntityTypeBuilder.create(SpawnGroup.MISC, StripminerEntity::new).dimensions(EntityDimensions.changing(1f, 1f)).forceTrackedVelocityUpdates(true).build());
        COLD_DIGGER = register("cold_digger", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ColdDiggerEntity::new).dimensions(EntityDimensions.changing(1f, 1f)).forceTrackedVelocityUpdates(true).build());
        BONESBURRIER = register("bonesburrier", FabricEntityTypeBuilder.create(SpawnGroup.MISC, BonesburrierEntity::new).dimensions(EntityDimensions.changing(1f, 1f)).forceTrackedVelocityUpdates(true).build());

        // projectiles
        AMETHYST_SHARD = register("amethyst_shard", FabricEntityTypeBuilder.create(SpawnGroup.MISC, AmethystShardEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());
        ICICLE = register("icicle", FabricEntityTypeBuilder.create(SpawnGroup.MISC, IcicleEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());
    }

    private static <T extends Entity> EntityType<T> register(String s, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, MODID + ":" + s, entityType);
    }

    private static <T extends Entity> EntityType<T> createBombEntityType(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.25f, 0.25f)).trackRangeBlocks(64).trackedUpdateRate(1).forceTrackedVelocityUpdates(true).build();
    }
}
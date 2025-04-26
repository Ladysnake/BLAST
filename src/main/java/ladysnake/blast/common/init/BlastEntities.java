package ladysnake.blast.common.init;

import ladysnake.blast.common.entity.*;
import ladysnake.blast.common.entity.projectiles.AmethystShardEntity;
import ladysnake.blast.common.entity.projectiles.IcicleEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEntityType;

public class BlastEntities {
    public static EntityType<BombEntity> BOMB = registerEntityType("bomb", createBombEntityType(BombEntity::new));
    public static EntityType<BombEntity> TRIGGER_BOMB = registerEntityType("trigger_bomb", createBombEntityType(TriggerBombEntity::new));
    public static EntityType<BombEntity> GOLDEN_BOMB = registerEntityType("golden_bomb", createBombEntityType(GoldenBombEntity::new));
    public static EntityType<BombEntity> GOLDEN_TRIGGER_BOMB = registerEntityType("golden_trigger_bomb", createBombEntityType(GoldenTriggerBombEntity::new));
    public static EntityType<BombEntity> DIAMOND_BOMB = registerEntityType("diamond_bomb", createBombEntityType(DiamondBombEntity::new));
    public static EntityType<BombEntity> DIAMOND_TRIGGER_BOMB = registerEntityType("diamond_trigger_bomb", createBombEntityType(DiamondTriggerBombEntity::new));
    public static EntityType<BombEntity> NAVAL_MINE = registerEntityType("naval_mine", createBombEntityType(NavalMineEntity::new));
    public static EntityType<BombEntity> CONFETTI_BOMB = registerEntityType("confetti_bomb", createBombEntityType(ConfettiBombEntity::new));
    public static EntityType<BombEntity> CONFETTI_TRIGGER_BOMB = registerEntityType("confetti_trigger_bomb", createBombEntityType(ConfettiTriggerBombEntity::new));
    public static EntityType<BombEntity> DIRT_BOMB = registerEntityType("dirt_bomb", createBombEntityType(DirtBombEntity::new));
    public static EntityType<BombEntity> DIRT_TRIGGER_BOMB = registerEntityType("dirt_trigger_bomb", createBombEntityType(DirtTriggerBombEntity::new));
    public static EntityType<BombEntity> PEARL_BOMB = registerEntityType("pearl_bomb", createBombEntityType(PearlBombEntity::new));
    public static EntityType<BombEntity> PEARL_TRIGGER_BOMB = registerEntityType("pearl_trigger_bomb", createBombEntityType(PearlTriggerBombEntity::new));
    public static EntityType<BombEntity> SLIME_BOMB = registerEntityType("slime_bomb", createBombEntityType(SlimeBombEntity::new));
    public static EntityType<BombEntity> SLIME_TRIGGER_BOMB = registerEntityType("slime_trigger_bomb", createBombEntityType(SlimeTriggerBombEntity::new));
    public static EntityType<BombEntity> AMETHYST_BOMB = registerEntityType("amethyst_bomb", createBombEntityType(AmethystBombEntity::new));
    public static EntityType<BombEntity> AMETHYST_TRIGGER_BOMB = registerEntityType("amethyst_trigger_bomb", createBombEntityType(AmethystTriggerBombEntity::new));
    public static EntityType<BombEntity> FROST_BOMB = registerEntityType("frost_bomb", createBombEntityType(FrostBombEntity::new));
    public static EntityType<BombEntity> FROST_TRIGGER_BOMB = registerEntityType("frost_trigger_bomb", createBombEntityType(FrostTriggerBombEntity::new));
    public static EntityType<PipeBombEntity> PIPE_BOMB = registerEntityType("pipe_bomb", createBombEntityType(PipeBombEntity::new));

    public static EntityType<GunpowderBlockEntity> GUNPOWDER_BLOCK = registerEntityType("gunpowder_block", EntityType.Builder.create(GunpowderBlockEntity::new, SpawnGroup.MISC).dimensions(1, 1).maxTrackingRange(10).trackingTickInterval(20));
    public static EntityType<StripminerEntity> STRIPMINER = registerEntityType("stripminer", EntityType.Builder.create(StripminerEntity::new, SpawnGroup.MISC).dimensions(1, 1).maxTrackingRange(10).trackingTickInterval(10));
    public static EntityType<ColdDiggerEntity> COLD_DIGGER = registerEntityType("cold_digger", EntityType.Builder.create(ColdDiggerEntity::new, SpawnGroup.MISC).dimensions(1, 1).maxTrackingRange(10).trackingTickInterval(10));
    public static EntityType<BonesburrierEntity> BONESBURRIER = registerEntityType("bonesburrier", EntityType.Builder.create(BonesburrierEntity::new, SpawnGroup.MISC).dimensions(1, 1).maxTrackingRange(10).trackingTickInterval(10));

    public static EntityType<AmethystShardEntity> AMETHYST_SHARD = registerEntityType("amethyst_shard", EntityType.Builder.create(AmethystShardEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f));
    public static EntityType<IcicleEntity> ICICLE = registerEntityType("icicle", EntityType.Builder.create(IcicleEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f));

    public static void init() {
        // custom falling block entity (Needed for claims protection)
        registerEntityType("shrapnel_block", EntityType.Builder.create(ShrapnelBlockEntity::new, SpawnGroup.MISC).dimensions(1, 1));
    }

    private static <T extends Entity> EntityType.Builder<T> createBombEntityType(EntityType.EntityFactory<T> factory) {
        return EntityType.Builder.create(factory, SpawnGroup.MISC).dimensions(0.25f, 0.25f);
    }
}

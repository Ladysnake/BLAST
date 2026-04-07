package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.world.entity.item.ColdDigger;
import ladysnake.blast.common.world.entity.item.Gunpowder;
import ladysnake.blast.common.world.entity.item.Stripminer;
import ladysnake.blast.common.world.entity.projectile.arrow.AmethystShard;
import ladysnake.blast.common.world.entity.projectile.arrow.Icicle;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEntityType;

public class BlastEntityTypes {
    public static final EntityType<Bomb> BOMB = registerEntityType("bomb", createBombEntityType(Bomb::new));
    public static final EntityType<Bomb> TRIGGER_BOMB = registerEntityType("trigger_bomb", createBombEntityType(TriggerBomb::new));
    public static final EntityType<Bomb> GOLDEN_BOMB = registerEntityType("golden_bomb", createBombEntityType(GoldenBomb::new));
    public static final EntityType<Bomb> GOLDEN_TRIGGER_BOMB = registerEntityType("golden_trigger_bomb", createBombEntityType(GoldenTriggerBomb::new));
    public static final EntityType<Bomb> DIAMOND_BOMB = registerEntityType("diamond_bomb", createBombEntityType(DiamondBomb::new));
    public static final EntityType<Bomb> DIAMOND_TRIGGER_BOMB = registerEntityType("diamond_trigger_bomb", createBombEntityType(DiamondTriggerBomb::new));
    public static final EntityType<Bomb> NAVAL_MINE = registerEntityType("naval_mine", createBombEntityType(NavalMine::new));
    public static final EntityType<Bomb> CONFETTI_BOMB = registerEntityType("confetti_bomb", createBombEntityType(ConfettiBomb::new));
    public static final EntityType<Bomb> CONFETTI_TRIGGER_BOMB = registerEntityType("confetti_trigger_bomb", createBombEntityType(ConfettiTriggerBomb::new));
    public static final EntityType<Bomb> DIRT_BOMB = registerEntityType("dirt_bomb", createBombEntityType(DirtBomb::new));
    public static final EntityType<Bomb> DIRT_TRIGGER_BOMB = registerEntityType("dirt_trigger_bomb", createBombEntityType(DirtTriggerBomb::new));
    public static final EntityType<Bomb> PEARL_BOMB = registerEntityType("pearl_bomb", createBombEntityType(PearlBomb::new));
    public static final EntityType<Bomb> PEARL_TRIGGER_BOMB = registerEntityType("pearl_trigger_bomb", createBombEntityType(PearlTriggerBomb::new));
    public static final EntityType<Bomb> SLIME_BOMB = registerEntityType("slime_bomb", createBombEntityType(SlimeBomb::new));
    public static final EntityType<Bomb> SLIME_TRIGGER_BOMB = registerEntityType("slime_trigger_bomb", createBombEntityType(SlimeTriggerBomb::new));
    public static final EntityType<Bomb> AMETHYST_BOMB = registerEntityType("amethyst_bomb", createBombEntityType(AmethystBomb::new));
    public static final EntityType<Bomb> AMETHYST_TRIGGER_BOMB = registerEntityType("amethyst_trigger_bomb", createBombEntityType(AmethystTriggerBomb::new));
    public static final EntityType<Bomb> FROST_BOMB = registerEntityType("frost_bomb", createBombEntityType(FrostBomb::new));
    public static final EntityType<Bomb> FROST_TRIGGER_BOMB = registerEntityType("frost_trigger_bomb", createBombEntityType(FrostTriggerBomb::new));
    public static final EntityType<PipeBomb> PIPE_BOMB = registerEntityType("pipe_bomb", createBombEntityType(PipeBomb::new));

    public static final EntityType<Gunpowder> GUNPOWDER_BLOCK = registerEntityType("gunpowder_block", EntityType.Builder.of(Gunpowder::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(20));
    public static final EntityType<Stripminer> STRIPMINER = registerEntityType("stripminer", EntityType.Builder.of(Stripminer::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(10));
    public static final EntityType<ColdDigger> COLD_DIGGER = registerEntityType("cold_digger", EntityType.Builder.of(ColdDigger::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(10));
    public static final EntityType<Bonesburrier> BONESBURRIER = registerEntityType("bonesburrier", EntityType.Builder.of(Bonesburrier::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(10));

    public static final EntityType<AmethystShard> AMETHYST_SHARD = registerEntityType("amethyst_shard", EntityType.Builder.of(AmethystShard::new, MobCategory.MISC).sized(0.5f, 0.5f));
    public static final EntityType<Icicle> ICICLE = registerEntityType("icicle", EntityType.Builder.of(Icicle::new, MobCategory.MISC).sized(0.5f, 0.5f));

    public static void init() {
        FabricEntityDataRegistry.register(Blast.id("facing"), Stripminer.FACING_TYPE);
    }

    private static <T extends Entity> EntityType.Builder<T> createBombEntityType(EntityType.EntityFactory<T> factory) {
        return EntityType.Builder.of(factory, MobCategory.MISC).sized(0.25f, 0.25f);
    }
}

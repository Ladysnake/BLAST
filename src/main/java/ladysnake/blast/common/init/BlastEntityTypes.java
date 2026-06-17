/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.references.BlastEntityTypeIds;
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
    public static final EntityType<Bomb> BOMB = registerEntityType(BlastEntityTypeIds.BOMB, createBombEntityType(Bomb::new));
    public static final EntityType<Bomb> TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.TRIGGER_BOMB, createBombEntityType(TriggerBomb::new));
    public static final EntityType<Bomb> GOLDEN_BOMB = registerEntityType(BlastEntityTypeIds.GOLDEN_BOMB, createBombEntityType(GoldenBomb::new));
    public static final EntityType<Bomb> GOLDEN_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.GOLDEN_TRIGGER_BOMB, createBombEntityType(GoldenTriggerBomb::new));
    public static final EntityType<Bomb> DIAMOND_BOMB = registerEntityType(BlastEntityTypeIds.DIAMOND_BOMB, createBombEntityType(DiamondBomb::new));
    public static final EntityType<Bomb> DIAMOND_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.DIAMOND_TRIGGER_BOMB, createBombEntityType(DiamondTriggerBomb::new));
    public static final EntityType<Bomb> NAVAL_MINE = registerEntityType(BlastEntityTypeIds.NAVAL_MINE, createBombEntityType(NavalMine::new));
    public static final EntityType<Bomb> CONFETTI_BOMB = registerEntityType(BlastEntityTypeIds.CONFETTI_BOMB, createBombEntityType(ConfettiBomb::new));
    public static final EntityType<Bomb> CONFETTI_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.CONFETTI_TRIGGER_BOMB, createBombEntityType(ConfettiTriggerBomb::new));
    public static final EntityType<Bomb> DIRT_BOMB = registerEntityType(BlastEntityTypeIds.DIRT_BOMB, createBombEntityType(DirtBomb::new));
    public static final EntityType<Bomb> DIRT_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.DIRT_TRIGGER_BOMB, createBombEntityType(DirtTriggerBomb::new));
    public static final EntityType<Bomb> PEARL_BOMB = registerEntityType(BlastEntityTypeIds.PEARL_BOMB, createBombEntityType(PearlBomb::new));
    public static final EntityType<Bomb> PEARL_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.PEARL_TRIGGER_BOMB, createBombEntityType(PearlTriggerBomb::new));
    public static final EntityType<Bomb> SLIME_BOMB = registerEntityType(BlastEntityTypeIds.SLIME_BOMB, createBombEntityType(SlimeBomb::new));
    public static final EntityType<Bomb> SLIME_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.SLIME_TRIGGER_BOMB, createBombEntityType(SlimeTriggerBomb::new));
    public static final EntityType<Bomb> AMETHYST_BOMB = registerEntityType(BlastEntityTypeIds.AMETHYST_BOMB, createBombEntityType(AmethystBomb::new));
    public static final EntityType<Bomb> AMETHYST_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.AMETHYST_TRIGGER_BOMB, createBombEntityType(AmethystTriggerBomb::new));
    public static final EntityType<Bomb> FROST_BOMB = registerEntityType(BlastEntityTypeIds.FROST_BOMB, createBombEntityType(FrostBomb::new));
    public static final EntityType<Bomb> FROST_TRIGGER_BOMB = registerEntityType(BlastEntityTypeIds.FROST_TRIGGER_BOMB, createBombEntityType(FrostTriggerBomb::new));
    public static final EntityType<PipeBomb> PIPE_BOMB = registerEntityType(BlastEntityTypeIds.PIPE_BOMB, createBombEntityType(PipeBomb::new));

    public static final EntityType<Gunpowder> GUNPOWDER_BLOCK = registerEntityType(BlastEntityTypeIds.GUNPOWDER_BLOCK, EntityType.Builder.of(Gunpowder::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(20));
    public static final EntityType<Stripminer> STRIPMINER = registerEntityType(BlastEntityTypeIds.STRIPMINER, EntityType.Builder.of(Stripminer::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(10));
    public static final EntityType<ColdDigger> COLD_DIGGER = registerEntityType(BlastEntityTypeIds.COLD_DIGGER, EntityType.Builder.of(ColdDigger::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(10));
    public static final EntityType<Bonesburrier> BONESBURRIER = registerEntityType(BlastEntityTypeIds.BONESBURRIER, EntityType.Builder.of(Bonesburrier::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(10).updateInterval(10));

    public static final EntityType<AmethystShard> AMETHYST_SHARD = registerEntityType(BlastEntityTypeIds.AMETHYST_SHARD, EntityType.Builder.of(AmethystShard::new, MobCategory.MISC).sized(0.5f, 0.5f));
    public static final EntityType<Icicle> ICICLE = registerEntityType(BlastEntityTypeIds.ICICLE, EntityType.Builder.of(Icicle::new, MobCategory.MISC).sized(0.5f, 0.5f));

    public static void init() {
        FabricEntityDataRegistry.register(Blast.id("facing"), Stripminer.FACING_TYPE);
    }

    private static <T extends Entity> EntityType.Builder<T> createBombEntityType(EntityType.EntityFactory<T> factory) {
        return EntityType.Builder.of(factory, MobCategory.MISC).sized(0.25f, 0.25f);
    }
}

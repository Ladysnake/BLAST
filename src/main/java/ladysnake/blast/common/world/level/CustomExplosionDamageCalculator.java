/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CustomExplosionDamageCalculator extends ExplosionDamageCalculator {
    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        if (entity instanceof ExperienceOrb || entity instanceof ItemEntity) {
            return false;
        }
        return super.shouldDamageEntity(explosion, entity);
    }

    @Nullable
    public Pair<BlockState, Boolean> getFillState() {
        return null;
    }

    public ItemStack getBreakTool(ServerLevel world) {
        return ItemStack.EMPTY;
    }

    public boolean customBlockDestruction(Explosion explosion, ServerLevel level, Vec3 sourcePos, List<BlockPos> positions) {
        return false;
    }

    public boolean createsFire() {
        return false;
    }

    public boolean createsPoof() {
        return true;
    }

    public boolean dropsAtSource() {
        return false;
    }

    public boolean pushesEntity(Entity entity) {
        return true;
    }

    public Optional<Float> getPower() {
        return Optional.empty();
    }

    public void affectEntity(Vec3 pos, Entity entity) {
    }
}

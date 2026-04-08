/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;

public class DiamondBomb extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new CustomExplosionDamageCalculator() {
        @Override
        public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (fluidState.isEmpty() && blockState.getDestroySpeed(level, pos) >= 0) {
                return Optional.of(0F);
            }
            return super.getBlockExplosionResistance(explosion, level, pos, blockState, fluidState);
        }
    };

    public DiamondBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIAMOND_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }
}

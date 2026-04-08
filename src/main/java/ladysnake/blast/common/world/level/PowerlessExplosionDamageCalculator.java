/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class PowerlessExplosionDamageCalculator extends CustomExplosionDamageCalculator {
    public static final CustomExplosionDamageCalculator INSTANCE = new PowerlessExplosionDamageCalculator();

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
        return false;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return false;
    }

    @Override
    public Optional<Float> getPower() {
        return Optional.of(0F);
    }
}

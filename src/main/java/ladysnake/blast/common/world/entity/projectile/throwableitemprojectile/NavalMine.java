package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class NavalMine extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new CustomExplosionDamageCalculator() {
        @Override
        public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (!fluidState.isEmpty()) {
                return Optional.of(0F);
            }
            return super.getBlockExplosionResistance(explosion, level, pos, blockState, fluidState);
        }
    };

    public NavalMine(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setExplosionPower(4);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.NAVAL_MINE;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }

    @Override
    protected BombTriggerType getTriggerType() {
        return BombTriggerType.IMPACT;
    }

    // play the click, although you can barely hear it, but you know, details
    @Override
    protected void onHit(HitResult hitResult) {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.NEUTRAL, 0.8F, 0.6F);
        super.onHit(hitResult);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }
}

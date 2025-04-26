package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Optional;

public class NavalMineEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new CustomExplosionBehavior() {
        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (!fluidState.isEmpty()) {
                return Optional.of(0F);
            }
            return super.getBlastResistance(explosion, world, pos, blockState, fluidState);
        }
    };

    public NavalMineEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionPower(4);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.NAVAL_MINE;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }

    @Override
    protected BombTriggerType getTriggerType() {
        return BombTriggerType.IMPACT;
    }

    // play the click, although you can barely hear it, but you know, details
    @Override
    protected void onCollision(HitResult hitResult) {
        getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.NEUTRAL, 0.8F, 0.6F);
        super.onCollision(hitResult);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

    @Override
    public boolean isTouchingWater() {
        return false;
    }
}

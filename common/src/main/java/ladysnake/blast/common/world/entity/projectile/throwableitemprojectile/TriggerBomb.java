package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class TriggerBomb extends Bomb {
    public TriggerBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.TRIGGER_BOMB;
    }

    @Override
    protected BombTriggerType getTriggerType() {
        return BombTriggerType.IMPACT;
    }

    // play the click, although you can barely hear it, but you know, details
    @Override
    protected void onHit(HitResult hitResult) {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.NEUTRAL, 1F, 0.6F);
        super.onHit(hitResult);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

}

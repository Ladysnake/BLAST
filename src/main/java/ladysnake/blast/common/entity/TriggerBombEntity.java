package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TriggerBombEntity extends BombEntity {
    public TriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public TriggerBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.TRIGGER_BOMB;
    }

    @Override
    public BombTriggerType getTriggerType() {
        return BombTriggerType.IMPACT;
    }

    // play the click, although you can barely hear it, but you know, details
    @Override
    protected void onCollision(HitResult hitResult) {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.NEUTRAL, 1F, 0.6F);
        super.onCollision(hitResult);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

}
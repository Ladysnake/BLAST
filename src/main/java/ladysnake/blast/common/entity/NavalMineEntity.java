package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class NavalMineEntity extends BombEntity {
    public NavalMineEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionRadius(4);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.NAVAL_MINE;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new CustomExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), getExplosionRadius(), CustomExplosion.BlockBreakEffect.AQUATIC, Explosion.DestructionType.DESTROY);
    }

    @Override
    public BombTriggerType getTriggerType() {
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

package ladysnake.blast.common.entities;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class StripminerEntity extends BombEntity {
    private static final TrackedData<Direction> FACING;
    private LivingEntity causingEntity;
    private Direction facing;

    public StripminerEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setFacing(Direction.UP);
        this.setFuse(80);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FACING, this.facing);
    }

    @Override
    public void explode() {
        for (int i = 0; i <= 24; i++) {
            BlockPos bp = this.getBlockPos().offset(this.getFacing(), i);
            if (world.getBlockState(bp).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(world, this, bp.getX()+0.5, bp.getY() +0.5, bp.getZ() + 0.5, 2.5f, null, Explosion.DestructionType.BREAK);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(true);
            } else {
                break;
            }
            world.playSound(null, bp.getX()+0.5, bp.getY() +0.5, bp.getZ() + 0.5, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1f, 0.025f);
        }
        this.remove();
    }

    public Direction getFacing() {
        return this.dataTracker.get(FACING);
    }

    public void setFacing(Direction facing) {
        this.dataTracker.set(FACING, facing);
        this.facing = facing;
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.BOMB;
    }

    @Override
    public void tick() {
        super.tick();
        this.onGround = true;
    }

    static {
        FACING = DataTracker.registerData(StripminerEntity.class, TrackedDataHandlerRegistry.FACING);
    }
}

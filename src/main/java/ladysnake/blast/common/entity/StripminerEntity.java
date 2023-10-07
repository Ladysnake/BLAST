package ladysnake.blast.common.entity;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.block.StripminerBlock;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class StripminerEntity extends BombEntity {
    protected static final TrackedData<Direction> FACING = DataTracker.registerData(StripminerEntity.class, Blast.FACING);

    static {
        DataTracker.registerData(StripminerEntity.class, Blast.FACING);
    }

    protected BlockState cachedState;

    public StripminerEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(80);
        this.setExplosionRadius(2.5f);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FACING, Direction.UP);
    }

    @Override
    public void explode() {
        for (int i = 0; i <= 24; i++) {
            BlockPos bp = this.getBlockPos().offset(this.getFacing(), i);
            if (this.getWorld().getBlockState(bp).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(this.getWorld(), this, bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, this.getExplosionRadius(), null, Explosion.DestructionType.DESTROY);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(true);
            } else {
                break;
            }
            this.getWorld().playSound(null, bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1f, 0.025f);
        }
        this.remove(RemovalReason.DISCARDED);
    }

    public Direction getFacing() {
        return this.dataTracker.get(FACING);
    }

    public void setFacing(Direction facing) {
        this.dataTracker.set(FACING, facing);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        super.onTrackedDataSet(trackedData);

        if (FACING.equals(trackedData)) {
            this.cachedState = BlastBlocks.STRIPMINER.getDefaultState().with(StripminerBlock.FACING, this.getFacing());
        }
    }

    public BlockState getState() {
        if (this.cachedState == null) {
            this.cachedState = BlastBlocks.STRIPMINER.getDefaultState().with(StripminerBlock.FACING, this.getFacing());
        }
        return this.cachedState;
    }

    @Override
    protected Item getDefaultItem() {
        return this.getType() == BlastEntities.COLD_DIGGER ? BlastBlocks.COLD_DIGGER.asItem() : BlastBlocks.STRIPMINER.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        this.setOnGround(true);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }
}

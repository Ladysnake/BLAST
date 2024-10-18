package ladysnake.blast.common.entity;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.block.StripminerBlock;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastEntities;
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

    protected BlockState cachedState;

    public StripminerEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setFuse(80);
        setExplosionRadius(2.5f);
    }

    @Override
    public void explode() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(getBlockPos());
        for (int i = 0; i <= 24; i++) {
            if (getWorld().getBlockState(mutable).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(getWorld(), this, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, getExplosionRadius(), null, Explosion.DestructionType.DESTROY);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(getWorld().isClient);
            } else {
                break;
            }
            getWorld().playSound(null, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1f, 0.025f);
            mutable.move(getFacing());
        }
        remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FACING, Direction.UP);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        super.onTrackedDataSet(trackedData);
        if (FACING.equals(trackedData)) {
            cachedState = BlastBlocks.STRIPMINER.getDefaultState().with(StripminerBlock.FACING, getFacing());
        }
    }

    @Override
    protected Item getDefaultItem() {
        return getType() == BlastEntities.COLD_DIGGER ? BlastBlocks.COLD_DIGGER.asItem() : BlastBlocks.STRIPMINER.asItem();
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

    public Direction getFacing() {
        return dataTracker.get(FACING);
    }

    public void setFacing(Direction facing) {
        dataTracker.set(FACING, facing);
    }

    public BlockState getState() {
        if (cachedState == null) {
            cachedState = BlastBlocks.STRIPMINER.getDefaultState().with(StripminerBlock.FACING, getFacing());
        }
        return cachedState;
    }
}

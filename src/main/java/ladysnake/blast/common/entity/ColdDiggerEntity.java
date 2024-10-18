package ladysnake.blast.common.entity;

import ladysnake.blast.common.block.StripminerBlock;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ColdDiggerEntity extends StripminerEntity {
    public ColdDiggerEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionRadius(3.5f);
    }

    @Override
    public void explode() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(getBlockPos());
        for (int i = 0; i <= 24; i++) {
            if (getWorld().getBlockState(mutable).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(getWorld(), this, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, getExplosionRadius(), CustomExplosion.BlockBreakEffect.FROSTY, Explosion.DestructionType.DESTROY);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(getWorld().isClient);
            } else {
                break;
            }
            getWorld().playSound(null, mutable.getX(), mutable.getY(), mutable.getZ(), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1f, 1.5f);
            mutable.move(getFacing());
        }
        mutable.set(getBlockPos());
        for (int i = 0; i <= 24; i++) {
            if (getWorld().getBlockState(mutable).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(getWorld(), this, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, 1f, null, Explosion.DestructionType.DESTROY);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(getWorld().isClient);
            } else {
                break;
            }
            mutable.move(getFacing());
        }
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        super.onTrackedDataSet(trackedData);
        if (FACING.equals(trackedData)) {
            cachedState = BlastBlocks.COLD_DIGGER.getDefaultState().with(StripminerBlock.FACING, getFacing());
        }
    }

    @Override
    public BlockState getState() {
        if (cachedState == null) {
            cachedState = BlastBlocks.COLD_DIGGER.getDefaultState().with(StripminerBlock.FACING, getFacing());
        }
        return cachedState;
    }
}

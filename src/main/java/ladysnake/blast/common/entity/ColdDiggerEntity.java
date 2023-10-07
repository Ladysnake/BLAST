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
        this.setExplosionRadius(3.5f);
    }

    @Override
    public void explode() {
        for (int i = 0; i <= 24; i++) {
            BlockPos bp = this.getBlockPos().offset(this.getFacing(), i);
            if (world.getBlockState(bp).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(world, this, bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, this.getExplosionRadius(), CustomExplosion.BlockBreakEffect.FROSTY, Explosion.DestructionType.BREAK);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(true);
            } else {
                break;
            }
            world.playSound(null, bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1f, 1.5f);
        }

        for (int i = 0; i <= 24; i++) {
            BlockPos bp = this.getBlockPos().offset(this.getFacing(), i);
            if (world.getBlockState(bp).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(world, this, bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, 1f, null, Explosion.DestructionType.BREAK);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(true);
            } else {
                break;
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        super.onTrackedDataSet(trackedData);

        if (FACING.equals(trackedData)) {
            this.cachedState = BlastBlocks.COLD_DIGGER.getDefaultState().with(StripminerBlock.FACING, this.getFacing());
        }
    }

    public BlockState getState() {
        if (this.cachedState == null) {
            this.cachedState = BlastBlocks.COLD_DIGGER.getDefaultState().with(StripminerBlock.FACING, this.getFacing());
        }
        return this.cachedState;
    }

}
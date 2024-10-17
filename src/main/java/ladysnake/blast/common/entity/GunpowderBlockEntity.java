package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class GunpowderBlockEntity extends BombEntity {
    public GunpowderBlockEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(1);
        this.setExplosionRadius(4f);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.GUNPOWDER_BLOCK.asItem();
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new CustomExplosion(this.getWorld(), this, this.getX(), this.getY(), this.getZ(), this.getExplosionRadius(), CustomExplosion.BlockBreakEffect.FIERY, Explosion.DestructionType.DESTROY);
    }
}

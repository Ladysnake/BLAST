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
        setFuse(1);
        setExplosionRadius(4);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.GUNPOWDER_BLOCK.asItem();
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new CustomExplosion(getWorld(), this, getX(), getY(), getZ(), getExplosionRadius(), CustomExplosion.BlockBreakEffect.FIERY, Explosion.DestructionType.DESTROY);
    }
}

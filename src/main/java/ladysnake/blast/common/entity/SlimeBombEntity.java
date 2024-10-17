package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.KnockbackExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class SlimeBombEntity extends BombEntity {
    public SlimeBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.SLIME_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new KnockbackExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), getExplosionRadius());
    }
}

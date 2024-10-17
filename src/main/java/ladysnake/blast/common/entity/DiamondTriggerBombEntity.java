package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class DiamondTriggerBombEntity extends TriggerBombEntity {
    public DiamondTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIAMOND_TRIGGER_BOMB;
    }

    @Override
    public CustomExplosion getExplosion() {
        return new CustomExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), getExplosionRadius(), CustomExplosion.BlockBreakEffect.UNSTOPPABLE, Explosion.DestructionType.DESTROY);
    }
}

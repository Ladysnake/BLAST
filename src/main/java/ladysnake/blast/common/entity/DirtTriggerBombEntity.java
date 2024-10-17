package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.BlockFillingExplosion;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DirtTriggerBombEntity extends TriggerBombEntity {
    public DirtTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionRadius(2);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIRT_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new BlockFillingExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), getExplosionRadius(), Blocks.DIRT.getDefaultState());
    }
}

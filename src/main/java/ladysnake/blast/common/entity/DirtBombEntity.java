package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.BlockFillingExplosion;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DirtBombEntity extends BombEntity {
    public DirtBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setExplosionRadius(2f);
    }

    public DirtBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
        this.setExplosionRadius(2f);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIRT_BOMB;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new BlockFillingExplosion(this.getWorld(), this.getOwner(), this.getX(), this.getY(), this.getZ(), this.getExplosionRadius(), Blocks.DIRT.getDefaultState());
    }
}

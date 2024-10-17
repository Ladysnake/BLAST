package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.BonesburrierExplosion;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class BonesburrierEntity extends BombEntity {
    public BonesburrierEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setFuse(80);
        setExplosionRadius(8);
    }

    @Override
    public void explode() {
        CustomExplosion explosion = new BonesburrierExplosion(getWorld(), this, getX(), getBodyY(0.0625), getZ(), getExplosionRadius(), Explosion.DestructionType.DESTROY);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(true);
        getWorld().playSound(null, getX(), getBodyY(0.0625), getZ(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.BLOCKS, 5, 1);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.BONESBURRIER.asItem();
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }
}

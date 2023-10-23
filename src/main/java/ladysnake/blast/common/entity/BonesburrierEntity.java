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
        this.setFuse(80);
        this.setExplosionRadius(8f);
    }

    @Override
    public void explode() {
        CustomExplosion explosion = new BonesburrierExplosion(this.getWorld(), this,  this.getX(), this.getBodyY(0.0625), this.getZ(), this.getExplosionRadius(), Explosion.DestructionType.DESTROY);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(true);

        this.getWorld().playSound(null,  this.getX(), this.getBodyY(0.0625), this.getZ(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.BLOCKS, 5f, 1.0f);

        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.BONESBURRIER.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        this.setOnGround(true);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }
}

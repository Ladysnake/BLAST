package ladysnake.blast.common.entities;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.network.Packets;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class BombEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int fuseTimer;

    public BombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(40);
    }

    public BombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, livingEntity, world);
        this.setFuse(40);
    }

    protected Item getDefaultItem() {
        return BlastItems.BOMB;
    }

    protected float getDirectHitDamage() {
        return 4f;
    }

    protected CustomExplosion getExplosion() {
        return new CustomExplosion(this.world, this, this.getX(), this.getY(), this.getZ(), 3f, null, Explosion.DestructionType.BREAK);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)hitResult).getEntity();
            entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), this.getDirectHitDamage());
        }

        this.setVelocity(0, 0, 0);
        if (this.getTriggerType() == BombTriggerType.IMPACT) {
            this.explode();
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return Packets.newSpawnPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        // drop item if in water
        if (this.isSubmergedInWater() && this.disableInLiquid()) {
            this.world.spawnEntity(new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), new ItemStack(this.getDefaultItem())));
            this.remove();
        }

        // tick down the fuse, then blow up
        if (this.getTriggerType() == BombTriggerType.FUSE) {
            this.setFuse(this.getFuse()-1);
            if (this.getFuse() <= 0) {
                this.explode();
            }
        }
    }

    public void explode() {
        this.remove();
        CustomExplosion explosion = this.getExplosion();
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(true);
    }

    public boolean disableInLiquid() {
        return true;
    }

    public BombTriggerType getTriggerType() {
        return BombTriggerType.FUSE;
    }

    public enum BombTriggerType {
        FUSE,
        IMPACT
    }


    public void setFuse(int int_1) {
        this.dataTracker.set(FUSE, int_1);
        this.fuseTimer = int_1;
    }

    public void onTrackedDataSet(TrackedData<?> trackedData_1) {
        if (FUSE.equals(trackedData_1)) {
            this.fuseTimer = this.getFuse();
        }
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public int getFuseTimer() {
        return this.fuseTimer;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(FUSE, 40);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compoundTag_1) {
        compoundTag_1.putShort("Fuse", (short)this.getFuseTimer());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compoundTag_1) {
        this.setFuse(compoundTag_1.getShort("Fuse"));
    }

    @Override
    protected ItemStack getItem() {
        return new ItemStack(this.getDefaultItem());
    }
}

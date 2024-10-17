package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class BombEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private float explosionRadius = 3f;
    public int ticksUntilRemoval;
    private int fuseTimer;

    public BombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setFuse(40);
        setExplosionRadius(3);
        ticksUntilRemoval = -1;
    }

    public BombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, livingEntity, world);
        setFuse(40);
        setExplosionRadius(3);
        ticksUntilRemoval = -1;
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.BOMB;
    }

    protected CustomExplosion getExplosion() {
        return new CustomExplosion(getWorld(), getOwner(), getX(), getY(), getZ(), getExplosionRadius(), null, Explosion.DestructionType.DESTROY);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (age > 1) {
//            if (hitResult.getType() == HitResult.Type.ENTITY) {
//                Entity entity = ((EntityHitResult) hitResult).getEntity();
//                entity.damage(DamageSource.thrownProjectile(this, getOwner()), getDirectHitDamage());
//            }

            setVelocity(0, 0, 0);

            if (getTriggerType() == BombTriggerType.IMPACT) {
                explode();
            }
        }
    }


    @Override
    public void tick() {
        if (ticksUntilRemoval > 0) {
            ticksUntilRemoval--;
            if (ticksUntilRemoval <= 0) {
                remove(RemovalReason.DISCARDED);
            }
        } else {
            super.tick();

            if (getWorld().getBlockState(getBlockPos()).isFullCube(getWorld(), getBlockPos())) {
                setPosition(prevX, prevY, prevZ);
            }

            // drop item if in water
            if (isSubmergedInWater() && disableInLiquid()) {
                getWorld().spawnEntity(new ItemEntity(getWorld(), getX(), getY(), getZ(), new ItemStack(getDefaultItem())));
                remove(RemovalReason.DISCARDED);
            }

            // tick down the fuse, then blow up
            if (getTriggerType() == BombTriggerType.FUSE) {
                // smoke particle for lit fuse
                if (getWorld().isClient) {
                    getWorld().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.3, getZ(), 0, 0, 0);
                }

                // shorten the fuse
                setFuse(getFuse() - 1);
                if (getFuse() <= 0) {
                    explode();
                }
            }
        }
    }

    public void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;

            CustomExplosion explosion = getExplosion();
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);

            if (!getWorld().isClient()) {
                for (net.minecraft.entity.player.PlayerEntity playerEntity : getWorld().getPlayers()) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                    if (serverPlayerEntity.squaredDistanceTo(getX(), getY(), getZ()) < 4096.0D) {
                        serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(getX(), getY(), getZ(), explosion.getPower(), explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity), explosion.getDestructionType(), explosion.getParticle(), explosion.getEmitterParticle(), explosion.getSoundEvent()));
                    }
                }
            }
        }
    }

    public boolean disableInLiquid() {
        return true;
    }

    public BombTriggerType getTriggerType() {
        return BombTriggerType.FUSE;
    }

    public void onTrackedDataSet(TrackedData<?> trackedData_1) {
        if (FUSE.equals(trackedData_1)) {
            fuseTimer = getFuse();
        }
    }

    public int getFuse() {
        return dataTracker.get(FUSE);
    }

    public void setFuse(int fuse) {
        dataTracker.set(FUSE, fuse);
        fuseTimer = fuse;
    }

    public int getFuseTimer() {
        return fuseTimer;
    }

    public float getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(float explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(new ItemStack(item.getItem()));
        // todo explosion radius
//        if (item.hasNbt() && item.getOrCreateNbt().contains("ExplosionRadius")) {
//            setExplosionRadius(item.getOrCreateNbt().getFloat("ExplosionRadius"));
//        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE, 40);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putShort("Fuse", (short) getFuseTimer());
        nbt.putFloat("ExplosionRadius", getExplosionRadius());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        setFuse(nbt.getShort("Fuse"));
        setExplosionRadius(nbt.getFloat("ExplosionRadius"));
    }

    @Override
    public ItemStack getStack() {
        return getDefaultItem().getDefaultStack();
    }

    public enum BombTriggerType {
        FUSE,
        IMPACT
    }
}

package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BombEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private float explosionPower = 3f;
    public int ticksUntilRemoval;
    private int fuseTimer;

    public BombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setFuse(40);
        setExplosionPower(3);
        ticksUntilRemoval = -1;
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
                setPosition(lastX, lastY, lastZ);
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
                    getWorld().addParticleClient(ParticleTypes.SMOKE, getX(), getY() + 0.3, getZ(), 0, 0, 0);
                }
                // shorten the fuse
                setFuse(getFuse() - 1);
                if (getFuse() <= 0) {
                    explode();
                }
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.BOMB;
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(item.getItem().getDefaultStack());
        setFuse(item.getOrDefault(BlastComponentTypes.FUSE, getFuse()));
        setExplosionPower(item.getOrDefault(BlastComponentTypes.EXPLOSION_POWER, getExplosionPower()));
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (age > 1) {
            setVelocity(0, 0, 0);
            if (getTriggerType() == BombTriggerType.IMPACT) {
                explode();
            }
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        if (FUSE.equals(trackedData)) {
            fuseTimer = getFuse();
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE, 40);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt("Fuse", getFuseTimer());
        view.putFloat("ExplosionPower", getExplosionPower());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        setFuse(view.getInt("Fuse", 0));
        setExplosionPower(view.getFloat("ExplosionPower", 0));
    }

    protected void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            CustomExplosionBehavior behavior = getExplosionBehavior();
            createExplosion(behavior, getPos(), behavior.getPower().orElse(getExplosionPower()), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE.value());
        }
    }

    protected void createExplosion(CustomExplosionBehavior behavior, Vec3d pos, float power, ParticleEffect smallParticle, ParticleEffect largeParticle, SoundEvent soundEvent) {
        getWorld().createExplosion(
            getOwner(),
            null,
            behavior,
            pos.getX(), pos.getY(), pos.getZ(),
            power,
            behavior.createsFire(),
            World.ExplosionSourceType.TNT,
            smallParticle,
            largeParticle,
            Registries.SOUND_EVENT.getEntry(soundEvent));
    }

    protected CustomExplosionBehavior getExplosionBehavior() {
        return new CustomExplosionBehavior();
    }

    protected BombTriggerType getTriggerType() {
        return BombTriggerType.FUSE;
    }

    public boolean disableInLiquid() {
        return true;
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

    public float getExplosionPower() {
        return explosionPower;
    }

    public void setExplosionPower(float explosionRadius) {
        this.explosionPower = explosionRadius;
    }

    public enum BombTriggerType {
        FUSE,
        IMPACT
    }
}

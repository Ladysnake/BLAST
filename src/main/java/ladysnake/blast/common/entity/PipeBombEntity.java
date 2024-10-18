package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PipeBombEntity extends PersistentProjectileEntity implements FlyingItemEntity {
    public final int MAX_FUSE = 20;

    private static final TrackedData<Integer> FUSE = DataTracker.registerData(PipeBombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public double rotationXmod;
    public double rotationYmod;
    public double rotationZmod;
    public float rotationX;
    public float rotationY;
    public float rotationZ;
    public float ticksUntilExplosion = -1;
    public Vec3d prevVelocity;
    public final float bounciness = 0.3f;

    public PipeBombEntity(EntityType<PipeBombEntity> variant, World world) {
        super(variant, world);
        setFuse(MAX_FUSE);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE, 40);

        rotationX = getWorld().random.nextFloat() * 360f;
        rotationY = getWorld().random.nextFloat() * 360f;
        rotationZ = getWorld().random.nextFloat() * 360f;
        rotationXmod = getWorld().random.nextFloat() * 10f * (getWorld().random.nextBoolean() ? -1 : 1);
        rotationYmod = getWorld().random.nextFloat() * 10f * (getWorld().random.nextBoolean() ? -1 : 1);
        rotationZmod = getWorld().random.nextFloat() * 10f * (getWorld().random.nextBoolean() ? -1 : 1);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return BlastItems.PIPE_BOMB.getDefaultStack();
    }

    @Override
    public ItemStack getStack() {
        return getDefaultItemStack();
    }

    @Override
    public void tick() {
        if (age >= 18000) {
            discard();
        }
        prevVelocity = getVelocity();
        super.tick();
        if (ticksUntilExplosion >= 0) {
            if (ticksUntilExplosion++ >= 5) {
                getWorld().createExplosion(this, getX(), getY(), getZ(), 4.0F, World.ExplosionSourceType.NONE);
                discard();
            }
        }
        if (getFuse() % 5 == 0) {
            playSound(BlastSoundEvents.PIPE_BOMB_TICK, 1.0f, 1.0f + Math.abs((float) (getFuse() - MAX_FUSE) / MAX_FUSE));
        }
        // shorten the fuse
        setFuse(getFuse() - 1);
        if (getFuse() <= 0) {
            explode();
            discard();
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_COPPER_HIT;
    }

    @Override
    public double getDamage() {
        return 0;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (prevVelocity != null && getVelocity().length() > 0.3f) {
            float xMod = bounciness;
            float yMod = bounciness;
            float zMod = bounciness;
            switch (blockHitResult.getSide()) {
                case DOWN, UP -> yMod = -yMod;
                case NORTH, SOUTH -> xMod = -xMod;
                case WEST, EAST -> zMod = -zMod;
            }
            setVelocity(prevVelocity.getX() * xMod, prevVelocity.getY() * yMod, prevVelocity.getZ() * zMod);
            playSound(SoundEvents.BLOCK_COPPER_HIT, 1.0f, 1.5f);
        } else {
            super.onBlockHit(blockHitResult);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }

    public int getFuse() {
        return dataTracker.get(FUSE);
    }

    public void setFuse(int fuse) {
        dataTracker.set(FUSE, fuse);
    }

    private void explode() {
        if (!getWorld().isClient) {
            getWorld().createExplosion(getOwner(), getX(), getY(), getZ(), 2f, World.ExplosionSourceType.NONE);
            // TODO Explosion
        }
    }
}

package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

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
    public float bounciness = 0.3f;

    public PipeBombEntity(EntityType<PipeBombEntity> variant, World world) {
        super(variant, world);
    }

    public PipeBombEntity(World world, PlayerEntity player) {
        super(BlastEntities.PIPE_BOMB, player, world);
        this.setOwner(player);
        this.setFuse(MAX_FUSE);
    }

    public PipeBombEntity(World world, double x, double y, double z) {
        super(BlastEntities.PIPE_BOMB, x, y, z, world);
        this.setFuse(MAX_FUSE);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(FUSE, 40);

        this.rotationX = this.getWorld().random.nextFloat() * 360f;
        this.rotationY = this.getWorld().random.nextFloat() * 360f;
        this.rotationZ = this.getWorld().random.nextFloat() * 360f;
        this.rotationXmod = this.getWorld().random.nextFloat() * 10f * (this.getWorld().random.nextBoolean() ? -1 : 1);
        this.rotationYmod = this.getWorld().random.nextFloat() * 10f * (this.getWorld().random.nextBoolean() ? -1 : 1);
        this.rotationZmod = this.getWorld().random.nextFloat() * 10f * (this.getWorld().random.nextBoolean() ? -1 : 1);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        return super.writeNbt(nbt);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(BlastItems.PIPE_BOMB);
    }

    @Override
    public ItemStack getStack() {
        return this.asItemStack();
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (this.prevVelocity != null && this.getVelocity().length() > 0.3f) {
            float xMod = bounciness;
            float yMod = bounciness;
            float zMod = bounciness;

            switch (blockHitResult.getSide()) {
                case DOWN, UP -> yMod = -yMod;
                case NORTH, SOUTH -> xMod = -xMod;
                case WEST, EAST -> zMod = -zMod;
            }

            this.setVelocity(this.prevVelocity.getX() * xMod, this.prevVelocity.getY() * yMod, this.prevVelocity.getZ() * zMod);
            this.playSound(SoundEvents.BLOCK_COPPER_HIT, 1.0f, 1.5f);
        } else {
            super.onBlockHit(blockHitResult);
        }
    }

    @Override
    public void tick() {
        this.prevVelocity = this.getVelocity();

        super.tick();

        if (this.ticksUntilExplosion >= 0) {
            if (this.ticksUntilExplosion++ >= 5) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 4.0F, World.ExplosionSourceType.NONE);
                this.discard();
            }
        }

        if (this.getFuse() % 5 == 0) {
            this.playSound(BlastSoundEvents.PIPE_BOMB_TICK, 1.0f, 1.0f + Math.abs((float) (this.getFuse() - MAX_FUSE) / MAX_FUSE));
        }

        // shorten the fuse
        this.setFuse(this.getFuse() - 1);
        if (this.getFuse() <= 0) {
            this.explode();
            this.discard();
        }
    }

    public void explode() {
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2f, World.ExplosionSourceType.NONE);
            // TODO Explosion
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_COPPER_HIT;
    }

    @Override
    protected void age() {
        if (this.age >= 18000) {
            this.discard();
        }
    }

    @Override
    public double getDamage() {
        return 0;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

}

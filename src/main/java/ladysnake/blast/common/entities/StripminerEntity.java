package ladysnake.blast.common.entities;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.network.Packets;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class StripminerEntity extends TntEntity {
    public static final int INITIAL_FUSE = 80;
    private static final TrackedData<Integer> FUSE;
    private static final TrackedData<Direction> FACING;
    private LivingEntity causingEntity;
    private int fuseTimer;
    private Direction facing;

    public StripminerEntity(EntityType<? extends StripminerEntity> entityType, World world) {
        super(entityType, world);
        this.fuseTimer = INITIAL_FUSE;
        this.inanimate = true;
        this.setFacing(Direction.NORTH);
    }

    public StripminerEntity(World world, double x, double y, double z, LivingEntity igniter, Direction facing) {
        this(BlastEntities.STRIPMINER, world);
        this.updatePosition(x, y, z);
        this.setFuse(INITIAL_FUSE);
        this.setFacing(facing);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(FUSE, INITIAL_FUSE);
        this.dataTracker.startTracking(FACING, this.facing);
    }

    protected boolean canClimb() {
        return false;
    }

    public boolean collides() {
        return !this.removed;
    }

    public void tick() {
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98D));
        if (this.onGround) {
            this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
        }

        --this.fuseTimer;
        if (this.fuseTimer <= 0) {
            this.remove();
            if (!this.world.isClient) {
                this.explode();
            }
        } else {
            this.updateWaterState();
            if (this.world.isClient) {
                this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    private void explode() {
        // test for a blast resistant block behind the barrel
        int x = 0;
        int y = 0;
        int z = 0;
        switch (this.getFacing()) {
            case DOWN:
                y = 1;
                break;
            case UP:
                y = -1;
                break;
            case NORTH:
                z = 1;
                break;
            case SOUTH:
                z = -1;
                break;
            case WEST:
                x = 1;
                break;
            case EAST:
                x = -1;
                break;
        }

        for (int i = 0; i <= 24; i++) {
            BlockPos bp = new BlockPos(this.getPos().getX() + (-x) * (i), this.getPos().getY() + (-y) * (i), this.getPos().getZ() + (-z) * (i));
            if (world.getBlockState(bp).getBlock().getBlastResistance() < 1200) {
                CustomExplosion explosion = new CustomExplosion(world, null, bp.getX()+0.5, bp.getY() +0.5, bp.getZ() + 0.5, 2.5f, null, Explosion.DestructionType.BREAK);
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(true);
            } else {
                break;
            }
        }
    }

    protected void writeCustomDataToTag(CompoundTag tag) {
        tag.putShort("Fuse", (short)this.getFuseTimer());
    }

    protected void readCustomDataFromTag(CompoundTag tag) {
        this.setFuse(tag.getShort("Fuse"));
    }

    public LivingEntity getCausingEntity() {
        return this.causingEntity;
    }

    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15F;
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
        this.fuseTimer = fuse;
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public Direction getFacing() {
        return this.dataTracker.get(FACING);
    }

    public void setFacing(Direction facing) {
        this.dataTracker.set(FACING, facing);
        this.facing = facing;
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (FUSE.equals(data)) {
            this.fuseTimer = this.getFuse();
        }
        if (FACING.equals(data)) {
            this.facing = this.getFacing();
        }
    }

    public int getFuseTimer() {
        return this.fuseTimer;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return Packets.newSpawnPacket(this);
    }

    static {
        FUSE = DataTracker.registerData(StripminerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        FACING = DataTracker.registerData(StripminerEntity.class, TrackedDataHandlerRegistry.FACING);
    }
}

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class BombEntity extends ThrownItemEntity {
	private static final TrackedData<Integer> FUSE = DataTracker.registerData(BombEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public int ticksUntilRemoval;
	private int fuseTimer;

	public BombEntity(EntityType<? extends BombEntity> entityType, World world) {
		super(entityType, world);
		this.setFuse(40);
		this.ticksUntilRemoval = -1;
	}

	public BombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
		super(entityType, livingEntity, world);
		this.setFuse(40);
		this.ticksUntilRemoval = -1;
	}

	protected Item getDefaultItem() {
		return BlastItems.BOMB;
	}

	protected float getDirectHitDamage() {
		return 4f;
	}

	protected CustomExplosion getExplosion() {
		return new CustomExplosion(this.world, this.getOwner(), this.getX(), this.getY(), this.getZ(), 3f, null, Explosion.DestructionType.BREAK);
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		if (this.age > 1) {
//            if (hitResult.getType() == HitResult.Type.ENTITY) {
//                Entity entity = ((EntityHitResult) hitResult).getEntity();
//                entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), this.getDirectHitDamage());
//            }

			this.setVelocity(0, 0, 0);

			if (this.getTriggerType() == BombTriggerType.IMPACT) {
				this.explode();
			}
		}
	}


	@Override
	public void tick() {
		if (this.ticksUntilRemoval > 0) {
			ticksUntilRemoval--;
			if (ticksUntilRemoval <= 0) {
				this.remove(RemovalReason.DISCARDED);
			}
		} else {
			super.tick();

			if (this.world.getBlockState(this.getBlockPos()).isFullCube(this.world, this.getBlockPos())) {
				this.setPosition(this.prevX, this.prevY, this.prevZ);
			}

			// drop item if in water
			if (this.isSubmergedInWater() && this.disableInLiquid()) {
				this.world.spawnEntity(new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), new ItemStack(this.getDefaultItem())));
				this.remove(RemovalReason.DISCARDED);
			}

			// tick down the fuse, then blow up
			if (this.getTriggerType() == BombTriggerType.FUSE) {
				// smoke particle for lit fuse
				if (this.world.isClient) {
					this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.3, this.getZ(), 0, 0, 0);
				}

				// shorten the fuse
				this.setFuse(this.getFuse() - 1);
				if (this.getFuse() <= 0) {
					this.explode();
				}
			}
		}
	}

	public void explode() {
		if (this.ticksUntilRemoval == -1) {
			this.ticksUntilRemoval = 1;

			CustomExplosion explosion = this.getExplosion();
			explosion.collectBlocksAndDamageEntities();
			explosion.affectWorld(true);

			if (!this.world.isClient()) {
				for (net.minecraft.entity.player.PlayerEntity playerEntity : this.world.getPlayers()) {
					ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
					if (serverPlayerEntity.squaredDistanceTo(this.getX(), this.getY(), this.getZ()) < 4096.0D) {
						serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(this.getX(), this.getY(), this.getZ(), explosion.getPower(), explosion.getAffectedBlocks(), (Vec3d) explosion.getAffectedPlayers().get(serverPlayerEntity)));
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
			this.fuseTimer = this.getFuse();
		}
	}

	public int getFuse() {
		return this.dataTracker.get(FUSE);
	}

	public void setFuse(int int_1) {
		this.dataTracker.set(FUSE, int_1);
		this.fuseTimer = int_1;
	}

	public int getFuseTimer() {
		return this.fuseTimer;
	}

	protected void initDataTracker() {
		this.dataTracker.startTracking(FUSE, 40);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound NbtCompound_1) {
		NbtCompound_1.putShort("Fuse", (short) this.getFuseTimer());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound NbtCompound_1) {
		this.setFuse(NbtCompound_1.getShort("Fuse"));
	}

	@Override
	protected ItemStack getItem() {
		return new ItemStack(this.getDefaultItem());
	}


	public enum BombTriggerType {
		FUSE,
		IMPACT
	}
}

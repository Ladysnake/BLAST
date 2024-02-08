package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import ladysnake.blast.mixin.FireworkRocketEntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PipeBombEntity extends PersistentProjectileEntity implements FlyingItemEntity {
	private static final TrackedData<Integer> FUSE = DataTracker.registerData(PipeBombEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public final int MAX_FUSE = 20;
	public double rotationXmod;
	public double rotationYmod;
	public double rotationZmod;
	public float rotationX;
	public float rotationY;
	public float rotationZ;
	public float ticksUntilExplosion = -1;
	public Vec3d prevVelocity;
	public float bounciness = 0.3f;

	public List<ItemStack> fireworkItemStacks = new ArrayList<>();

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

	public static PipeBombEntity fromItemStack(World world, ItemStack itemStack, PlayerEntity player, PlayerEntity pipeBombOwner) {
		PipeBombEntity pipeBombEntity = new PipeBombEntity(world, player.getX(), player.getY(), player.getZ());
		pipeBombEntity.setPos(player.getX(), player.getY() + (double) player.getStandingEyeHeight() - 0.10000000149011612D, player.getZ());

		if (itemStack.getOrCreateNbt().contains("Fireworks", NbtElement.LIST_TYPE)) {
			NbtList fireworksNbtList = itemStack.getOrCreateNbt().getList("Fireworks", NbtElement.COMPOUND_TYPE);
			for (NbtElement fireworkNbt : fireworksNbtList) {
				ItemStack fireworkItemStack = ItemStack.fromNbt((NbtCompound) fireworkNbt);
				pipeBombEntity.addFireworkItemStack(fireworkItemStack);
			}
		}

		if (pipeBombOwner != null) {
			pipeBombEntity.setOwner(pipeBombOwner);
		}

		return pipeBombEntity;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();

		this.dataTracker.startTracking(FUSE, 40);

		this.rotationX = world.random.nextFloat() * 360f;
		this.rotationY = world.random.nextFloat() * 360f;
		this.rotationZ = world.random.nextFloat() * 360f;
		this.rotationXmod = world.random.nextFloat() * 10f * (world.random.nextBoolean() ? -1 : 1);
		this.rotationYmod = world.random.nextFloat() * 10f * (world.random.nextBoolean() ? -1 : 1);
		this.rotationZmod = world.random.nextFloat() * 10f * (world.random.nextBoolean() ? -1 : 1);
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

		if (nbt.contains("Fireworks", NbtElement.LIST_TYPE)) {
			NbtList fireworksNbtList = nbt.getList("Fireworks", NbtElement.COMPOUND_TYPE);
			for (NbtElement fireworkNbtElement : fireworksNbtList) {
				this.addFireworkItemStack(ItemStack.fromNbt((NbtCompound) fireworkNbtElement));
			}
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList fireworksNbtList = new NbtList();
		for (ItemStack fireworkItemStack : this.getFireworkItemStacks()) {
			NbtCompound itemCompound = new NbtCompound();
			fireworkItemStack.writeNbt(itemCompound);
			fireworksNbtList.add(itemCompound);
		}
		nbt.put("Fireworks", fireworksNbtList);

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

		if (this.getFuse() % 5 == 0 && this.getFuse() >= 0) {
			this.playSound(BlastSoundEvents.PIPE_BOMB_TICK, 3.0f, 1.0f + Math.abs((float) (this.getFuse() - MAX_FUSE) / MAX_FUSE));
		}

		// shorten the fuse
		this.setFuse(this.getFuse() - 1);
		if (this.getFuse() <= 0) {
			if (!this.world.isClient) {
				if (this.explode()) {
					this.discard();
				}
			}
		} else {
			super.tick();
		}

		if (this.getOwner() == null) {
			PlayerEntity player = world.getClosestPlayer(this, 5f);
			if (player != null) {
				this.setPos(player.getX(), player.getY(), player.getZ());
			}
		}
	}

	@Override
	public boolean shouldRender(double distance) {
		return super.shouldRender(distance) && !this.isInvisible();
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return super.shouldRender(cameraX, cameraY, cameraZ) && !this.isInvisible();
	}

	// return true when done exploding
	public boolean explode() {
//		world.createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2f, Explosion.DestructionType.NONE);
		if (random.nextInt(5) == 0 || !this.isInvisible()) {
			ItemStack itemStack = this.getFirstFireworkItemStack();

			float rad = 1.2f;
			float randX = (float) random.nextGaussian() * rad;
			float randY = random.nextFloat() * rad;
			float randZ = (float) random.nextGaussian() * rad;
			if (!this.isInvisible()) {
				this.setInvisible(true);
				randX = 0;
				randY = 0;
				randZ = 0;

				if (itemStack == null) {
					this.playSound(SoundEvents.BLOCK_CANDLE_EXTINGUISH, 3.0f, 1.0f);
					((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 50, 0.1, 0.1, 0.1, 0);
				}
			}

			if (itemStack != null) {
				ItemStack fireworkStack = itemStack.copy();
				for (int i = 0; i < fireworkStack.getCount(); i++) {
					FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, this.getX() + randX, this.getY() + randY, this.getZ() + randZ, itemStack);
					world.spawnEntity(fireworkRocketEntity);
					((FireworkRocketEntityAccessor) fireworkRocketEntity).callExplodeAndRemove();
					this.playSound(BlastSoundEvents.PIPE_BOMB_EXPLODE, 5.0f, (float) (1.0f + random.nextGaussian() / 10f));
				}

				return this.removeFirstFireworkItemStack();
			} else {
				return true;
			}
		}

		return false;
	}

	public void addFireworkItemStack(ItemStack fireworkItemStack) {
		if (fireworkItemStack.getItem() instanceof FireworkRocketItem) {
			this.fireworkItemStacks.add(fireworkItemStack);
		}
	}

	public ItemStack getFirstFireworkItemStack() {
		if (fireworkItemStacks == null || fireworkItemStacks.isEmpty()) {
			return null;
		}
		return this.fireworkItemStacks.get(0);
	}

	// removes first firework item stack and returns whether the list is now empty or not
	public boolean removeFirstFireworkItemStack() {
		fireworkItemStacks.remove(0);

		return fireworkItemStacks.isEmpty();
	}

	public List<ItemStack> getFireworkItemStacks() {
		return this.fireworkItemStacks;
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

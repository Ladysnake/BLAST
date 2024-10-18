package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PipeBombEntity extends PersistentProjectileEntity implements FlyingItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(PipeBombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int MAX_FUSE = 20;
    private static final float BOUNCINESS = 0.3f;
    private ItemStack stack = getDefaultItemStack();
    private final List<ItemStack> fireworks = new ArrayList<>();

    public PipeBombEntity(EntityType<PipeBombEntity> variant, World world) {
        super(variant, world);
        setFuse(MAX_FUSE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (!fireworks.isEmpty()) {
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(fireworks));
        }
        nbt.put("Stack", stack.encode(getRegistryManager(), new NbtCompound()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        fireworks.clear();
        stack = ItemStack.fromNbt(getRegistryManager(), nbt.getCompound("Stack")).orElse(getDefaultItemStack());
        if (stack.contains(DataComponentTypes.CHARGED_PROJECTILES)) {
            fireworks.addAll(stack.get(DataComponentTypes.CHARGED_PROJECTILES).getProjectiles());
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE, 40);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return BlastItems.PIPE_BOMB.getDefaultStack();
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void tick() {
        if (age >= 18000) {
            discard();
        }
        super.tick();
        if (getFuse() % 5 == 0) {
            playSound(BlastSoundEvents.PIPE_BOMB_TICK, 1, 1 + Math.abs((float) (getFuse() - MAX_FUSE) / MAX_FUSE));
        }
        // shorten the fuse
        setFuse(getFuse() - 1);
        if (getFuse() <= 0 && explode()) {
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
        if (getVelocity().length() > 0.3f) {
            float xMod = BOUNCINESS;
            float yMod = BOUNCINESS;
            float zMod = BOUNCINESS;
            switch (blockHitResult.getSide()) {
                case DOWN, UP -> yMod = -yMod;
                case NORTH, SOUTH -> xMod = -xMod;
                case WEST, EAST -> zMod = -zMod;
            }
            setVelocity(getVelocity().getX() * xMod, getVelocity().getY() * yMod, getVelocity().getZ() * zMod);
            playSound(getHitSound(), 1, 1.5f);
        } else {
            super.onBlockHit(blockHitResult);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }

    public void setItem(ItemStack item) {
        stack = item;
        if (item.contains(DataComponentTypes.CHARGED_PROJECTILES)) {
            fireworks.addAll(item.get(DataComponentTypes.CHARGED_PROJECTILES).getProjectiles());
        }
    }

    public int getFuse() {
        return dataTracker.get(FUSE);
    }

    public void setFuse(int fuse) {
        dataTracker.set(FUSE, fuse);
    }

    private boolean explode() {
        if (getWorld() instanceof ServerWorld world) {
            if (random.nextInt(5) == 0 || !isInvisible()) {
                ItemStack stack = null;
                if (!fireworks.isEmpty()) {
                    stack = fireworks.getFirst();
                }
                float rad = 1.2f;
                float randX = (float) random.nextGaussian() * rad;
                float randY = random.nextFloat() * rad;
                float randZ = (float) random.nextGaussian() * rad;
                if (!isInvisible()) {
                    setInvisible(true);
                    randX = 0;
                    randY = 0;
                    randZ = 0;
                    if (stack == null) {
                        playSound(SoundEvents.BLOCK_CANDLE_EXTINGUISH, 3, 1);
                        world.spawnParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 50, 0.1, 0.1, 0.1, 0);
                    }
                }
                if (stack != null) {
                    ItemStack firework = stack.copy();
                    for (int i = 0; i < firework.getCount(); i++) {
                        FireworkRocketEntity rocket = new FireworkRocketEntity(world, getX() + randX, getY() + randY, getZ() + randZ, stack);
                        world.spawnEntity(rocket);
                        rocket.explodeAndRemove();
                        playSound(BlastSoundEvents.PIPE_BOMB_EXPLODE, 5, (float) (1 + random.nextGaussian() / 10f));
                    }
                    fireworks.removeFirst();
                    return fireworks.isEmpty();
                }
                return true;
            }
        }
        return false;
    }
}

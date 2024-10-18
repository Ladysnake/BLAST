package ladysnake.blast.common.entity.projectiles;

import ladysnake.blast.common.init.BlastDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class AmethystShardEntity extends PersistentProjectileEntity {
    public int ticksUntilRemoval = -1;

    public AmethystShardEntity(EntityType<? extends AmethystShardEntity> entityType, World world) {
        super(entityType, world);
        this.setSound(this.getHitSound());
        this.setDamage(8);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.AIR.getDefaultStack();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK;
    }

    @Override
    public void tick() {
        super.tick();
        if (inGround) {
            if (ticksUntilRemoval == -1) {
                for (int i = 0; i < 8; ++i) {
                    getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(getBreakItemParticle(), 1)), getX() + random.nextGaussian() / 20f, getY() + random.nextGaussian() / 20f, getZ() + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f);
                }
                ticksUntilRemoval = 2;
            }
            if (ticksUntilRemoval > 0) {
                ticksUntilRemoval--;
                if (ticksUntilRemoval <= 0) {
                    remove(RemovalReason.DISCARDED);
                }
            }
        }
        if (age < 10) {
            for (LivingEntity livingEntity : getWorld().getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(1f), LivingEntity::isAlive)) {
                onEntityHit(new EntityHitResult(livingEntity));
                kill();
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Entity owner = getOwner();
        DamageSource damageSource = getDamageSource(owner);
        if (owner instanceof LivingEntity living) {
            living.onAttacking(entity);
        }
        if (entity.getType() != EntityType.ENDERMAN && entity.damage(damageSource, (float) getDamage())) {
            if (isOnFire()) {
                entity.setOnFireFor(5);
            }
            if (entity instanceof LivingEntity living) {
                if (getWorld() instanceof ServerWorld serverWorld) {
                    EnchantmentHelper.onTargetDamaged(serverWorld, living, damageSource);
                }
                onHit(living);
                if (living != owner && living instanceof PlayerEntity && owner instanceof ServerPlayerEntity serverPlayer && !isSilent()) {
                    serverPlayer.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
            }
        }
        discard();
        getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.NEUTRAL, 1.0f, 1.5f);
    }

    protected Item getBreakItemParticle() {
        return Items.AMETHYST_BLOCK;
    }

    protected DamageSource getDamageSource(Entity owner) {
        return BlastDamageTypes.amethystShard(this, owner != null ? owner : this);
    }
}

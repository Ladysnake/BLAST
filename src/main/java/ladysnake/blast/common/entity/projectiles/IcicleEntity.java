package ladysnake.blast.common.entity.projectiles;

import ladysnake.blast.common.init.BlastDamageSources;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class IcicleEntity extends AmethystShardEntity {

	public IcicleEntity(EntityType<? extends AmethystShardEntity> entityType, World world) {
		super(entityType, world);
		this.setDamage(0.01f);
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}

	public Item getBreakItemParticle() {
		return Items.ICE;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();

		Entity entity2 = this.getOwner();
		DamageSource damageSource2;
		if (entity2 == null) {
			damageSource2 = BlastDamageSources.icicle(this, this);
		} else {
			damageSource2 = BlastDamageSources.icicle(this, entity2);
			if (entity2 instanceof LivingEntity) {
				((LivingEntity) entity2).onAttacking(entity);
			}
		}

		boolean bl = entity.getType() == EntityType.ENDERMAN;
		int j = entity.getFireTicks();
		if (this.isOnFire() && !bl) {
			entity.setOnFireFor(5);
		}

		if (entity.damage(damageSource2, (float) this.getDamage())) {
			if (bl) {
				return;
			}

			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;

				if (!this.world.isClient && entity2 instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity, entity2);
					EnchantmentHelper.onTargetDamaged((LivingEntity) entity2, livingEntity);
				}

				this.onHit(livingEntity);
				if (entity2 != null && livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity) entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
				}

				livingEntity.timeUntilRegen = 0;
				livingEntity.setFrozenTicks(livingEntity.getFrozenTicks() + 100);
			}
		} else {
			entity.setFireTicks(j);
			this.setVelocity(this.getVelocity().multiply(-0.1D));
			this.setYaw(this.getYaw() + 180.0F);
			this.prevYaw += 180.0F;
			if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7D) {
				if (this.pickupType == PickupPermission.ALLOWED) {
					this.dropStack(this.asItemStack(), 0.1F);
				}

				this.discard();
			}
		}

		this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.NEUTRAL, 1.0f, 1.5f);
	}
}

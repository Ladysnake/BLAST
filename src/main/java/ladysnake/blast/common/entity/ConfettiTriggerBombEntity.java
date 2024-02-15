package ladysnake.blast.common.entity;

import ladysnake.blast.client.BlastClient;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.init.BlastParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ConfettiTriggerBombEntity extends TriggerBombEntity {
	public ConfettiTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
		super(entityType, world);
	}

	public ConfettiTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
		super(entityType, world, livingEntity);
	}

	@Override
	protected Item getDefaultItem() {
		return BlastItems.CONFETTI_TRIGGER_BOMB;
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		this.setVelocity(0, 0, 0);
		if (this.getTriggerType() == BombTriggerType.IMPACT) {
			this.explode();
		}
	}

	@Override
	public void explode() {
		if (this.world.isClient) {
			for (int i = 0; i < 15; i++) {
				world.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), random.nextGaussian() / 10f, Math.abs(random.nextGaussian() / 10f), random.nextGaussian() / 10f);
			}

			for (int i = 0; i < 500; i++) {
				world.addParticle(BlastParticles.CONFETTI, this.getX(), this.getY(), this.getZ(), random.nextGaussian() / 8f, Math.abs(random.nextGaussian() / 8f), random.nextGaussian() / 8f);
			}

			this.remove(RemovalReason.DISCARDED);
		}

		// TW: disgusting hack
		// since the server removes the bomb too early, we have to manually remove it after a delay
		// so we set the age to a normally unobtainable value (value) to mark it as "to be removed" for tick()
		if (this.age > 0) {
			world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 4.0f, (1.5F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
			this.age = -1000;
		}
	}

	@Override
	public void tick() {
		super.tick();

		this.age += 1;
		if (age < 0 && age > -995) {
			this.remove(RemovalReason.DISCARDED);
		}
	}
}

package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.EntityExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class FrostTriggerBombEntity extends TriggerBombEntity {
	public FrostTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world) {
		super(entityType, world);
	}

	public FrostTriggerBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
		super(entityType, world, livingEntity);
	}

	@Override
	protected Item getDefaultItem() {
		return BlastItems.FROST_TRIGGER_BOMB;
	}

	@Override
	protected CustomExplosion getExplosion() {
		return new EntityExplosion(this.world, this.getOwner(), this.getX(), this.getY(), this.getZ(), BlastEntities.ICICLE, 70, 1.4f);
	}

}

package ladysnake.blast.common.entity.projectiles;

import ladysnake.blast.common.init.BlastDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

    @Override
    protected Item getBreakItemParticle() {
        return Items.ICE;
    }

    @Override
    protected DamageSource getDamageSource(Entity owner) {
        return BlastDamageTypes.icicle(this, owner != null ? owner : this);
    }

    @Override
    protected void onHit(LivingEntity target) {
        target.timeUntilRegen = 0;
        target.setFrozenTicks(200);
    }
}

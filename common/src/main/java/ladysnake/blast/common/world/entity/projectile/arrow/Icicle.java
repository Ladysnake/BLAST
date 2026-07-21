package ladysnake.blast.common.world.entity.projectile.arrow;

import ladysnake.blast.common.init.BlastDamageTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Icicle extends AmethystShard {
    public Icicle(EntityType<? extends AmethystShard> type, Level level) {
        super(type, level);
        setBaseDamage(0.01f);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected Item getBreakItemParticle() {
        return Items.ICE;
    }

    @Override
    protected DamageSource getDamageSource(Entity owner) {
        return damageSources().source(BlastDamageTypes.ICICLE, this, owner != null ? owner : this);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        target.invulnerableTime = 0;
        target.setTicksFrozen(target.getTicksFrozen() + 20);
    }
}

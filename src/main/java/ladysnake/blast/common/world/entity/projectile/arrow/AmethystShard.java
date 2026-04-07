package ladysnake.blast.common.world.entity.projectile.arrow;

import ladysnake.blast.common.init.BlastDamageTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class AmethystShard extends AbstractArrow {
    public int ticksUntilRemoval = -1;

    public AmethystShard(EntityType<? extends AmethystShard> type, Level level) {
        super(type, level);
        setSoundEvent(getDefaultHitGroundSoundEvent());
        setBaseDamage(8);
        pickup = Pickup.DISALLOWED;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_CLUSTER_BREAK;
    }

    @Override
    public void tick() {
        super.tick();
        if (isInGround()) {
            if (ticksUntilRemoval == -1) {
                for (int i = 0; i < 8; ++i) {
                    level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, getBreakItemParticle()), getX() + random.nextGaussian() / 20f, getY() + random.nextGaussian() / 20f, getZ() + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f);
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
        if (tickCount < 10) {
            for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(1f), LivingEntity::isAlive)) {
                onHitEntity(new EntityHitResult(entity));
                discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Entity owner = getOwner();
        DamageSource source = getDamageSource(owner);
        if (owner instanceof LivingEntity living) {
            living.setLastHurtMob(entity);
        }
        if (entity.getType() != EntityType.ENDERMAN && level() instanceof ServerLevel level && entity.hurtServer(level, source, (float) baseDamage)) {
            if (isOnFire()) {
                entity.igniteForSeconds(5);
            }
            if (entity instanceof LivingEntity living) {
                doPostHurtEffects(living);
                EnchantmentHelper.doPostAttackEffects(level, living, source);
                if (living != owner && living instanceof Player && owner instanceof ServerPlayer serverPlayer && !isSilent()) {
                    serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, ClientboundGameEventPacket.DEMO_PARAM_INTRO));
                }
            }
        }
        discard();
        level().playSound(null, blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundSource.NEUTRAL, 1.0f, 1.5f);
    }

    protected Item getBreakItemParticle() {
        return Items.AMETHYST_BLOCK;
    }

    protected DamageSource getDamageSource(Entity owner) {
        return damageSources().source(BlastDamageTypes.AMETHYST_SHARD, this, owner != null ? owner : this);
    }
}

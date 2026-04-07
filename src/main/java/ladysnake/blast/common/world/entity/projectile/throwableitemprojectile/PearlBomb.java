package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.EnchantedExplosionDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PearlBomb extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new EnchantedExplosionDamageCalculator(Enchantments.SILK_TOUCH, 1) {
        private static final ConsumeEffect TELEPORT_EFFECT = new TeleportRandomlyConsumeEffect();

        @Override
        public boolean createsPoof() {
            return false;
        }

        @Override
        public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
            return false;
        }

        @Override
        public boolean dropsAtSource() {
            return true;
        }

        @Override
        public boolean pushesEntity(Entity entity) {
            return false;
        }

        @Override
        public void affectEntity(Vec3 pos, Entity entity) {
            if (entity instanceof LivingEntity living) {
                TELEPORT_EFFECT.apply(entity.level(), ItemStack.EMPTY, living);
            }
        }
    };

    public PearlBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.PEARL_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }

    @Override
    public void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            CustomExplosionDamageCalculator calculator = getExplosionCalculator();
            createExplosion(calculator, position(), calculator.getPower().orElse(getExplosionPower()), ParticleTypes.REVERSE_PORTAL, ParticleTypes.REVERSE_PORTAL, SoundEvents.ENDERMAN_TELEPORT);
            for (int i = 0; i < 100; i++) {
                level().addParticle(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(), random.nextGaussian() / 8f, random.nextGaussian() / 8f, random.nextGaussian() / 8f);
            }
        }
    }
}

package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.EnchantedExplosionBehavior;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class PearlBombEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new EnchantedExplosionBehavior(Enchantments.SILK_TOUCH, 1) {
        private static final ConsumeEffect TELEPORT_EFFECT = new TeleportRandomlyConsumeEffect();

        @Override
        public boolean shouldDamage(Explosion explosion, Entity entity) {
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
        public void affectEntity(Vec3d pos, Entity entity) {
            if (entity instanceof LivingEntity living) {
                TELEPORT_EFFECT.onConsume(entity.getWorld(), ItemStack.EMPTY, living);
            }
        }
    };

    public PearlBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.PEARL_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }

    @Override
    public void explode() {
        if (ticksUntilRemoval == -1) {
            ticksUntilRemoval = 1;
            CustomExplosionBehavior behavior = getExplosionBehavior();
            createExplosion(behavior, getPos(), behavior.getPower().orElse(getExplosionPower()), ParticleTypes.REVERSE_PORTAL, ParticleTypes.REVERSE_PORTAL, SoundEvents.ENTITY_ENDERMAN_TELEPORT);
            for (int i = 0; i < 100; i++) {
                getWorld().addParticleClient(ParticleTypes.REVERSE_PORTAL, getX(), getY(), getZ(), random.nextGaussian() / 8f, random.nextGaussian() / 8f, random.nextGaussian() / 8f);
            }
        }
    }
}

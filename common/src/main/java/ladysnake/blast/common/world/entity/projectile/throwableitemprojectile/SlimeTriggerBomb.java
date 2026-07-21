package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class SlimeTriggerBomb extends TriggerBomb {
    public SlimeTriggerBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.SLIME_TRIGGER_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return SlimeBomb.CALCULATOR;
    }

    @Override
    public void explode() {
        super.explode();
        for (int i = 0; i < 500; i++) {
            level().addParticle(ParticleTypes.SNEEZE, getX(), getY(), getZ(), random.nextGaussian() / 5, random.nextGaussian() / 5, random.nextGaussian() / 5);
        }
    }
}

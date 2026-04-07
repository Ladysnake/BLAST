package ladysnake.blast.common.world.entity.item;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class Gunpowder extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new CustomExplosionDamageCalculator() {
        @Override
        public boolean createsFire() {
            return true;
        }
    };

    public Gunpowder(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setFuse(1);
        setExplosionPower(4);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.GUNPOWDER_BLOCK.asItem();
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }
}

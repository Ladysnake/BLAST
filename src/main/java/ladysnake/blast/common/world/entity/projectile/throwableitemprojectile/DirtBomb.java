package ladysnake.blast.common.world.entity.projectile.throwableitemprojectile;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import ladysnake.blast.common.world.level.FillingExplosionDamageCalculator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DirtBomb extends Bomb {
    public static final CustomExplosionDamageCalculator CALCULATOR = new FillingExplosionDamageCalculator(Blocks.DIRT.defaultBlockState(), true);

    public DirtBomb(EntityType<? extends Bomb> type, Level level) {
        super(type, level);
        setExplosionPower(2);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIRT_BOMB;
    }

    @Override
    protected CustomExplosionDamageCalculator getExplosionCalculator() {
        return CALCULATOR;
    }
}

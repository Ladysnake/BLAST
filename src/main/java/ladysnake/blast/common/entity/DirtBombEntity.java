package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.FillingExplosionBehavior;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DirtBombEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new FillingExplosionBehavior(Blocks.DIRT.getDefaultState(), true);

    public DirtBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setExplosionPower(2);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.DIRT_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }
}

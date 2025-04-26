package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GunpowderBlockEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new CustomExplosionBehavior() {
        @Override
        public boolean createsFire() {
            return true;
        }
    };

    public GunpowderBlockEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        setFuse(1);
        setExplosionPower(4);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.GUNPOWDER_BLOCK.asItem();
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }
}

package ladysnake.blast.common.entity;

import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import ladysnake.blast.common.world.explosion.EnchantedExplosionBehavior;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GoldenBombEntity extends BombEntity {
    public static final CustomExplosionBehavior BEHAVIOR = new EnchantedExplosionBehavior(Enchantments.FORTUNE, 3);

    public GoldenBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastItems.GOLDEN_BOMB;
    }

    @Override
    protected CustomExplosionBehavior getExplosionBehavior() {
        return BEHAVIOR;
    }
}

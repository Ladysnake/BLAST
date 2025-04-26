package ladysnake.blast.common.world.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;

public class StripMinerExplosionBehavior extends CustomExplosionBehavior {
    public static final CustomExplosionBehavior INSTANCE = new StripMinerExplosionBehavior();

    @Override
    public boolean pushesEntity(Entity entity) {
        if (entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity) {
            return false;
        }
        return super.pushesEntity(entity);
    }
}

package ladysnake.blast.common.world;

import ladysnake.blast.common.util.ProtectionsProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashSet;
import java.util.Set;

public class CustomExplosion extends Explosion {
    public final BlockBreakEffect effect;
    public final Set<Entity> affectedEntities = new HashSet<>();

    public CustomExplosion(World world, Entity entity, double x, double y, double z, float power, BlockBreakEffect effect, DestructionType destructionType) {
        super(world, entity, x, y, z, power, false, destructionType);
        this.effect = effect;
    }

    public void collectEntities() {
        for (BlockPos pos : affectedBlocks) {
            affectedEntities.addAll(world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(1), LivingEntity::isAlive));
        }
    }

    public boolean shouldDamageEntities() {
        return true;
    }

    protected boolean canExplode(BlockPos blockPos) {
        return ProtectionsProvider.canExplodeBlock(blockPos, world, this, damageSource);
    }

    protected boolean canPlace(BlockPos blockPos) {
        return ProtectionsProvider.canPlaceBlock(blockPos, world, damageSource);
    }

    public enum BlockBreakEffect {
        FORTUNE,
        UNSTOPPABLE,
        AQUATIC,
        FIERY,
        FROSTY
    }
}

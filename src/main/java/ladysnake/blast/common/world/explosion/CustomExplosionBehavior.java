package ladysnake.blast.common.world.explosion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CustomExplosionBehavior extends ExplosionBehavior {
    @Override
    public boolean shouldDamage(Explosion explosion, Entity entity) {
        if (entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity) {
            return false;
        }
        return super.shouldDamage(explosion, entity);
    }

    @Nullable
    public Pair<BlockState, Boolean> getCustomFillState() {
        return null;
    }

    public ItemStack getBreakTool(ServerWorld world) {
        return ItemStack.EMPTY;
    }

    public boolean customBlockDestruction(Explosion explosion, ServerWorld world, Vec3d sourcePos, List<BlockPos> positions) {
        return false;
    }

    public boolean createsFire() {
        return false;
    }

    public boolean createsPoof() {
        return true;
    }

    public boolean dropsAtSource() {
        return false;
    }

    public boolean pushesEntity(Entity entity) {
        return true;
    }

    public Optional<Float> getPower() {
        return Optional.empty();
    }

    public void affectEntity(Vec3d pos, Entity entity) {
    }
}

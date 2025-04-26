package ladysnake.blast.common.world.explosion;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;

import java.util.Optional;

public class PowerlessExplosionBehavior extends CustomExplosionBehavior {
    public static final CustomExplosionBehavior INSTANCE = new PowerlessExplosionBehavior();

    @Override
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
        return false;
    }

    @Override
    public boolean shouldDamage(Explosion explosion, Entity entity) {
        return false;
    }

    @Override
    public Optional<Float> getPower() {
        return Optional.of(0F);
    }
}

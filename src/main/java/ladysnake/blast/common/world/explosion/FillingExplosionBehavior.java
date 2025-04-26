package ladysnake.blast.common.world.explosion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;

public class FillingExplosionBehavior extends CustomExplosionBehavior {
    private final Pair<BlockState, Boolean> fillState;

    public FillingExplosionBehavior(BlockState state, boolean onlyAir) {
        fillState = Pair.of(state, onlyAir);
    }

    @Override
    public Pair<BlockState, Boolean> getCustomFillState() {
        return fillState;
    }
}

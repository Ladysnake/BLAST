package ladysnake.blast.common.world.level;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.BlockState;

public class FillingExplosionDamageCalculator extends CustomExplosionDamageCalculator {
    private final Pair<BlockState, Boolean> fillState;

    public FillingExplosionDamageCalculator(BlockState state, boolean onlyAir) {
        fillState = Pair.of(state, onlyAir);
    }

    @Override
    public Pair<BlockState, Boolean> getFillState() {
        return fillState;
    }
}

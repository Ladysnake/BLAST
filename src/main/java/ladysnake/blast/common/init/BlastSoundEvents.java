package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlastSoundEvents {
    public static final SoundEvent PIPE_BOMB_TICK = new SoundEvent(new Identifier(Blast.MODID, "entity.pipe_bomb.tick"));

    public static void initialize() {
        Registry.register(Registry.SOUND_EVENT, PIPE_BOMB_TICK.getId(), PIPE_BOMB_TICK);
    }
}

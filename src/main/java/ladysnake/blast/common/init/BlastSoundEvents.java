package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class BlastSoundEvents {
    public static final SoundEvent PIPE_BOMB_TICK = SoundEvent.of(Blast.id("entity.pipe_bomb.tick"));
    public static final SoundEvent PIPE_BOMB_EXPLODE = SoundEvent.of(Blast.id("entity.pipe_bomb.explode"));

    public static void initialize() {
        Registry.register(Registries.SOUND_EVENT, PIPE_BOMB_TICK.getId(), PIPE_BOMB_TICK);
        Registry.register(Registries.SOUND_EVENT, PIPE_BOMB_EXPLODE.getId(), PIPE_BOMB_EXPLODE);
    }
}

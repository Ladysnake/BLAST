package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlastSoundEvents {
	public static final SoundEvent PIPE_BOMB_TICK = new SoundEvent(new Identifier(Blast.MOD_ID, "entity.pipe_bomb.tick"));
	public static final SoundEvent PIPE_BOMB_EXPLODE = new SoundEvent(new Identifier(Blast.MOD_ID, "entity.pipe_bomb.explode"));
	public static final SoundEvent SMILESWEEPER_EXPLODE = new SoundEvent(new Identifier(Blast.MOD_ID, "entity.smilesweeper.explode"));

	public static void initialize() {
		Registry.register(Registry.SOUND_EVENT, PIPE_BOMB_TICK.getId(), PIPE_BOMB_TICK);
		Registry.register(Registry.SOUND_EVENT, PIPE_BOMB_EXPLODE.getId(), PIPE_BOMB_EXPLODE);
		Registry.register(Registry.SOUND_EVENT, SMILESWEEPER_EXPLODE.getId(), SMILESWEEPER_EXPLODE);
	}
}

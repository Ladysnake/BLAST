package ladysnake.blast.data.provider;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.init.BlastSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder.RegistrationBuilder.ofFile;
import static net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder.of;

public class BlastSoundsProvider extends FabricSoundsProvider {
    public BlastSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, SoundExporter exporter) {
        exporter.add(BlastSoundEvents.PIPE_BOMB_TICK, of().subtitle("subtitles.blast.entity.pipe_bomb.tick")
            .sound(ofFile(Blast.id("pipe_bomb_tick"))));
        exporter.add(BlastSoundEvents.PIPE_BOMB_EXPLODE, of().subtitle("subtitles.blast.entity.pipe_bomb.explode")
            .sound(ofFile(Blast.id("pipe_bomb_explode"))));
    }

    @Override
    public String getName() {
        return Blast.MODID + "_sounds";
    }
}

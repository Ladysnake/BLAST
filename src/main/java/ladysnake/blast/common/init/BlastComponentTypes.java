package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

public class BlastComponentTypes {
    public static final ComponentType<Integer> FUSE = new ComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT).build();
    public static final ComponentType<Float> EXPLOSION_RADIUS = new ComponentType.Builder<Float>().codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Blast.id("fuse"), FUSE);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Blast.id("explosion_radius"), EXPLOSION_RADIUS);
    }
}

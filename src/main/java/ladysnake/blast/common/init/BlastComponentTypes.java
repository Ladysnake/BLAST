package ladysnake.blast.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerComponentType;

public class BlastComponentTypes {
    public static final ComponentType<Boolean> PRIMED = registerComponentType("primed", new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN));
    public static final ComponentType<Identifier> FAKE_ITEM_ID = registerComponentType("fake_item_id", new ComponentType.Builder<Identifier>().codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));
    public static final ComponentType<Integer> FUSE = registerComponentType("fuse", new ComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Float> EXPLOSION_POWER = registerComponentType("explosion_power", new ComponentType.Builder<Float>().codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT));

    public static void init() {
    }
}

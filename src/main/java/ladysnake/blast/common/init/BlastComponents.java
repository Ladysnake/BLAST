package ladysnake.blast.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentType;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.function.UnaryOperator;

import static ladysnake.blast.common.Blast.MODID;

public class BlastComponents {

    public static final DataComponentType<Boolean> ARMED = registerComponent("armed", (builder) -> {
        return builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL);
    });

    public static final DataComponentType<ItemStack> FAKE_BUNDLE_DISPLAY_STACK = registerComponent("fake_bundle_display_stack", (builder) -> {
        return builder.codec(ItemStack.CODEC).packetCodec(ItemStack.PACKET_CODEC).cache();
    });

    public static final DataComponentType<List<ItemStack>> FIREWORKS = registerComponent("fireworks", (builder) -> {
        return builder.codec(ItemStack.CODEC.listOf()).packetCodec(ItemStack.LIST_PACKET_CODEC).cache();
    });

    public static final DataComponentType<Float> EXPLOSION_RADIUS = registerComponent("explosion_radius", (builder) -> {
        return builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT).cache();
    });

    public static final DataComponentType<Integer> FUSE = registerComponent("fuse", (builder) -> {
        return builder.codec(Codecs.rangedInt(-32768, 32767)).packetCodec(PacketCodecs.INTEGER).cache();
    });

    private static <T> DataComponentType<T> registerComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, MODID + ":" + id, (builderOperator.apply(DataComponentType.builder())).build());
    }

    public static void init() {}
}

/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerComponentType;

public class BlastComponentTypes {
    public static final DataComponentType<Boolean> PRIMED = registerComponentType("primed", new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final DataComponentType<Identifier> FAKE_ITEM_ID = registerComponentType("fake_item_id", new DataComponentType.Builder<Identifier>().persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC));
    public static final DataComponentType<Integer> FUSE = registerComponentType("fuse", new DataComponentType.Builder<Integer>().persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Float> EXPLOSION_POWER = registerComponentType("explosion_power", new DataComponentType.Builder<Float>().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static void init() {
    }
}

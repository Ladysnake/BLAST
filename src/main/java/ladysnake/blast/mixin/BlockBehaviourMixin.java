/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @SuppressWarnings("unchecked")
    @WrapOperation(method = "onExplosionHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withParameter(Lnet/minecraft/util/context/ContextKey;Ljava/lang/Object;)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;", ordinal = 1))
    private <T> LootParams.Builder blast$customFillBlock(LootParams.Builder instance, ContextKey<T> param, T value, Operation<LootParams.Builder> original, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) Explosion explosion) {
        if (explosion instanceof ServerExplosion serverExplosion && serverExplosion.damageCalculator instanceof CustomExplosionDamageCalculator calculator) {
            value = (T) calculator.getBreakTool(level);
        }
        return original.call(instance, param, value);
    }
}

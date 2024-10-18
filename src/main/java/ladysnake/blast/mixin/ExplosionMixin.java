package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @WrapOperation(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/ExplosionBehavior;getBlastResistance(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Ljava/util/Optional;"))
    private Optional<Float> blast$overrideBlastResistance(ExplosionBehavior instance, Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, Operation<Optional<Float>> original) {
        if (isEffect(explosion, CustomExplosion.BlockBreakEffect.AQUATIC) || isEffect(explosion, CustomExplosion.BlockBreakEffect.FROSTY)) {
            if (!fluidState.isEmpty()) {
                return Optional.of(0F);
            }
        } else if (isEffect(explosion, CustomExplosion.BlockBreakEffect.UNSTOPPABLE) && fluidState.isEmpty() && blockState.getHardness(world, pos) >= 0) {
            return Optional.of(0F);
        }
        return original.call(instance, explosion, world, pos, blockState, fluidState);
    }

    @ModifyExpressionValue(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/ExplosionBehavior;shouldDamage(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/entity/Entity;)Z"))
    private boolean blast$customDamage(boolean original, @Local Entity entity) {
        if ((Object) this instanceof CustomExplosion customExplosion) {
            if (entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity) {
                return false;
            }
            return customExplosion.shouldDamageEntities();
        }
        return original;
    }

    @Unique
    private static boolean isEffect(Object explosion, CustomExplosion.BlockBreakEffect effect) {
        return explosion instanceof CustomExplosion customExplosion && customExplosion.effect == effect;
    }
}

package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.block.AbstractBlock;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @SuppressWarnings("unchecked")
    @WrapOperation(method = "onExploded", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/context/LootWorldContext$Builder;add(Lnet/minecraft/util/context/ContextParameter;Ljava/lang/Object;)Lnet/minecraft/loot/context/LootWorldContext$Builder;", ordinal = 1))
    private <T> LootWorldContext.Builder blast$customFillBlock(LootWorldContext.Builder instance, ContextParameter<T> parameter, T value, Operation<LootWorldContext.Builder> original, @Local(argsOnly = true) ServerWorld world, @Local(argsOnly = true) Explosion explosion) {
        if (explosion instanceof ExplosionImpl impl && impl.behavior instanceof CustomExplosionBehavior behavior) {
            value = (T) behavior.getBreakTool(world);
        }
        return original.call(instance, parameter, value);
    }
}

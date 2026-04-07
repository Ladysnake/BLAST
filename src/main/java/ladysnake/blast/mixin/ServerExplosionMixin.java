package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BiConsumer;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin implements Explosion {
    @Shadow
    @Final
    public ExplosionDamageCalculator damageCalculator;

    @Shadow
    @Final
    private Vec3 center;

    @Shadow
    @Final
    private ServerLevel level;

    @WrapWithCondition(method = "hurtEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;push(Lnet/minecraft/world/phys/Vec3;)V"))
    private boolean blast$pushesEntity(Entity instance, Vec3 impulse) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator) {
            return calculator.pushesEntity(instance);
        }
        return true;
    }

    @Inject(method = "hurtEntities", at = @At("HEAD"))
    private void blast$affectEntity(CallbackInfo ci) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator) {
            final int range = 5;
            for (Entity entity : level.getEntitiesOfClass(Entity.class, new AABB(center.x() - range, center.y() - range, center.z() - range, center.x() + range, center.y() + range, center.z() + range))) {
                calculator.affectEntity(center, entity);
            }
        }
    }

    @Inject(method = "interactWithBlocks", at = @At("HEAD"), cancellable = true)
    private void blast$customBlockDestruction(List<BlockPos> targetBlocks, CallbackInfo ci) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator && calculator.customBlockDestruction(this, level, center, targetBlocks)) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "interactWithBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;onExplosionHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;Ljava/util/function/BiConsumer;)V"))
    private void blast$fillBehavior(BlockState instance, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer biConsumer, Operation<Void> original) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator && calculator.getFillState() != null) {
            if (calculator.getFillState().getSecond() ? instance.canBeReplaced() : !instance.isAir()) {
                original.call(instance, level, pos, explosion, biConsumer);
                level.setBlockAndUpdate(pos, calculator.getFillState().getFirst());
            }
        } else {
            original.call(instance, level, pos, explosion, biConsumer);
        }
    }

    @WrapOperation(method = "interactWithBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"))
    private void blast$dropsAtSource(Level level, BlockPos pos, ItemStack itemStack, Operation<Void> original) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator && calculator.dropsAtSource()) {
            pos = BlockPos.containing(center);
        }
        original.call(level, pos, itemStack);
    }
}

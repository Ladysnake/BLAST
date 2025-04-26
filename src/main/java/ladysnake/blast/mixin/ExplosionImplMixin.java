package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ladysnake.blast.common.world.explosion.CustomExplosionBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BiConsumer;

@Mixin(ExplosionImpl.class)
public abstract class ExplosionImplMixin implements Explosion {
    @Shadow
    @Final
    public ExplosionBehavior behavior;

    @Shadow
    @Final
    private Vec3d pos;

    @Shadow
    @Final
    private ServerWorld world;

    @WrapWithCondition(method = "damageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private boolean blast$pushesEntity(Entity instance, Vec3d velocity) {
        if (behavior instanceof CustomExplosionBehavior customBehavior) {
            return customBehavior.pushesEntity(instance);
        }
        return true;
    }

    @Inject(method = "damageEntities", at = @At("HEAD"))
    private void blast$affectEntity(CallbackInfo ci) {
        if (behavior instanceof CustomExplosionBehavior customBehavior) {
            final int range = 5;
            for (Entity entity : world.getNonSpectatingEntities(Entity.class, new Box(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range))) {
                customBehavior.affectEntity(pos, entity);
            }
        }
    }

    @Inject(method = "destroyBlocks", at = @At("HEAD"), cancellable = true)
    private void blast$customBlockDestruction(List<BlockPos> positions, CallbackInfo ci) {
        if (behavior instanceof CustomExplosionBehavior customBehavior && customBehavior.customBlockDestruction(this, world, pos, positions)) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "destroyBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onExploded(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;Ljava/util/function/BiConsumer;)V"))
    private void blast$fillBehavior(BlockState instance, ServerWorld world, BlockPos blockPos, Explosion explosion, BiConsumer<ItemStack, BlockPos> biConsumer, Operation<Void> original) {
        if (behavior instanceof CustomExplosionBehavior customBehavior && customBehavior.getCustomFillState() != null) {
            if (customBehavior.getCustomFillState().getSecond() ? instance.isReplaceable() : !instance.isAir()) {
                original.call(instance, world, blockPos, explosion, biConsumer);
                world.setBlockState(blockPos, customBehavior.getCustomFillState().getFirst());
            }
        } else {
            original.call(instance, world, blockPos, explosion, biConsumer);
        }
    }

    @WrapOperation(method = "destroyBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V"))
    private void blast$dropsAtSource(World world, BlockPos pos, ItemStack stack, Operation<Void> original) {
        if (behavior instanceof CustomExplosionBehavior customBehavior && customBehavior.dropsAtSource()) {
            pos = BlockPos.ofFloored(this.pos);
        }
        original.call(world, pos, stack);
    }
}

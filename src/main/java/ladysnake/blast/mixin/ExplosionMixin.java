package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.util.ProtectionsProvider;
import ladysnake.blast.common.world.CustomExplosion;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Unique
    private static Explosion currentExplosion = null;

    @Shadow
    @Final
    public Random random;

    @Shadow
    @Final
    public World world;

    @Shadow
    @Final
    public DamageSource damageSource;

    @Shadow
    @Final
    public ObjectArrayList<BlockPos> affectedBlocks;

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
    private boolean blast$customDamage(boolean original) {
        if ((Object) this instanceof CustomExplosion customExplosion) {
            return customExplosion.shouldDamageEntities();
        }
        return original;
    }

    @Inject(method = "affectWorld", at = @At("HEAD"))
    private void blast$cacheExplosion(boolean particles, CallbackInfo ci) {
        currentExplosion = (Explosion) (Object) this;
        if ((Object) this instanceof CustomExplosion customExplosion) {
            customExplosion.collectEntities();
        }
    }

    // todo fix fortune not working for some reason even though the pickaxe does
    @ModifyArg(method = "method_24024", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/Explosion;tryMergeStack(Ljava/util/List;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/BlockPos;)V"))
    private static ItemStack blast$fortuneEffect(ItemStack stack) {
        if (isEffect(currentExplosion, CustomExplosion.BlockBreakEffect.FORTUNE)) {
            ItemStack pickaxe = Items.NETHERITE_PICKAXE.getDefaultStack();
            pickaxe.addEnchantment(currentExplosion.world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(Enchantments.FORTUNE), 3);
        }
        return stack;
    }

    @Inject(method = "affectWorld", at = @At("TAIL"))
    private void blast$frostEffect(boolean particles, CallbackInfo ci) {
        if (isEffect(this, CustomExplosion.BlockBreakEffect.FROSTY)) {
            for (BlockPos pos : affectedBlocks) {
                world.setBlockState(pos, BlastBlocks.DRY_ICE.getDefaultState());
            }
        }
    }

    @Inject(method = "affectWorld", at = @At("TAIL"))
    private void blast$fieryEffect(boolean particles, CallbackInfo ci) {
        if (!world.isClient && isEffect(this, CustomExplosion.BlockBreakEffect.FIERY)) {
            for (BlockPos pos : affectedBlocks) {
                if (ProtectionsProvider.canPlaceBlock(pos, world, damageSource) && random.nextInt(3) == 0 && world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOpaqueFullCube(world, pos.down())) {
                    world.setBlockState(pos, AbstractFireBlock.getState(world, pos));
                }
            }
        }
    }

    @Unique
    private static boolean isEffect(Object explosion, CustomExplosion.BlockBreakEffect effect) {
        return explosion instanceof CustomExplosion customExplosion && customExplosion.effect == effect;
    }
}

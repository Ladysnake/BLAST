package ladysnake.blast.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.serialization.Codec;
import ladysnake.blast.common.entity.projectiles.AmethystShardEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapWithCondition(method = "readCustomData", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setStack(Lnet/minecraft/item/ItemStack;)V"))
    private boolean blast$fixModProjectileSerializationRead(PersistentProjectileEntity instance, ItemStack stack) {
        return !isDisallowed();
    }

    @WrapWithCondition(method = "writeCustomData", at = @At(value = "INVOKE", target = "Lnet/minecraft/storage/WriteView;put(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V"))
    private <T> boolean blast$fixModProjectileSerializationWrite(WriteView instance, String key, Codec<T> codec, T value) {
        return codec != ItemStack.CODEC || !isDisallowed();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private boolean isDisallowed() {
        return (PersistentProjectileEntity) (Object) this instanceof AmethystShardEntity;
    }
}

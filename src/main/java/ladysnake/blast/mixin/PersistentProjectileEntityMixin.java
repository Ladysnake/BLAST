package ladysnake.blast.mixin;

import ladysnake.blast.common.entity.projectiles.AmethystShardEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    @Shadow
    private ItemStack stack;

    @Shadow
    protected abstract ItemStack getDefaultItemStack();

    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void blast$fixModProjectileSerializationRead(ReadView view, CallbackInfo ci) {
        if (isDisallowed()) {
            stack = getDefaultItemStack();
        }
    }

    @Inject(method = "writeCustomData", at = @At("HEAD"))
    private void blast$fixModProjectileSerializationWrite(WriteView view, CallbackInfo ci) {
        if (isDisallowed()) {
            stack = Items.BEDROCK.getDefaultStack();
        }
    }

    @Unique
    private boolean isDisallowed() {
        return (PersistentProjectileEntity) (Object) this instanceof AmethystShardEntity;
    }
}

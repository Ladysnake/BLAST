package ladysnake.blast.mixin;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.init.BlastComponents;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class PipeBombItemMixin {
    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    @Nullable
    private Entity thrower;

    @Inject(method = "tick", at = @At("HEAD"))
    private void blast$replaceWithActualPipeBomb(CallbackInfo ci) {
        if (getStack().isOf(BlastItems.PIPE_BOMB)) {
            ItemEntity thrownItem = ((ItemEntity) (Object) this);
            //noinspection UnreachableCode
            if (!thrownItem.getWorld().isClient
                && this.thrower instanceof PlayerEntity player
            && Boolean.TRUE.equals(this.getStack().get(BlastComponents.ARMED))) {
                for (int i = 0; i < getStack().getCount(); i++) {
                    thrower.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, 1.0F, 1.0f);
                    PipeBombEntity pipeBombEntity = PipeBombEntity.fromItemStack(thrownItem.getWorld(), getStack(), player);
                    pipeBombEntity.setPos(thrownItem.getX(), thrownItem.getY(), thrownItem.getZ());
                    pipeBombEntity.setVelocity(thrownItem.getVelocity());
                    pipeBombEntity.velocityModified = true;
                    thrownItem.getWorld().spawnEntity(pipeBombEntity);
                }
                getStack().decrement(getStack().getCount());
            }
        }
    }
}

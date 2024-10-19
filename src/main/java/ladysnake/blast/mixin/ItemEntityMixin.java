package ladysnake.blast.mixin;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getStack();

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"), cancellable = true)
    private void blast$pipeBomb(CallbackInfo ci) {
        if (getStack().isOf(BlastItems.PIPE_BOMB) && getStack().getOrDefault(BlastComponentTypes.PRIMED, false)) {
            while (!getStack().isEmpty()) {
                PipeBombEntity pipeBomb = BlastEntities.PIPE_BOMB.create(getWorld());
                pipeBomb.setPosition(getPos());
                pipeBomb.setVelocity(getVelocity());
                pipeBomb.setItem(getStack().copyWithCount(1));
                getWorld().spawnEntity(pipeBomb);
                getStack().decrement(1);
            }
            discard();
            ci.cancel();
        }
    }
}

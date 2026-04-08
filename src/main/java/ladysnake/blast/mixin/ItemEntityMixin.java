/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.mixin;

import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.PipeBomb;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getItem();

    public ItemEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"), cancellable = true)
    private void blast$pipeBomb(CallbackInfo ci) {
        if (getItem().is(BlastItems.PIPE_BOMB) && getItem().getOrDefault(BlastComponentTypes.PRIMED, false)) {
            while (!getItem().isEmpty()) {
                PipeBomb bomb = BlastEntityTypes.PIPE_BOMB.create(level(), EntitySpawnReason.SPAWN_ITEM_USE);
                bomb.setPos(position());
                bomb.setDeltaMovement(getDeltaMovement());
                bomb.setItem(getItem().copyWithCount(1));
                level().addFreshEntity(bomb);
                getItem().shrink(1);
            }
            discard();
            ci.cancel();
        }
    }
}

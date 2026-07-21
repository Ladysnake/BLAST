package ladysnake.blast.neoforge.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import ladysnake.blast.common.world.level.CustomExplosionDamageCalculator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin implements Explosion {
    @Shadow
    @Final
    public ExplosionDamageCalculator damageCalculator;

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    @Final
    private Vec3 center;

    @WrapWithCondition(method = "hurtEntities(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;push(Lnet/minecraft/world/phys/Vec3;)V"))
    private boolean blast$pushesEntity(Entity instance, Vec3 impulse) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator) {
            return calculator.pushesEntity(instance);
        }
        return true;
    }

    @Inject(method = "hurtEntities(Ljava/util/List;)V", at = @At("HEAD"))
    private void blast$affectEntity(CallbackInfo ci) {
        if (damageCalculator instanceof CustomExplosionDamageCalculator calculator) {
            final int range = 5;
            for (Entity entity : level.getEntitiesOfClass(Entity.class, new AABB(center.x() - range, center.y() - range, center.z() - range, center.x() + range, center.y() + range, center.z() + range))) {
                calculator.affectEntity(center, entity);
            }
        }
    }
}

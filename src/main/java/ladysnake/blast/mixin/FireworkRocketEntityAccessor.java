package ladysnake.blast.mixin;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireworkRocketEntity.class)
public interface FireworkRocketEntityAccessor {
	@Invoker(value = "explodeAndRemove")
	void callExplodeAndRemove();
}

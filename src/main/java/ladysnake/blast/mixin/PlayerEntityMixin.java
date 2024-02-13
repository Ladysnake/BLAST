package ladysnake.blast.mixin;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.item.PipeBombItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
	public void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
		if (stack.getItem() instanceof PipeBombItem && stack.getOrCreateNbt().getBoolean("Armed")) {
			for (int count = 0; count < stack.getCount(); count++) {
				if (this.world.isClient) {
					this.swingHand(Hand.MAIN_HAND);
				}
				double d = this.getEyeY() - (double) 0.3f;

				this.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
				this.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, 1.0F, 1.0f);

				PipeBombEntity pipeBombEntity = PipeBombEntity.fromItemStack(this.world, stack, (PlayerEntity) Object.class.cast(this));

				if (throwRandomly) {
					float f = this.random.nextFloat() * 0.5f;
					float g = this.random.nextFloat() * ((float) Math.PI * 2);
					pipeBombEntity.setVelocity(-MathHelper.sin(g) * f, 0.2f, MathHelper.cos(g) * f);
				} else {
					float f = 0.8f;
					float g = MathHelper.sin(this.getPitch() * ((float) Math.PI / 180));
					float h = MathHelper.cos(this.getPitch() * ((float) Math.PI / 180));
					float i = MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
					float j = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
					float k = this.random.nextFloat() * ((float) Math.PI * 2);
					float l = 0.02f * this.random.nextFloat();
					pipeBombEntity.setVelocity((double) (-i * h * f) + Math.cos(k) * (double) l, -g * f + 0.1f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f, (double) (j * h * f) + Math.sin(k) * (double) l);
				}

				world.spawnEntity(pipeBombEntity);
			}

			cir.cancel();
		}
	}
}

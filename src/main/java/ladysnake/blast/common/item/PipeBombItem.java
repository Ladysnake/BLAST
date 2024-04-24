package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.init.BlastComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PipeBombItem extends Item {
    public PipeBombItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!playerEntity.isCreative()) {
            for (int i = 0; i < playerEntity.getInventory().size(); i++) {
                if (playerEntity.getInventory().getStack(i).getItem() instanceof BombItem || playerEntity.getInventory().getStack(i).getItem() instanceof TriggerBombItem) {
                    playerEntity.getItemCooldownManager().set(playerEntity.getInventory().getStack(i).getItem(), 20);
                }
            }
        }

        ItemStack stackInHand = playerEntity.getStackInHand(hand);

        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 1.0F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.NEUTRAL, 1.0F, 1.0f);

        if (!world.isClient) {
            PipeBombEntity pipeBombEntity = PipeBombEntity.fromItemStack(world, stackInHand, playerEntity);
            pipeBombEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(pipeBombEntity);
        }

        if (!playerEntity.getAbilities().creativeMode) {
            stackInHand.decrement(1);
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        return new TypedActionResult<>(ActionResult.SUCCESS, stackInHand);
    }

    public void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.NEUTRAL, 0.5F, 1.0f);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (stack.getComponents().getOrDefault(BlastComponents.ARMED, false) && entity instanceof PlayerEntity player) {
            if (!world.isClient) {
                for (int i = 0; i < stack.getCount(); i++) {
                    entity.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, 1.0F, 1.0f);
                    PipeBombEntity pipeBombEntity = PipeBombEntity.fromItemStack(world, stack, player);
                    pipeBombEntity.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0.0F, 0.0f, 1.0F);
                    world.spawnEntity(pipeBombEntity);
                }
                stack.decrement(stack.getCount());
            }
        }
    }

}

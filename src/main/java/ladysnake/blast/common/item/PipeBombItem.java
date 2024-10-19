package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PipeBombItem extends Item {
    public PipeBombItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!player.isCreative()) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                Item item = player.getInventory().getStack(i).getItem();
                if (item instanceof BombItem || item instanceof TriggerBombItem) {
                    player.getItemCooldownManager().set(item, 20);
                }
            }
        }
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            spawn(world, stack, player, 1.5F);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, player.getSoundCategory(), 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
            stack.decrementUnlessCreative(1, player);
            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && stack.getOrDefault(BlastComponentTypes.PRIMED, false) && entity instanceof PlayerEntity player) {
            while (!stack.isEmpty()) {
                spawn(world, stack.copyWithCount(1), player, 0);
                stack.decrement(1);
            }
        }
    }

    private static void spawn(World world, ItemStack stack, PlayerEntity player, float speed) {
        PipeBombEntity entity = BlastEntities.PIPE_BOMB.create(world);
        entity.setVelocity(player, player.getPitch(), player.getYaw(), 0, speed, 1);
        entity.setPos(player.getX(), player.getY() + player.getStandingEyeHeight() - 0.1, player.getZ());
        entity.setOwner(player);
        entity.setItem(stack);
        world.spawnEntity(entity);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, player.getSoundCategory(), 1, 1);
    }
}

package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.init.BlastEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
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
            PipeBombEntity entity = BlastEntities.PIPE_BOMB.create(world);
            entity.setVelocity(player, player.getPitch(), player.getYaw(), 0, 1.5F, 1);
            entity.setPos(player.getX(), player.getY() + player.getStandingEyeHeight() - 0.1, player.getZ());
            entity.setOwner(player);
            entity.setItem(stack);
            world.spawnEntity(entity);
            stack.decrementUnlessCreative(1, player);
            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        playSoundEffects(world, player);
        return TypedActionResult.success(stack, world.isClient);
    }

    private void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.NEUTRAL, 1, 1);
    }
}

package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.PipeBombEntity;
import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastEntities;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PipeBombItem extends Item implements ProjectileItem {
    public PipeBombItem(Item.Settings settings) {
        super(settings);
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        PipeBombEntity bomb = BlastEntities.PIPE_BOMB.create(world, SpawnReason.SPAWN_ITEM_USE);
        bomb.setItem(stack);
        bomb.setPosition(pos.getX(), pos.getY(), pos.getZ());
        return bomb;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!user.isCreative()) {
            for (int i = 0; i < user.getInventory().size(); i++) {
                ItemStack stack = user.getInventory().getStack(i);
                Item item = stack.getItem();
                if (item instanceof BombItem || item instanceof TriggerBombItem) {
                    user.getItemCooldownManager().set(stack, 20);
                }
            }
        }
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient()) {
            spawn(world, stack, user, 1.5F);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, user.getSoundCategory(), 0.5F, 0.4F / (user.getRandom().nextFloat() * 0.4F + 0.8F));
            stack.decrementUnlessCreative(1, user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!world.isClient() && stack.getOrDefault(BlastComponentTypes.PRIMED, false) && entity instanceof PlayerEntity player) {
            while (!stack.isEmpty()) {
                spawn(world, stack.copyWithCount(1), player, 0);
                stack.decrement(1);
            }
        }
    }

    private static void spawn(World world, ItemStack stack, PlayerEntity player, float speed) {
        PipeBombEntity entity = BlastEntities.PIPE_BOMB.create(world, SpawnReason.SPAWN_ITEM_USE);
        entity.setVelocity(player, player.getPitch(), player.getYaw(), 0, speed, 1);
        entity.setPos(player.getX(), player.getY() + player.getStandingEyeHeight() - 0.1, player.getZ());
        entity.setOwner(player);
        entity.setItem(stack);
        world.spawnEntity(entity);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, player.getSoundCategory(), 1, 1);
    }
}

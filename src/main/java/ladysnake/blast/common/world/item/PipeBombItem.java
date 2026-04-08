/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.item;

import ladysnake.blast.common.init.BlastComponentTypes;
import ladysnake.blast.common.init.BlastEntityTypes;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.PipeBomb;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jspecify.annotations.Nullable;

public class PipeBombItem extends Item implements ProjectileItem {
    public PipeBombItem(Properties properties) {
        super(properties);
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        PipeBomb bomb = BlastEntityTypes.PIPE_BOMB.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        bomb.setItem(itemStack);
        bomb.setPos(position.x(), position.y(), position.z());
        return bomb;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (!user.isCreative()) {
            for (int i = 0; i < user.getInventory().getContainerSize(); i++) {
                ItemStack stack = user.getInventory().getItem(i);
                Item item = stack.getItem();
                if (item instanceof BombItem || item instanceof TriggerBombItem) {
                    user.getCooldowns().addCooldown(stack, 20);
                }
            }
        }
        ItemStack stack = user.getItemInHand(hand);
        if (!level.isClientSide()) {
            spawn(level, stack, user, 1.5F);
            level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.SNOWBALL_THROW, user.getSoundSource(), 0.5F, 0.4F / (user.getRandom().nextFloat() * 0.4F + 0.8F));
            stack.consume(1, user);
            user.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (!level.isClientSide() && stack.getOrDefault(BlastComponentTypes.PRIMED, false) && owner instanceof Player player) {
            while (!stack.isEmpty()) {
                spawn(level, stack.copyWithCount(1), player, 0);
                stack.shrink(1);
            }
        }
    }

    private static void spawn(Level level, ItemStack stack, Player player, float speed) {
        PipeBomb bomb = BlastEntityTypes.PIPE_BOMB.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        bomb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, speed, 1);
        bomb.setPosRaw(player.getX(), player.getY() + player.getEyeHeight() - 0.1, player.getZ());
        bomb.setOwner(player);
        bomb.setItem(stack);
        level.addFreshEntity(bomb);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIPWIRE_CLICK_OFF, player.getSoundSource(), 1, 1);
    }
}

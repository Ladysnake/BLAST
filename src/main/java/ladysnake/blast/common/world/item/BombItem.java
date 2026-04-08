/*
 * Copyright (c) doctor4t. All Rights Reserved.
 */

package ladysnake.blast.common.world.item;

import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class BombItem extends Item implements ProjectileItem {
    private final EntityType<Bomb> type;

    public BombItem(Properties properties, EntityType<Bomb> type) {
        super(properties);
        this.type = type;
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        Bomb bomb = type.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
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
            Bomb bomb = type.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
            bomb.setItem(stack);
            bomb.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
            bomb.setPosRaw(user.getX(), user.getY() + user.getEyeHeight() - 0.1, user.getZ());
            bomb.setOwner(user);
            level.addFreshEntity(bomb);
            stack.consume(1, user);
            user.awardStat(Stats.ITEM_USED.get(this));
        }
        playSoundEffects(level, user);
        return InteractionResult.SUCCESS;
    }

    protected void playSoundEffects(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}

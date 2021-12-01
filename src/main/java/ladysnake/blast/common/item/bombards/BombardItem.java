package ladysnake.blast.common.item.bombards;

import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.init.BlastEntities;
import ladysnake.blast.common.init.BlastItems;
import ladysnake.blast.common.item.BombItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BombardItem extends RangedWeaponItem implements Vanishable {
    public BombardItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return itemStack -> itemStack.getItem() == BlastItems.BOMB || itemStack.getItem() == BlastItems.TRIGGER_BOMB;
    }

    @Override
    public int getRange() {
        return 8;
    }

    public BombEntity.BombardModifier getBombardModifier() {
        return BombEntity.BombardModifier.NORMAL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND && user.getStackInHand(Hand.OFF_HAND).getItem() instanceof BombItem) {
            ItemStack stackToConsume = user.getInventory().getStack(PlayerInventory.OFF_HAND_SLOT);
            if (getProjectiles().test(stackToConsume)) {
                BombEntity bomb = BlastEntities.BOMB.create(world);
                if (stackToConsume.getItem() == BlastItems.TRIGGER_BOMB) {
                    bomb = BlastEntities.TRIGGER_BOMB.create(world);
                }

                bomb.setBombardModifier(this.getBombardModifier());
                bomb.setPosition(user.getX(), user.getEyeY() - 0.10000000149011612D, user.getZ());
                bomb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 1.0F);
                bomb.setOwner(user);
                user.getItemCooldownManager().set(this, 60 - (EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, user.getStackInHand(hand)) * 10));

                user.getStackInHand(hand).damage(1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));

                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.NEUTRAL, 0.5F, 1.2f / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                world.spawnEntity(bomb);

                user.incrementStat(Stats.USED.getOrCreateStat(this));

                if (!user.getAbilities().creativeMode) {
                    stackToConsume.decrement(1);
                }

                return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
            }
        } else {
            for (int i = 0; i < user.getInventory().size(); ++i) {
                ItemStack stackToConsume = user.getInventory().getStack(i);
                if (getProjectiles().test(stackToConsume)) {
                    BombEntity bomb = BlastEntities.BOMB.create(world);
                    if (stackToConsume.getItem() == BlastItems.TRIGGER_BOMB) {
                        bomb = BlastEntities.TRIGGER_BOMB.create(world);
                    }

                    bomb.setBombardModifier(this.getBombardModifier());
                    bomb.setPosition(user.getX(), user.getEyeY() - 0.10000000149011612D, user.getZ());
                    bomb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 1.0F);
                    bomb.setOwner(user);
                    user.getItemCooldownManager().set(this, 60 - (EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, user.getStackInHand(hand)) * 10));

                    user.getStackInHand(hand).damage(1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));

                    this.playFireSound(world, user);

                    world.spawnEntity(bomb);

                    user.incrementStat(Stats.USED.getOrCreateStat(this));

                    if (!user.getAbilities().creativeMode) {
                        stackToConsume.decrement(1);
                    }

                    return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
                }
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public void playFireSound(World world, PlayerEntity user) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.NEUTRAL, 0.5F, 1.2f / (world.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }
}

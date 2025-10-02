package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.BombEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class BombItem extends Item implements ProjectileItem {
    private final EntityType<BombEntity> type;

    public BombItem(Item.Settings settings, EntityType<BombEntity> entityType) {
        super(settings);
        this.type = entityType;
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        BombEntity bomb = type.create(world, SpawnReason.SPAWN_ITEM_USE);
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
            BombEntity entity = type.create(world, SpawnReason.SPAWN_ITEM_USE);
            entity.setItem(stack);
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            entity.setPos(user.getX(), user.getY() + user.getStandingEyeHeight() - 0.1, user.getZ());
            entity.setOwner(user);
            world.spawnEntity(entity);
            stack.decrementUnlessCreative(1, user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        playSoundEffects(world, user);
        return ActionResult.SUCCESS;
    }

    protected void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}

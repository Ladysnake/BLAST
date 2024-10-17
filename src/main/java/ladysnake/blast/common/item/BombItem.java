package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.BombEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class BombItem extends Item implements ProjectileItem {
    private final EntityType<BombEntity> type;

    public BombItem(Item.Settings settings, EntityType<BombEntity> entityType) {
        super(settings);
        this.type = entityType;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        BombEntity bomb = type.create(world);
        bomb.setItem(stack);
        bomb.setPosition(pos.getX(), pos.getY(), pos.getZ());
        return bomb;
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
            BombEntity entity = type.create(world);
            entity.setItem(stack);
            entity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
            entity.setPos(player.getX(), player.getY() + player.getStandingEyeHeight() - 0.1, player.getZ());
            entity.setOwner(player);
            world.spawnEntity(entity);
            stack.decrementUnlessCreative(1, player);
            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        playSoundEffects(world, player);
        return TypedActionResult.success(stack, world.isClient);
    }

    protected void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}

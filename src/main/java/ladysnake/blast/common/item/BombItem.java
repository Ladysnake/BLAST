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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.Objects;

public class BombItem extends Item implements ProjectileItem {
    EntityType<BombEntity> type;

    public BombItem(Item.Settings settings, EntityType<BombEntity> entityType) {
        super(settings);
        this.type = entityType;
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

        this.playSoundEffects(world, playerEntity);

        if (!world.isClient) {
            BombEntity entity = Objects.requireNonNull(this.type.create(world));
            entity.setItem(stackInHand);
            entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
            entity.setPos(playerEntity.getX(), playerEntity.getY() + (double) playerEntity.getStandingEyeHeight() - 0.10000000149011612D, playerEntity.getZ());
            entity.setOwner(playerEntity);
            world.spawnEntity(entity);
        }

        if (!playerEntity.getAbilities().creativeMode) {
            stackInHand.decrement(1);
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        return new TypedActionResult<>(ActionResult.SUCCESS, stackInHand);
    }

    public EntityType<BombEntity> getType() {
        return type;
    }

    public void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ProjectileEntity bomb = this.getType().create(world);
        bomb.setPos(pos.getX(), pos.getY(), pos.getZ());
        return bomb;
    }
}

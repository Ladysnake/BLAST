package ladysnake.blast.common.items;

import ladysnake.blast.common.entities.BombEntity;
import net.minecraft.entity.EntityType;
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

public class BombItem extends Item {
    EntityType<BombEntity> type;

    public BombItem(Item.Settings settings, EntityType<BombEntity> entityType) {
        super(settings);
        this.type = entityType;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stackInHand = playerEntity.getStackInHand(hand);

        this.playSoundEffects(world, playerEntity);

        if (!world.isClient) {
            BombEntity entity = this.type.create(world);
            entity.setItem(stackInHand);
            entity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, 1.5F, 1.0F);
            entity.setPosition(playerEntity.getX(), playerEntity.getY() + (double)playerEntity.getStandingEyeHeight() - 0.10000000149011612D, playerEntity.getZ());
            world.spawnEntity(entity);
        }

        if (!playerEntity.abilities.creativeMode) {
            stackInHand.decrement(1);
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        return new TypedActionResult(ActionResult.SUCCESS, stackInHand);
    }

    public EntityType<BombEntity> getType() {
        return type;
    }

    public void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
    }
}

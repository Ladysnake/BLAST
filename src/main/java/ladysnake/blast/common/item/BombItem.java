package ladysnake.blast.common.item;

import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import ladysnake.blast.common.compat.FlanCompat;
import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.item.bombards.BombardItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class BombItem extends Item {
    EntityType<BombEntity> type;

    public BombItem(Item.Settings settings, EntityType<BombEntity> entityType) {
        super(settings);
        this.type = entityType;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (FabricLoader.getInstance().isModLoaded("flan") && world instanceof ServerWorld serverWorld && playerEntity instanceof ServerPlayerEntity serverPlayer) {
            if (!FlanCompat.canInteract(serverWorld, serverPlayer, serverPlayer.getBlockPos(), PermissionRegistry.INTERACTBLOCK)) {
                return new TypedActionResult<>(ActionResult.PASS, playerEntity.getStackInHand(hand));
            }
        }

        if (hand == Hand.OFF_HAND && playerEntity.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BombardItem) {
            return new TypedActionResult<>(ActionResult.PASS, playerEntity.getStackInHand(hand));
        } else {
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
    }

    public EntityType<BombEntity> getType() {
        return type;
    }

    public void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}

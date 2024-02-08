package ladysnake.blast.common.item;

import ladysnake.blast.common.entity.PipeBombEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class PipeBombItem extends Item {
    public PipeBombItem(Settings settings) {
        super(settings);
    }

    private static Stream<ItemStack> getFireworkStacks(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return Stream.empty();
        }
        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
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
            PipeBombEntity entity = new PipeBombEntity(world, playerEntity);
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

    public void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.NEUTRAL, 0.5F, 1.0f);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

}

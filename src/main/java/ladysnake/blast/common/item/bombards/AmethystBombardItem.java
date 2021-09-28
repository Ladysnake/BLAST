package ladysnake.blast.common.item.bombards;

import ladysnake.blast.common.entity.BombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class AmethystBombardItem extends BombardItem {
    public AmethystBombardItem(Settings settings) {
        super(settings);
    }

    @Override
    public BombEntity.BombardModifier getBombardModifier() {
        return BombEntity.BombardModifier.AMETHYST;
    }

    @Override
    public void playFireSound(World world, PlayerEntity user) {
        super.playFireSound(world, user);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.NEUTRAL, 2f, 1f);
    }
}

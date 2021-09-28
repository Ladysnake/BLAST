package ladysnake.blast.common.entity.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class IcicleEntity extends AmethystShardEntity {

    public IcicleEntity(EntityType<? extends AmethystShardEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_GLASS_BREAK;
    }

    public Item getBreakItemParticle() {
        return Items.ICE;
    }

}
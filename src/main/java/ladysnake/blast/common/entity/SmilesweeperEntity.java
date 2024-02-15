package ladysnake.blast.common.entity;

import com.luxintrus.befoul.core.BefoulParticles;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.init.BlastSoundEvents;
import ladysnake.blast.common.world.CustomExplosion;
import ladysnake.blast.common.world.SmilesweeperExplosion;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class SmilesweeperEntity extends BombEntity {
    private static final RegistryKey<Block> INK_BLOCK = RegistryKey.of(Registry.BLOCK_KEY, new Identifier("befoul", "ink"));

    public SmilesweeperEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(80);
    }

    @Override
    public void explode() {
        Block inkBlock = Registry.BLOCK.get(INK_BLOCK);
        if (inkBlock != null) {

            CustomExplosion explosion = new SmilesweeperExplosion(world, this, this.getX(), this.getBodyY(0.0625), this.getZ(), 40f, Explosion.DestructionType.DESTROY, inkBlock);
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);
        }

        if (this.world instanceof ServerWorld serverWorld) {
            this.playSound(BlastSoundEvents.SMILESWEEPER_EXPLODE, 6.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f);

            serverWorld.spawnParticles(
                    BefoulParticles.FALLING_INK,
                    this.getX(), this.getY() + this.getHeight() / 2d, this.getZ(),
                    10000, 0.125, 0.125, 0.125, 0.5f);
        }

        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected Item getDefaultItem() {
        return BlastBlocks.SMILESWEEPER.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        this.onGround = true;
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }
}

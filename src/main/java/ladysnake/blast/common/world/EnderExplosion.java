package ladysnake.blast.common.world;

import ladysnake.blast.common.util.ProtectionsProvider;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EnderExplosion extends CustomExplosion {
    public static final int PARTICLE_DISTANCE = 1;

    public EnderExplosion(World world, Entity entity, double x, double y, double z, float power, DestructionType destructionType) {
        super(world, entity, x, y, z, power, null, destructionType);
    }

    @Override
    public void affectWorld(boolean particles) {
        collectEntities();
        world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 4, (1 + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.9F);
        for (BlockPos pos : affectedBlocks) {
            if (canExplode(pos)) {
                if (!world.getBlockState(pos).isAir()) {
                    if (world.isClient) {
                        for (int x = 0; x <= 1; x += PARTICLE_DISTANCE) {
                            for (int y = 0; y <= 1; y += PARTICLE_DISTANCE) {
                                for (int z = 0; z <= 1; z += PARTICLE_DISTANCE) {
                                    world.addParticle(ParticleTypes.REVERSE_PORTAL, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0, 0, 0);
                                }
                            }
                        }
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        if (!world.isClient) {
            for (Entity foundEntity : affectedEntities) {
                if (ProtectionsProvider.canInteractEntity(foundEntity, damageSource) && foundEntity instanceof LivingEntity living) {
                    for (int i = 0; i < 16; i++) {
                        double x = living.getX() + (living.getRandom().nextDouble() - 0.5) * 16.0;
                        double y = MathHelper.clamp(living.getY() + (double) (living.getRandom().nextInt(16) - 8), world.getBottomY(), world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1);
                        double z = living.getZ() + (living.getRandom().nextDouble() - 0.5) * 16.0;
                        if (living.hasVehicle()) {
                            living.stopRiding();
                        }
                        if (living.teleport(x, y, z, true)) {
                            SoundEvent soundEvent = living instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                            world.playSound(null, x, y, z, soundEvent, living.getSoundCategory(), 1, 1);
                            living.playSound(soundEvent, 1.0F, 1.0F);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldDamageEntities() {
        return false;
    }
}

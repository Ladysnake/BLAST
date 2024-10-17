package ladysnake.blast.common.world;

import ladysnake.blast.common.entity.BombEntity;
import ladysnake.blast.common.entity.ShrapnelBlockEntity;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.util.ProtectionsProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;

public class BonesburrierExplosion extends CustomExplosion {
    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();

    public BonesburrierExplosion(World world, Entity entity, double x, double y, double z, float power, DestructionType destructionType) {
        super(world, entity, x, y, z, power, null, destructionType);
    }

    @Override
    public void affectWorld(boolean particles) {
        Vec3d source = new Vec3d(x, y, z);
        for (BlockPos pos : affectedBlocks) {
            if (canPlace(pos) && canExplode(pos)) {
                BlockState state = world.getBlockState(pos);
                if (!world.isClient()) {
                    PlayerEntity attacker = null;
                    if (damageSource.getSource() instanceof BombEntity bombEntity && bombEntity.getOwner() instanceof PlayerEntity owner) {
                        attacker = owner;
                    } else if (damageSource.getAttacker() != null && damageSource.getAttacker() instanceof PlayerEntity player) {
                        attacker = player;
                    }

                    ShrapnelBlockEntity shrapnelBlockEntity = ShrapnelBlockEntity.spawnFromBlock(world, pos, state, attacker);
                    shrapnelBlockEntity.dropItem = false;
                    shrapnelBlockEntity.setVelocity(new Vec3d(pos.getX(), pos.getY(), pos.getZ()).subtract(source).normalize());
                    shrapnelBlockEntity.velocityModified = true;
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());

                    // paint
                    BlockPos.Mutable mutable = new BlockPos.Mutable();
                    for (Direction direction : Direction.values()) {
                        if (ProtectionsProvider.canPlaceBlock(mutable.set(pos, direction), world, damageSource)) {
                            BlockState adjacentBlockState = world.getBlockState(mutable.set(pos, direction));
                            FluidState fluidState = world.getFluidState(mutable.set(pos, direction));
                            Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(this, world, mutable.set(pos, direction), adjacentBlockState, fluidState);
                            if (optional.isPresent() && optional.get() < 1200 && !affectedBlocks.contains(mutable.set(pos, direction))) {
                                world.setBlockState(mutable.set(pos, direction), BlastBlocks.FOLLY_RED_PAINT.getDefaultState());
                            }
                        }
                    }
                }
                state.getBlock().onDestroyedByExplosion(world, pos, this);
            }
        }
        super.affectWorld(particles);
    }
}

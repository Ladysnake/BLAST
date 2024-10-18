package ladysnake.blast.common.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ladysnake.blast.common.entity.StripminerEntity;
import ladysnake.blast.common.init.BlastBlocks;
import ladysnake.blast.common.util.ProtectionsProvider;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashSet;
import java.util.Set;

public class CustomExplosion extends Explosion {
    public final BlockBreakEffect effect;
    public final Set<Entity> affectedEntities = new HashSet<>();

    public CustomExplosion(World world, Entity entity, double x, double y, double z, float power, BlockBreakEffect effect, DestructionType destructionType) {
        super(world, entity, x, y, z, power, false, destructionType);
        this.effect = effect;
    }

    @Override
    public void affectWorld(boolean particles) {
        world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4, (1 + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
        boolean destroy = destructionType != DestructionType.KEEP;
        if (particles) {
            if (getPower() >= 2 && destroy) {
                world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0, 0);
            } else {
                world.addParticle(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0);
            }
        }
        if (destroy) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> destroyedBlocks = new ObjectArrayList<>();
            for (BlockPos pos : affectedBlocks) {
                if (canExplode(pos)) {
                    BlockState state = world.getBlockState(pos);
                    if (particles) {
                        double rX = pos.getX() + world.random.nextFloat();
                        double rY = pos.getY() + world.random.nextFloat();
                        double rZ = pos.getZ() + world.random.nextFloat();
                        double dX = rX - x;
                        double dY = rY - y;
                        double dZ = rZ - z;
                        double product = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                        dX /= product;
                        dY /= product;
                        dZ /= product;
                        double multiplier = 0.5 / (product / getPower() + 0.1);
                        multiplier *= world.random.nextFloat() * world.random.nextFloat() + 0.3F;
                        dX *= multiplier;
                        dY *= multiplier;
                        dZ *= multiplier;
                        if (state.getFluidState().isEmpty()) {
                            world.addParticle(ParticleTypes.POOF, (rX + x) / 2, (rY + y) / 2, (rZ + z) / 2, dX, dY, dZ);
                            world.addParticle(ParticleTypes.SMOKE, rX, rY, rZ, dX, dY, dZ);
                        } else {
                            world.addParticle(ParticleTypes.BUBBLE, (rX + x) / 2, (rY + y) / 2, (rZ + z) / 2, dX, dY, dZ);
                            world.addParticle(ParticleTypes.BUBBLE_POP, rX, rY, rZ, dX, dY, dZ);
                        }
                    }
                    boolean canDestroy = !state.isAir();
                    if (effect != BlockBreakEffect.AQUATIC && !state.getFluidState().isEmpty()) {
                        canDestroy = false;
                    }
                    if (canDestroy) {
                        world.getProfiler().push("explosion_blocks");
                        if (world instanceof ServerWorld serverWorld) {
                            if (state.getBlock().shouldDropItemsOnExplosion(this)) {
                                ItemStack stack = Items.NETHERITE_PICKAXE.getDefaultStack();
                                if (effect == BlockBreakEffect.FORTUNE) {
                                    stack.addEnchantment(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(Enchantments.FORTUNE), 3);
                                }
                                state.getDroppedStacks(getBuilder(serverWorld, pos, stack, world.getBlockEntity(pos) != null ? world.getBlockEntity(pos) : null)).forEach(droppedStack -> tryMergeStack(destroyedBlocks, droppedStack, pos));
                            }
                            BlockState toPlace = Blocks.AIR.getDefaultState();
                            if (effect == BlockBreakEffect.FROSTY) {
                                toPlace = BlastBlocks.DRY_ICE.getDefaultState();
                                if (entity instanceof StripminerEntity stripminer) {
                                    toPlace = toPlace.with(PillarBlock.AXIS, stripminer.getFacing().getAxis());
                                }
                            }
                            world.setBlockState(pos, toPlace);
                        }
                        state.getBlock().onDestroyedByExplosion(world, pos, this);
                        world.getProfiler().pop();
                    } else if (!state.getFluidState().isEmpty()) {
                        BlockState toPlace = Blocks.AIR.getDefaultState();
                        if (state.getFluidState().isStill()) {
                            if (state.getFluidState().getFluid() == Fluids.WATER) {
                                if (effect == BlockBreakEffect.FROSTY) {
                                    toPlace = Blocks.ICE.getDefaultState();
                                } else {
                                    toPlace = state;
                                }
                            } else if (state.getFluidState().getFluid() == Fluids.LAVA) {
                                if (effect == BlockBreakEffect.FROSTY) {
                                    toPlace = Blocks.BASALT.getDefaultState();
                                    if (entity instanceof StripminerEntity stripminer) {
                                        toPlace = toPlace.with(PillarBlock.AXIS, stripminer.getFacing().getAxis());
                                    }
                                } else {
                                    toPlace = state;
                                }
                            }
                        }
                        world.setBlockState(pos, toPlace);
                    }
                }
            }
            destroyedBlocks.forEach(pair -> Block.dropStack(world, pair.getSecond(), pair.getFirst()));
        }
        if (effect == BlockBreakEffect.FIERY) {
            for (BlockPos pos : affectedBlocks) {
                if (!world.isClient && canPlace(pos)) {
                    if (random.nextInt(3) == 0 && world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOpaqueFullCube(world, pos.down())) {
                        world.setBlockState(pos, AbstractFireBlock.getState(world, pos));
                    }
                }
            }
        }
    }

    public boolean shouldDamageEntities() {
        return true;
    }

    protected LootContextParameterSet.Builder getBuilder(ServerWorld serverWorld, BlockPos pos, ItemStack stack, BlockEntity blockEntity) {
        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(serverWorld)
            .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
            .add(LootContextParameters.TOOL, stack)
            .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity)
            .addOptional(LootContextParameters.THIS_ENTITY, entity);
        if (destructionType == DestructionType.DESTROY_WITH_DECAY) {
            builder.add(LootContextParameters.EXPLOSION_RADIUS, getPower());
        }
        return builder;
    }

    protected boolean canExplode(BlockPos blockPos) {
        return ProtectionsProvider.canExplodeBlock(blockPos, world, this, damageSource);
    }

    protected boolean canPlace(BlockPos blockPos) {
        return ProtectionsProvider.canPlaceBlock(blockPos, world, damageSource);
    }

    public enum BlockBreakEffect {
        FORTUNE,
        UNSTOPPABLE,
        AQUATIC,
        FIERY,
        FROSTY
    }
}

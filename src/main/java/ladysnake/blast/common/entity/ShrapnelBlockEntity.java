package ladysnake.blast.common.entity;

import ladysnake.blast.common.util.ProtectionsProvider;
import ladysnake.blast.mixin.FallingBlockEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShrapnelBlockEntity extends FallingBlockEntity {
    @Nullable
    private PlayerEntity owner;

    public ShrapnelBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    private ShrapnelBlockEntity(World world, double x, double y, double z, BlockState block, @Nullable PlayerEntity owner) {
        this(EntityType.FALLING_BLOCK, world);
        ((FallingBlockEntityAccessor) this).blast$setBlock(block);
        intersectionChecked = true;
        setPosition(x, y, z);
        setVelocity(Vec3d.ZERO);
        prevX = x;
        prevY = y;
        prevZ = z;
        this.owner = owner;
        setFallingBlockPos(getBlockPos());
    }

    public static ShrapnelBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state, @Nullable PlayerEntity owner) {
        var explosionThrownBlockEntity = new ShrapnelBlockEntity(
            world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5,
            state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, false) : state, owner
        );
        world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
        world.spawnEntity(explosionThrownBlockEntity);
        return explosionThrownBlockEntity;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (!getWorld().isClient && owner != null) {
            nbt.putUuid("Owner", owner.getUuid());
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner") && getWorld() instanceof ServerWorld serverWorld) {
            owner = serverWorld.getServer().getPlayerManager().getPlayer(nbt.getUuid("Owner"));
        }
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        if (isOnGround() && !getWorld().isClient && !ProtectionsProvider.canPlaceBlock(getBlockPos(), getWorld(), owner)) {
            setDestroyedOnLanding();
        }
    }
}

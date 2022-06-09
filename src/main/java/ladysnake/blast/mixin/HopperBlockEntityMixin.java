package ladysnake.blast.mixin;

import ladysnake.blast.common.block.RemoteDetonatorBlock;
import ladysnake.blast.common.init.BlastBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity implements Hopper {
    @Shadow private DefaultedList<ItemStack> inventory;

    protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void serverTick(World world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, CallbackInfo callbackInfo) {
        if (world.getBlockState(pos.add(0, 1, 0)).equals(BlastBlocks.REMOTE_DETONATOR.getDefaultState().with(RemoteDetonatorBlock.FILLED, true))) {

            System.out.println(blockEntity.inventory.get(0));
            if (blockEntity.inventory.add(new ItemStack(Items.ENDER_EYE))) {
                world.setBlockState(pos.add(0, 1, 0), world.getBlockState(pos.add(0, 1, 0)).with(RemoteDetonatorBlock.FILLED, false));
            }
        }
    }
}

package ladysnake.blast.common.compat;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.data.IPermissionStorage;
import io.github.flemmli97.flan.api.permission.ClaimPermission;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class FlanCompat {
    public static boolean canInteract(ServerWorld world, ServerPlayerEntity player, BlockPos pos, ClaimPermission type) {
        IPermissionStorage storage = ClaimHandler.getPermissionStorage(world);
        IPermissionContainer container = storage.getForPermissionCheck(pos);

        return container.canInteract(player, type, pos);
    }
}

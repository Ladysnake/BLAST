package ladysnake.blast.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import ladysnake.blast.common.world.entity.projectile.throwableitemprojectile.Bomb;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastBlockEntityRenderer<T extends Bomb> extends EntityRenderer<T, TntRenderState> {
    private final BlockModelResolver blockModelResolver;
    private final Function<T, BlockState> stateGetter;

    public BlastBlockEntityRenderer(EntityRendererProvider.Context context, Function<T, BlockState> stateGetter) {
        super(context);
        blockModelResolver = context.getBlockModelResolver();
        this.stateGetter = stateGetter;
        shadowRadius = 0.5F;
    }

    @Override
    public void submit(TntRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5F, 0);
        float fuse = state.fuseRemainingInTicks;
        if (state.fuseRemainingInTicks < 10) {
            float multiplier = 1 - state.fuseRemainingInTicks / 10;
            multiplier = Mth.clamp(multiplier, 0, 1);
            multiplier *= multiplier;
            multiplier *= multiplier;
            float scale = 1 + multiplier * 0.3F;
            poseStack.scale(scale, scale, scale);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        if (!state.blockState.isEmpty()) {
            TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, poseStack, submitNodeCollector, state.lightCoords, (int) fuse / 5 % 2 == 0, state.outlineColor);
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public TntRenderState createRenderState() {
        return new TntRenderState();
    }

    @Override
    public void extractRenderState(T entity, TntRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.fuseRemainingInTicks = entity.getFuseTimer();
        blockModelResolver.update(state.blockState, stateGetter.apply(entity), TntRenderer.BLOCK_DISPLAY_CONTEXT);
    }
}

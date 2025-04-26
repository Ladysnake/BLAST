//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ladysnake.blast.client.renderers;

import ladysnake.blast.common.entity.BombEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastBlockEntityRenderer<T extends BombEntity> extends EntityRenderer<T, TntEntityRenderState> {

    private final Function<T, BlockState> stateGetter;
    private final BlockRenderManager blockRenderManager;

    public BlastBlockEntityRenderer(EntityRendererFactory.Context ctx, Function<T, BlockState> stateGetter) {
        super(ctx);

        this.stateGetter = stateGetter;
        this.shadowRadius = 0.5F;
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Override
    public void render(TntEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0.0F, 0.5F, 0.0F);
        float f = state.fuse;
        if (state.fuse < 10.0F) {
            float g = 1.0F - state.fuse / 10.0F;
            g = MathHelper.clamp(g, 0.0F, 1.0F);
            g *= g;
            g *= g;
            float h = 1.0F + g * 0.3F;
            matrices.scale(h, h, h);
        }

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
        matrices.translate(-0.5F, -0.5F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        if (state.blockState != null) {
            TntMinecartEntityRenderer.renderFlashingBlock(blockRenderManager, state.blockState, matrices, vertexConsumers, light, (int) f / 5 % 2 == 0);
        }

        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public TntEntityRenderState createRenderState() {
        return new TntEntityRenderState();
    }

    @Override
    public void updateRenderState(T entity, TntEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        state.blockState = stateGetter.apply(entity);
        state.fuse = entity.getFuseTimer();
    }
}

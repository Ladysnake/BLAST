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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastBlockEntityRenderer<T extends BombEntity> extends EntityRenderer<T> {
    private final Function<T, BlockState> stateGetter;
    private final BlockRenderManager blockRenderManager;

    public BlastBlockEntityRenderer(EntityRendererFactory.Context ctx, Function<T, BlockState> stateGetter) {
        super(ctx);

        this.stateGetter = stateGetter;
        this.shadowRadius = 0.5F;
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Override
    public void render(T bombEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.5D, 0.0D);
        if ((float) bombEntity.getFuseTimer() - g + 1.0F < 10.0F) {
            float h = 1.0F - ((float) bombEntity.getFuseTimer() - g + 1.0F) / 10.0F;
            h = MathHelper.clamp(h, 0.0F, 1.0F);
            h *= h;
            h *= h;
            float j = 1.0F + h * 0.3F;
            matrixStack.scale(j, j, j);
        }

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
        matrixStack.translate(-0.5D, -0.5D, 0.5D);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

        TntMinecartEntityRenderer.renderFlashingBlock(this.blockRenderManager, this.stateGetter.apply(bombEntity), matrixStack, vertexConsumerProvider, i, bombEntity.getFuseTimer() / 5 % 2 == 0);
        matrixStack.pop();
        super.render(bombEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(T stripminerEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}

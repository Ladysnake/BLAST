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
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlastBlockEntityRenderer<T extends BombEntity> extends EntityRenderer<T> {
    private final Function<T, BlockState> stateGetter;

    public BlastBlockEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, Function<T, BlockState> stateGetter) {
        super(entityRenderDispatcher);
        this.stateGetter = stateGetter;
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(T stripminerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.5D, 0.0D);
        if ((float)stripminerEntity.getFuseTimer() - g + 1.0F < 10.0F) {
            float h = 1.0F - ((float)stripminerEntity.getFuseTimer() - g + 1.0F) / 10.0F;
            h = MathHelper.clamp(h, 0.0F, 1.0F);
            h *= h;
            h *= h;
            float j = 1.0F + h * 0.3F;
            matrixStack.scale(j, j, j);
        }

        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
        matrixStack.translate(-0.5D, -0.5D, 0.5D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

        TntMinecartEntityRenderer.renderFlashingBlock(this.stateGetter.apply(stripminerEntity), matrixStack, vertexConsumerProvider, i, stripminerEntity.getFuseTimer() / 5 % 2 == 0);
        matrixStack.pop();
        super.render(stripminerEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(T stripminerEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

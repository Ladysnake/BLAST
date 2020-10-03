//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ladysnake.blast.client.renderers;

import ladysnake.blast.common.block.StripminerBlock;
import ladysnake.blast.common.entities.StripminerEntity;
import ladysnake.blast.common.init.BlastBlocks;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class StripminerEntityRenderer extends EntityRenderer<StripminerEntity> {
    public StripminerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
        this.shadowRadius = 0.5F;
    }

    public void render(StripminerEntity stripminerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
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

        BlockState stripminerState = BlastBlocks.STRIPMINER.getDefaultState().with(StripminerBlock.FACING, stripminerEntity.getFacing());
        TntMinecartEntityRenderer.renderFlashingBlock(stripminerState, matrixStack, vertexConsumerProvider, i, stripminerEntity.getFuseTimer() / 5 % 2 == 0);
        matrixStack.pop();
        super.render(stripminerEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(StripminerEntity stripminerEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

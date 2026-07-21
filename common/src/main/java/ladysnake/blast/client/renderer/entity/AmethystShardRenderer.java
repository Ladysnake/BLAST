package ladysnake.blast.client.renderer.entity;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.world.entity.projectile.arrow.AmethystShard;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;

public class AmethystShardRenderer extends ArrowRenderer<AmethystShard, ArrowRenderState> {
    public static final Identifier TEXTURE = Blast.id("textures/entity/projectiles/amethyst_shard.png");

    public AmethystShardRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }
}

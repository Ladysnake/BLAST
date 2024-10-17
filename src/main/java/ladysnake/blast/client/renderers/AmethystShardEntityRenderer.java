package ladysnake.blast.client.renderers;

import ladysnake.blast.common.Blast;
import ladysnake.blast.common.entity.projectiles.AmethystShardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class AmethystShardEntityRenderer extends ProjectileEntityRenderer<AmethystShardEntity> {
    public static final Identifier TEXTURE = Blast.id("textures/entity/projectiles/amethyst_shard.png");

    public AmethystShardEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(AmethystShardEntity entity) {
        return TEXTURE;
    }
}

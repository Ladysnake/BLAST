package ladysnake.blast.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.jetbrains.annotations.Nullable;

public class BlastDamageSources {
    public static DamageSource amethystShard(PersistentProjectileEntity projectile, @Nullable Entity attacker) {
        return (new ProjectileDamageSource("amethyst_shard", projectile, attacker)).setProjectile();
    }

}

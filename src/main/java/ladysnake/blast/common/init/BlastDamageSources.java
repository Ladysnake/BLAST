package ladysnake.blast.common.init;

import ladysnake.blast.common.Blast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlastDamageSources {

    private static final RegistryKey<DamageType> AMETHYST_SHARD = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Blast.MODID, "amethyst_shard"));
    private static final RegistryKey<DamageType> ICICLE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Blast.MODID, "icicle"));

    public static DamageSource amethystShard(PersistentProjectileEntity projectile, @Nullable Entity attacker) {
        return create(AMETHYST_SHARD, projectile.getWorld(), null, attacker);
    }

    public static DamageSource icicle(PersistentProjectileEntity projectile, @Nullable Entity attacker) {
        return create(ICICLE, projectile.getWorld(), null, attacker);
    }

    private static DamageSource create(RegistryKey<DamageType> key, World world, @Nullable Entity source, @Nullable Entity attacker) {
        return world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .getEntry(key)
                .map((type) -> new DamageSource(type, source, attacker))
                .orElse(world.getDamageSources().genericKill()); // Fallback, should never reach this
    }
}

package ladysnake.blast.common.world;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class KnockbackExplosion extends CustomExplosion {
    public KnockbackExplosion(World world, Entity entity, double x, double y, double z, float power) {
        super(world, entity, x, y, z, power, null, DestructionType.NONE);
    }

    public float getPower() {
        return power;
    }

    public void collectBlocksAndDamageEntities() {
        Set<BlockPos> set = Sets.newHashSet();

        int k;
        int l;
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                for (l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (float) j / 15.0F * 2.0F - 1.0F;
                        double e = (float) k / 15.0F * 2.0F - 1.0F;
                        double f = (float) l / 15.0F * 2.0F - 1.0F;
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double m = this.x;
                        double n = this.y;
                        double o = this.z;

                        for (float var21 = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = new BlockPos(m, n, o);
                            BlockState blockState = this.world.getBlockState(blockPos);
                            FluidState fluidState = this.world.getFluidState(blockPos);
                            if (!blockState.isAir() || !fluidState.isEmpty()) {
                                float br = Math.max(blockState.getBlock().getBlastResistance(), fluidState.getBlastResistance());
                                if (((this.effect == BlockBreakEffect.AQUATIC || this.effect == BlockBreakEffect.FROSTY) && !fluidState.isEmpty()) || (this.effect == BlockBreakEffect.UNSTOPPABLE && fluidState.isEmpty() && blockState.getHardness(this.world, blockPos) >= 0)) {
                                    br = 0;
                                }
                                if (this.entity != null) {
                                    br = this.entity.getEffectiveExplosionResistance(this, this.world, blockPos, blockState, fluidState, br);
                                }

                                h -= (br + 0.3F) * 0.3F;
                            }


                            if (h > 0.0F && (this.entity == null || this.entity.canExplosionDestroyBlock(this, this.world, blockPos, blockState, h))) {
                                set.add(blockPos);
                            }

                            m += d * 0.30000001192092896D;
                            n += e * 0.30000001192092896D;
                            o += f * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0F;
        k = MathHelper.floor(this.x - (double) q - 1.0D);
        l = MathHelper.floor(this.x + (double) q + 1.0D);
        int t = MathHelper.floor(this.y - (double) q - 1.0D);
        int u = MathHelper.floor(this.y + (double) q + 1.0D);
        int v = MathHelper.floor(this.z - (double) q - 1.0D);
        int w = MathHelper.floor(this.z + (double) q + 1.0D);
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, t, v, l, u, w));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int i = 0; i < 500; i++) {
            world.addParticle(ParticleTypes.SNEEZE, this.x, this.y, this.z, this.random.nextGaussian() / 5, this.random.nextGaussian() / 5, this.random.nextGaussian() / 5);
        }

        for (int x = 0; x < list.size(); ++x) {
            Entity entity = list.get(x);
            if (!entity.isImmuneToExplosion()) {
                double y = Math.sqrt(entity.squaredDistanceTo(vec3d)) / q;
                if (y <= 1.0D) {
                    double z = entity.getX() - this.x;
                    double aa = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double ab = entity.getZ() - this.z;
                    double ac = Math.sqrt(z * z + aa * aa + ab * ab);
                    if (ac != 0.0D) {
                        z /= ac;
                        aa /= ac;
                        ab /= ac;
                        double ad = getExposure(vec3d, entity);
                        double ae = (1.0D - y) * ad;
                        double af = ae;
                        if (entity instanceof LivingEntity) {
                            af = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ae);
                        }

                        int knockbackMultiplier = 3;
                        entity.setVelocity(entity.getVelocity().add(z * af * knockbackMultiplier, aa * af * knockbackMultiplier, ab * af * knockbackMultiplier));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerEntity = (PlayerEntity) entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, new Vec3d(z * ae, aa * ae, ab * ae));
                            }
                        }
                    }
                }
            }
        }

    }

}

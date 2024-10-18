package ladysnake.blast.common.util;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import eu.pb4.common.protection.api.CommonProtection;
import ladysnake.blast.common.entity.BombEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ProtectionsProvider {
    public static boolean canDamageEntity(Entity entity, DamageSource damageSource) {
        return checkProtection(entity.getWorld(), entity.getBlockPos(), damageSource, ret ->
            CommonProtection.canDamageEntity(entity.getWorld(), entity, ret.getFirst(), ret.getSecond()));
    }

    public static boolean canInteractEntity(Entity entity, DamageSource damageSource) {
        return checkProtection(entity.getWorld(), entity.getBlockPos(), damageSource, ret ->
            CommonProtection.canInteractEntity(entity.getWorld(), entity, ret.getFirst(), ret.getSecond()));
    }

    public static boolean canExplodeBlock(BlockPos blockPos, World world, Explosion explosion, DamageSource damageSource) {
        return checkProtection(world, blockPos, damageSource, ret ->
            CommonProtection.canExplodeBlock(world, blockPos, explosion, ret.getFirst(), ret.getSecond()));
    }

    public static boolean canPlaceBlock(BlockPos blockPos, World world, DamageSource damageSource) {
        return checkProtection(world, blockPos, damageSource, ret ->
            CommonProtection.canPlaceBlock(world, blockPos, ret.getFirst(), ret.getSecond()));
    }

    public static boolean canPlaceBlock(BlockPos blockPos, World world, @Nullable PlayerEntity player) {
        var gameProfile = (player != null) ? player.getGameProfile() : null;
        if (gameProfile != null) {
            return CommonProtection.canPlaceBlock(world, blockPos, gameProfile, player);
        } else {
            return !CommonProtection.isProtected(world, blockPos);
        }
    }

    private static boolean checkProtection(World world, BlockPos blockPos, DamageSource damageSource, Predicate<Pair<GameProfile, ServerPlayerEntity>> protectionPredicate) {
        Entity attacker = null;
        if (damageSource.getAttacker() != null) {
            attacker = damageSource.getAttacker();
        } else if (damageSource.getSource() instanceof BombEntity bombEntity) {
            attacker = bombEntity.getOwner();
        }
        if (attacker instanceof ServerPlayerEntity playerAttacker) {
            return protectionPredicate.test(Pair.of(playerAttacker.getGameProfile(), playerAttacker));
        } else {
            return !CommonProtection.isProtected(world, blockPos);
        }
    }
}

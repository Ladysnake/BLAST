//package ladysnake.blast.common.entity;
//
//import ladysnake.blast.common.init.BlastItems;
//import ladysnake.blast.common.world.CustomExplosion;
//import ladysnake.blast.common.world.KnockbackExplosion;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.item.Item;
//import net.minecraft.world.World;
//
//public class PillowBombEntity extends BombEntity {
//    public PillowBombEntity(EntityType<? extends BombEntity> entityType, World world) {
//        super(entityType, world);
//    }
//
//    public PillowBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
//        super(entityType, world, livingEntity);
//    }
//
//    @Override
//    protected Item getDefaultItem() {
//        return BlastItems.PILLOW_BOMB;
//    }
//
//    @Override
//    protected CustomExplosion getExplosion() {
//        return new KnockbackExplosion(this.world, this, this.getX(), this.getY(), this.getZ(), 3f);
//    }
//}

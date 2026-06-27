package com.yuo.bh3.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class WeaponArrowEntity extends AbstractArrow {

    public WeaponArrowEntity(EntityType<? extends AbstractArrow> type, Level world) {
        super(BH3EntityTypes.WEAPON_ARROW.get(), world);
        this.setBaseDamage(5);
    }

    public WeaponArrowEntity(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level world) {
        super(BH3EntityTypes.WEAPON_ARROW.get(), x, y, z,world);
        this.setBaseDamage(5);
    }

    public WeaponArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level world) {
        super(type, world);
        this.setBaseDamage(5);
    }

    @Override
    public void tick() {
        super.tick();
        // 如果启用了自定义重力
        if (!this.isNoGravity()) {
            Vec3 motion = this.getDeltaMovement();
            // 原版重力是 -0.05，这里我们将其替换为 -0.05 * gravityMultiplier
            // 但由于 super.tick() 已经应用了 -0.05，我们需要补偿
            // 我们可以将 motion.y += -0.05 * gravityMultiplier - (-0.05) = -0.05 * (gravityMultiplier - 1)
            motion = motion.add(0, -0.05 * (1.5 - 1), 0);
            this.setDeltaMovement(motion);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("damage", getBaseDamage());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBaseDamage(compound.getDouble("damage"));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            if (entity.isAlive())
                entity.hurt(entity.damageSources().mobAttack((LivingEntity) getOwner()), (float) getBaseDamage());
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BlockPos blockpos = new BlockPos(result.getBlockPos());
        this.level().getBlockState(blockpos).entityInside(this.level(), blockpos, this);
        if (!this.level().isClientSide) {
            this.level().explode(getOwner(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), 5, ExplosionInteraction.NONE);
            this.discard();
        }

        super.onHitBlock(result);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    protected float getWaterInertia() {
        return 0.95f;
    }
}

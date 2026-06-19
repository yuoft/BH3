package com.yuo.bh3.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class PlacedWeaponEntity extends Entity {
    private int rotationYaw = 0; // 水平旋转角度
    private int rotationPitch = 0; // 俯仰角度（可让武器倾斜）
    private double verticalSpeed = 0.0;
    private static final double GRAVITY = 0.04;
    private static final double OFFSET_BOTTOM = 0.25; // 实体中心到底部的距离

    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(PlacedWeaponEntity.class, EntityDataSerializers.ITEM_STACK);


    public PlacedWeaponEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true); // 不受重力影响
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            double bottomY = this.getY() - OFFSET_BOTTOM;
            BlockPos belowPos = new BlockPos((int)Math.floor(this.getX()), (int)Math.floor(bottomY), (int)Math.floor(this.getZ()));
            boolean solidBelow = this.level().getBlockState(belowPos).isSolid();
            if (solidBelow) {
                double blockTop = belowPos.getY() + 1.0;
                if (bottomY < blockTop) {
                    // 已接触到地面，修正位置并停止速度
                    this.setPos(this.getX(), blockTop, this.getZ());
                    this.verticalSpeed = 0;
                } else {
                    // 尚未接触地面，持续下落
                    applyGravity();
                }
            } else {
                // 下方无方块，下落
                applyGravity();
            }
        }
    }

    private void applyGravity() {
        this.verticalSpeed -= GRAVITY;
        this.setPos(this.getX(), this.getY() + this.verticalSpeed, this.getZ());
    }

    /**
     * 添加物理实体的物品信息
     * @param stack 物品
     * @param pos 坐标
     */
    public void setWeapon(ItemStack stack, BlockPos pos, float yaw, float pitch) {
        this.setWeaponStack(stack.copy());
        this.setCustomName(stack.getDisplayName());
        this.rotationYaw = (int) yaw;
        this.rotationPitch = (int) pitch;
        // 将实体放置在方块中心偏上一点，避免嵌入方块
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);
        // 设置实体的旋转
        this.setYRot(yaw);
        this.setXRot(pitch);
    }

    @Override
    public Component getDisplayName() {
        return getWeaponStack().getDisplayName();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.level().isClientSide()) {
            return InteractionResult.PASS; // 客户端不处理
        }
        if (hand == InteractionHand.MAIN_HAND) {
            return pickUp(player);
        }
        return InteractionResult.PASS;
    }

    public InteractionResult pickUp(Player player) {
        ItemStack weaponStack = getWeaponStack();
        if (weaponStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        // 1. 优先放入主手（若主手为空）
        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, weaponStack.copy());
            this.discard();
            return InteractionResult.SUCCESS;
        }

        // 2. 否则尝试放入背包（包括副手和其他槽位）
        if (!player.getInventory().add(weaponStack.copy())) {
            player.drop(weaponStack.copy(), false);
        }
        this.discard();
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Weapon")) {
            ItemStack weaponStack = ItemStack.of(tag.getCompound("Weapon"));
            setWeaponStack(weaponStack);
        }
        rotationYaw = tag.getInt("RotationYaw");
        rotationPitch = tag.getInt("RotationPitch");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (!getWeaponStack().isEmpty()) {
            tag.put("Weapon", getWeaponStack().save(new CompoundTag()));
        }
        tag.putInt("RotationYaw", rotationYaw);
        tag.putInt("RotationPitch", rotationPitch);
    }

    // 保存数据（持久化）
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_WEAPON, ItemStack.EMPTY);
    }

    public ItemStack getWeaponStack() {
        return this.entityData.get(DATA_WEAPON);
    }

    public void setWeaponStack(ItemStack time) {
        this.entityData.set(DATA_WEAPON, time);
    }

    // 网络同步
    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    // 碰撞箱
    @Override
    public AABB getBoundingBoxForCulling() {
        return new AABB(this.getX() - 0.3, this.getY() - 0.1, this.getZ() - 0.3,
                this.getX() + 0.3, this.getY() + 0.6, this.getZ() + 0.3);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 0.25f; // 适当增大，方便点击
    }
}
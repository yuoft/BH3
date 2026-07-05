package com.yuo.bh3.Entity;

import com.yuo.bh3.BH3;
import com.yuo.bh3.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlacedWeaponEntity extends Entity {
    private static final double GRAVITY = 0.04;
    private static final double OFFSET_BOTTOM = 0.25; // 实体中心到底部的距离
    private static final long ATTACK_COOLDOWN_TICKS = 20; // 1 秒冷却

    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(PlacedWeaponEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(PlacedWeaponEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    // 攻击冷却映射（实体UUID -> 上次攻击的游戏刻）
    private final Map<UUID, Long> attackedEntities = new HashMap<>();
    private int rotationYaw = 0;
    private int rotationPitch = 0;
    private double verticalSpeed = 0.0;
    private UUID ownerUUID; // 所有者的 UUID

    public PlacedWeaponEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true); // 我们手动控制重力
    }

    @SuppressWarnings("removal")
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (getWeaponStack().isEmpty()) discard();
            // ----- 重力与地面处理 -----
            double bottomY = this.getY() - OFFSET_BOTTOM;
            BlockPos belowPos = new BlockPos((int) Math.floor(this.getX()), (int) Math.floor(bottomY), (int) Math.floor(this.getZ()));
            boolean solidBelow = this.level().getBlockState(belowPos).isSolid();

            if (solidBelow) {
                double blockTop = belowPos.getY() + 1.0;
                if (bottomY < blockTop) {
                    // 已接触地面，修正位置并停止速度
                    this.setPos(this.getX(), blockTop, this.getZ());
                    this.verticalSpeed = 0;
                } else {
                    applyGravity();
                }
            } else {
                applyGravity();
            }

            // ----- 下落攻击逻辑（只有在下落时才攻击） -----
            if (this.verticalSpeed != 0) {
                attackNearbyEntities();
            }
        }
    }

    private void applyGravity() {
        this.verticalSpeed -= GRAVITY;
        this.setPos(this.getX(), this.getY() + this.verticalSpeed, this.getZ());
    }

    /**
     * 检测并攻击碰撞箱内的生物
     */
    private void attackNearbyEntities() {
        AABB aabb = this.getBoundingBox();
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        long currentTime = this.level().getGameTime();

        // 获取所有者（玩家）
        Player owner = this.getOwnerPlayer();

        for (LivingEntity living : entities) {
            if (!living.isAlive()) continue;
            // 如果是创造模式玩家，跳过
            if (living instanceof Player player && player.getAbilities().instabuild) continue;

            UUID uuid = living.getUUID();
            Long lastAttackTime = attackedEntities.get(uuid);
            if (lastAttackTime == null || (currentTime - lastAttackTime) > ATTACK_COOLDOWN_TICKS) {
                // 计算伤害
                double damage = 0;
                for (AttributeModifier modifier : getWeaponStack().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
                    damage += modifier.getAmount();
                }
                // 如果伤害为0，给予一个基础伤害（防止无伤害）
                if (damage <= 0) damage = 2.0;

                // 创建伤害源（使用所有者或通用）
                DamageSource source;
                if (owner != null) {
                    source = this.damageSources().thrown(this, owner);
                } else {
                    source = this.damageSources().generic();
                }

                // 造成伤害
                living.hurt(source, (float) damage);
                attackedEntities.put(uuid, currentTime);

                // 触发“天降正义”进度（仅当所有者存在且是服务端玩家）
                if (owner instanceof ServerPlayer serverPlayer) {
                    var adv = serverPlayer.server.getAdvancements().getAdvancement(ModUtils.fa("weapon_plunge_attack"));
                    if (adv != null) {
                        // 检查是否已解锁，避免重复触发（award 内部会处理）
                        serverPlayer.getAdvancements().award(adv, "plunge_attack");
                    }
                }

                // 可选：播放击中音效
                // this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                //         SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    /**
     * 获取所有者玩家（服务端专用）
     */
    private ServerPlayer getOwnerPlayer() {
        if (this.level().isClientSide()) return null;
        UUID uuid = this.entityData.get(DATA_OWNER).orElse(null);
        if (uuid == null) return null;
        if (this.level().getServer() == null) return null;
        Player player = this.level().getServer().getPlayerList().getPlayer(uuid);
        if (player instanceof ServerPlayer) return (ServerPlayer) player;
        return null;
    }

    /**
     * 设置武器和所有者
     *
     * @param stack 物品
     * @param pos   放置位置
     * @param yaw   水平旋转
     * @param pitch 垂直旋转
     * @param owner 所有者玩家
     */
    public void setWeapon(ItemStack stack, BlockPos pos, float yaw, float pitch, Player owner) {
        this.setWeaponStack(stack.copy());
        this.setCustomName(stack.getDisplayName());
        this.rotationYaw = (int) yaw;
        this.rotationPitch = (int) pitch;
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);
        this.setYRot(yaw);
        this.setXRot(pitch);
        this.attackedEntities.clear();

        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.entityData.set(DATA_OWNER, Optional.of(owner.getUUID()));
        } else {
            this.ownerUUID = null;
            this.entityData.set(DATA_OWNER, Optional.empty());
        }
    }

    // ----- 交互拾取 -----
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.level().isClientSide()) {
            return InteractionResult.PASS;
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

        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, weaponStack.copy());
        } else if (!player.getInventory().add(weaponStack.copy())) {
            player.drop(weaponStack.copy(), false);
        }
        playSound(SoundEvents.ITEM_PICKUP);
        this.discard();
        return InteractionResult.SUCCESS;
    }

    // ----- 数据持久化 -----
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_WEAPON, ItemStack.EMPTY);
        this.entityData.define(DATA_OWNER, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Weapon")) {
            ItemStack stack = ItemStack.of(tag.getCompound("Weapon"));
            setWeaponStack(stack);
        }
        if (tag.hasUUID("Owner")) {
            UUID uuid = tag.getUUID("Owner");
            this.ownerUUID = uuid;
            this.entityData.set(DATA_OWNER, Optional.of(uuid));
        } else {
            this.ownerUUID = null;
            this.entityData.set(DATA_OWNER, Optional.empty());
        }
        rotationYaw = tag.getInt("RotationYaw");
        rotationPitch = tag.getInt("RotationPitch");
        verticalSpeed = tag.getDouble("VerticalSpeed");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (!getWeaponStack().isEmpty()) {
            tag.put("Weapon", getWeaponStack().save(new CompoundTag()));
        }
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putInt("RotationYaw", rotationYaw);
        tag.putInt("RotationPitch", rotationPitch);
        tag.putDouble("VerticalSpeed", verticalSpeed);
    }

    // ----- 网络同步 -----
    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    // ----- 物品访问 -----
    public ItemStack getWeaponStack() {
        return this.entityData.get(DATA_WEAPON);
    }

    public void setWeaponStack(ItemStack stack) {
        this.entityData.set(DATA_WEAPON, stack);
    }

    // ----- 碰撞箱与交互 -----
    @Override
    public AABB getBoundingBoxForCulling() {
        return new AABB(this.getX() - 0.3, this.getY() - 0.1, this.getZ() - 0.3, this.getX() + 0.3, this.getY() + 0.6, this.getZ() + 0.3);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 0.25f;
    }
}
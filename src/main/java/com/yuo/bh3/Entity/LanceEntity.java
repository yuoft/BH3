package com.yuo.bh3.Entity;

import com.yuo.bh3.Items.skill.LanceSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class LanceEntity extends Entity {

    public static final int STATE_FLYING = 0;
    public static final int STATE_IMPACT = 1;
    /**
     * 每 tick 的重力（非常小，几乎直线飞行）
     */
    public static final double GRAVITY = 0.003;
    /**
     * 最长存活时间（tick），超时自爆
     */
    public static final int MAX_LIFE = 200;
    /** 直线飞行距离（超过后开始增加重力） */
    public static final double MAX_LINEAR_DISTANCE = 32.0;
    /** 基础重力 */
    public static final double BASE_GRAVITY = 0.003;
    /** 最大重力（距离足够远后） */
    public static final double MAX_GRAVITY = 0.08;
    /** 从直线到最大重力之间的过渡距离 */
    public static final double GRAVITY_RAMP_DISTANCE = 16.0;

    /** 起点（第一次 tick 时记录） */
    private Vec3 startPos = null;
    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.INT);
    /**
     * 已经伤害过的实体 ID，避免每 tick 重复命中
     */
    private final Set<Integer> hitEntities = new HashSet<>();
    private int fallTime = 0;

    public LanceEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    public LanceEntity(Level level, Player owner) {
        super(BH3EntityTypes.LANCE.get(), level);
        this.setOwnerUUID(owner.getUUID());
        this.setWeapon(owner.getUseItem());
        this.setState(STATE_FLYING);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        // 撞击后短暂保留，然后消失
        if (this.getState() == STATE_IMPACT) {
            if (tickCount > 20 + this.fallTime) this.discard();
            return;
        }

        // 首次 tick 记录起点
        if (startPos == null) {
            startPos = this.position();
        }

        Vec3 motion = this.getDeltaMovement();
        Vec3 prevPos = this.position();
        Vec3 nextPos = prevPos.add(motion);
        if (!level.isClientSide) {
            // 1. 实体碰撞
            Vec3 ray = nextPos.subtract(prevPos);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    level, this,
                    prevPos, nextPos,
                    this.getBoundingBox().expandTowards(ray).inflate(0.5),
                    e -> e != this && e.isAlive() && e != getOwner(),
                    0.5f);

            if (entityHit != null) {
                Entity target = entityHit.getEntity();
                if (!hitEntities.contains(target.getId())) {
                    hitEntities.add(target.getId());
                    if (target instanceof LivingEntity living) {
                       living.hurt(getOwner().damageSources().playerAttack(getOwner()), (float) getOwner().getAttributeValue(Attributes.ATTACK_DAMAGE));
                    }
                }
            }

            // 2. 方块碰撞
            BlockHitResult blockHit = level.clip(new ClipContext(prevPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            if (blockHit.getType() != HitResult.Type.MISS) {
                Vec3 hitPos = blockHit.getLocation();
                this.setPos(hitPos.x, hitPos.y, hitPos.z);
                onImpact(hitPos);
                this.fallTime = tickCount;
                return;
            }
        }

        // ★ 计算当前飞行距离
        double distance = this.position().distanceTo(startPos);

        // ★ 根据距离动态计算重力
        double gravity = computeGravity(distance);
        this.setPos(nextPos.x, nextPos.y, nextPos.z);
        this.setDeltaMovement(motion.x, motion.y - gravity, motion.z);

        // 超时自爆
        if (!level.isClientSide && tickCount > MAX_LIFE) {
            onImpact(this.position());
        }
    }

    /**
     * 根据飞行距离返回当前重力
     * - 距离 ≤ MAX_LINEAR_DISTANCE：基础重力（几乎直线）
     * - MAX_LINEAR_DISTANCE ~ MAX_LINEAR_DISTANCE + GRAVITY_RAMP_DISTANCE：线性增大
     * - 超过 MAX_LINEAR_DISTANCE + GRAVITY_RAMP_DISTANCE：最大重力
     */
    private double computeGravity(double distance) {
        if (distance <= MAX_LINEAR_DISTANCE) {
            return BASE_GRAVITY;
        }
        double t = Math.min(1.0, (distance - MAX_LINEAR_DISTANCE) / GRAVITY_RAMP_DISTANCE);
        return BASE_GRAVITY + (MAX_GRAVITY - BASE_GRAVITY) * t;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("entity.bh3.lance", getWeapon().getDisplayName());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_WEAPON, ItemStack.EMPTY);
        this.entityData.define(DATA_OWNER, Optional.empty());
        this.entityData.define(DATA_STATE, STATE_FLYING);
    }

    public ItemStack getWeapon() {
        return this.entityData.get(DATA_WEAPON);
    }

    public void setWeapon(ItemStack stack) {
        this.entityData.set(DATA_WEAPON, stack);
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(DATA_OWNER);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(DATA_OWNER, Optional.of(uuid));
    }

    public int getState() {
        return this.entityData.get(DATA_STATE);
    }

    public void setState(int state) {
        this.entityData.set(DATA_STATE, state);
    }

    public Player getOwner() {
        Optional<UUID> uuid = getOwnerUUID();
        return uuid.map(value -> this.level().getPlayerByUUID(value)).orElse(null);
    }

    private void onImpact(Vec3 pos) {
        if (this.getState() == STATE_IMPACT) return;
        this.setState(STATE_IMPACT);
        this.setDeltaMovement(Vec3.ZERO);

        if (this.level() instanceof ServerLevel serverLevel) {
            LanceSkill.doImpact(serverLevel, getOwner(), pos);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
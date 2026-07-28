package com.yuo.bh3.Entity;

import com.yuo.bh3.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlacedWeaponEntity extends Entity {
    private static final float GRAVITY = 0.04f; //重力
    private static final double OFFSET_BOTTOM = 0.25; // 实体中心到底部的距离
    private static final long ATTACK_COOLDOWN_TICKS = 20; // 1 秒冷却
    private static final float EXPLOSION_TRIGGER_SPEED = -3.2F; // 对应 128 格
    private float ringAngle = 0; // 环旋转角度
    private static final int RING_PARTICLE_COUNT = 12; // 每环粒子数
    private static final int MAX_FALL_BLOCK_COUNT = 20; //最大实体方块数

    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(PlacedWeaponEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(PlacedWeaponEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> DATA_VERTICAL_SPEED = SynchedEntityData.defineId(PlacedWeaponEntity.class, EntityDataSerializers.FLOAT);
    // 攻击冷却映射（实体UUID -> 上次攻击的游戏刻）
    private final Map<UUID, Long> attackedEntities = new HashMap<>();
    private int rotationYaw = 0; //实体角度
    private int rotationPitch = 0;
    private UUID ownerUUID; // 所有者的 UUID

    public PlacedWeaponEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true); // 我们手动控制重力
    }

    @SuppressWarnings("removal")
    @Override
    public void tick() {
        super.tick();
        Level level = this.level();

        if (level.isClientSide) {
            double y = this.getDeltaMovement().y();
            addFallParticle(level, getFallSpeed());
            return;
        }

        if (getWeaponStack().isEmpty()) {
            discard();
            return;
        }

        // 应用重力
        addFallSpeed(-GRAVITY);
        // 限制最大速度（可选）
        if (getFallSpeed() < -10.0f) setFallSpeed(-10.0f);

        // 移动并处理碰撞
        this.move(MoverType.SELF, new Vec3(0, getFallSpeed(), 0));

        // 落地检测
        if (this.onGround() && getFallSpeed() < 0) {
            if (getFallSpeed() < EXPLOSION_TRIGGER_SPEED) {
                upSpeedGround(level, (float) -getFallSpeed(), this.getOwnerPlayer());
            }
            setFallSpeed(0);
        }

        // 下落攻击
        if (getFallSpeed() < 0 && !onGround()) {
            attackNearbyEntities();
        }
    }

    /**
     * 一定速度落地时的效果
     * 1.爆炸（破坏方块，掉落物品）
     * 2. 击飞周围生物（增强效果）
     * 3. 方块碎片飞散效果（使用 FallingBlockEntity 模拟重力方块）
     * 4. 触发进度《真·天降正义》
     */
    private void upSpeedGround(Level level, float speed, Player player) {
        float explosionPower = Math.max(speed * 3.5f + 3, 2.0f);
        level.explode(player, this.getX(), this.getY(), this.getZ(), explosionPower, true, ExplosionInteraction.BLOCK);


        double radius = speed * 1.25d; //击飞范围
        AABB explodeAabb = new AABB(this.getX() - radius, this.getY() - radius, this.getZ() - radius, this.getX() + radius, this.getY() + radius, this.getZ() + radius);
        List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, explodeAabb);
        for (LivingEntity living : entityList) {
            if (living == player || !living.isAlive()) continue;
            double dx = living.getX() - this.getX();
            double dz = living.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance < radius) {
                double strength = 1.5 * (1 - distance / radius);
                living.setDeltaMovement(living.getDeltaMovement().add(dx / distance * strength, 0.25 + strength, dz / distance * strength));
                attackLiving(player, living);
            }
        }

        addGroundParticle(level);
        flyBlock(level, explosionPower);

        if (player instanceof ServerPlayer serverPlayer) {
            var adv = serverPlayer.server.getAdvancements().getAdvancement(ModUtils.fa("true_plunge_attack"));
            if (adv != null) {
                serverPlayer.getAdvancements().award(adv, "true_plunge_attack");
            }
        }
    }

    /**
     * 掀飞附近方块
     */
    private void flyBlock(Level level, float explosionPower){
        // ----- 生成爆炸范围外的方块碎片（不预缓存） -----
        // 计算爆炸破坏半径（近似为爆炸威力）
        float explosionRadius = explosionPower * 1.25f;
        int startRadius = (int) Math.ceil(explosionRadius) + 1; // 爆炸范围外开始
        int endRadius = (int) Math.ceil(explosionRadius) + 4;   // 爆炸范围外 3 格内

        BlockPos center = this.blockPosition();
        List<BlockPos> candidates = new ArrayList<>();

        // 遍历球形范围内的方块
        for (int x = -endRadius; x <= endRadius; x++) {
            for (int y = -endRadius; y <= endRadius; y++) {
                for (int z = -endRadius; z <= endRadius; z++) {
                    double dist = Math.sqrt(x*x + y*y + z*z);
                    if (dist >= startRadius && dist <= endRadius) {
                        BlockPos pos = center.offset(x, y, z);
                        // 检查Y差是否在3格内
                        if (Math.abs(pos.getY() - center.getY()) > 3) continue;
                        BlockState state = level.getBlockState(pos);
                        if (!state.isAir() && state.getBlock().defaultBlockState().isSolid()) {
                            // 检查是否暴露在空气中（表面）
                            boolean exposed = level.getBlockState(pos.above()).isAir() ||
                                    level.getBlockState(pos.below()).isAir() ||
                                    level.getBlockState(pos.north()).isAir() ||
                                    level.getBlockState(pos.south()).isAir() ||
                                    level.getBlockState(pos.east()).isAir() ||
                                    level.getBlockState(pos.west()).isAir();
                            if (!exposed) continue;
                            candidates.add(pos.immutable());
                        }
                    }
                }
            }
        }

        // 随机选取最多 20 个
        if (!candidates.isEmpty()) {
            Collections.shuffle(candidates); //随机打乱列表
            int maxFragments = Math.min(candidates.size(), MAX_FALL_BLOCK_COUNT);
            for (int i = 0; i < maxFragments; i++) {
                BlockPos pos = candidates.get(i);
                BlockState state = level.getBlockState(pos); // 方块未被破坏
                FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, state);
                double vx = (level.random.nextDouble() - 0.5) * 1.5;
                double vy = level.random.nextDouble() * 1.5;
                double vz = (level.random.nextDouble() - 0.5) * 1.5;
                fallingBlock.setDeltaMovement(vx, vy, vz);
                fallingBlock.time = 1;
                fallingBlock.setYRot(level.random.nextFloat() * 360);
                fallingBlock.setXRot(0);
                level.addFreshEntity(fallingBlock);
            }
        }
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

                attackLiving(owner, living);
                attackedEntities.put(uuid, currentTime);

                // 触发“天降正义”进度（仅当所有者存在且是服务端玩家）
                if (owner instanceof ServerPlayer serverPlayer) {
                    var adv = serverPlayer.server.getAdvancements().getAdvancement(ModUtils.fa("weapon_plunge_attack"));
                    if (adv != null) {
                        // 检查是否已解锁，避免重复触发（award 内部会处理）
                        serverPlayer.getAdvancements().award(adv, "weapon_plunge_attack");
                    }
                }
            }
        }
    }

    /**
     * 攻击
     * @param owner 玩家
     * @param living 目标
     */
    private void attackLiving(Player owner, LivingEntity living) {
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
    }

    /**
     * 下落时的粒子
     */
    private void addFallParticle(Level level, double speed){
        if (speed < EXPLOSION_TRIGGER_SPEED * 0.1) {
            if (this.tickCount % 2 == 0) {
                double x = this.getX();
                double y = this.getY() + 0.3; // 从实体中部稍上
                double z = this.getZ();
                // 生成多个粒子形成拖尾效果
                for (int i = 0; i < 3; i++) {
                    double ox = (level.random.nextDouble() - 0.5) * 0.3;
                    double oy = (level.random.nextDouble() - 0.5) * 0.3;
                    double oz = (level.random.nextDouble() - 0.5) * 0.3;
                    // 使用 CRIT 或 SWEEP_ATTACK 粒子
                    level.addParticle(ParticleTypes.SWEEP_ATTACK, x + ox, y + oy, z + oz, 0, 0, 0);
                }
            }
        }

        // ========== 新增：动能环（围绕武器的光环） ==========
        if (speed < EXPLOSION_TRIGGER_SPEED * 0.25) { // 速度超过阈值时显示光环
            // 更新旋转角度
            ringAngle += 0.2F; // 每 tick 旋转 0.2 弧度
            double radius = 0.6; // 环半径
            double centerX = this.getX();
            double centerY = this.getY() + 0.2; // 略微偏下
            double centerZ = this.getZ();

            // 生成环状粒子（每 tick 生成一圈）
            for (int i = 0; i < RING_PARTICLE_COUNT; i++) {
                double angle = ringAngle + (i * 2 * Math.PI / RING_PARTICLE_COUNT);
                double px = centerX + radius * Math.cos(angle);
                double pz = centerZ + radius * Math.sin(angle);
                // 添加带颜色的粒子（使用 ENTITY_EFFECT 并设置 RGB）
                // 或者使用 END_ROD（白色光点）
                level.addParticle(ParticleTypes.END_ROD, px, centerY, pz, 0, 0, 0);
                // 可选：再生成一个小粒子在下方形成双层环
                if (i % 2 == 0) {
                    level.addParticle(ParticleTypes.END_ROD, px, centerY - 0.15, pz, 0, 0, 0);
                }
            }
        }
    }

    /**
     * 落地时的粒子（服务端广播版）
     */
    private void addGroundParticle(Level level) {
        // 使用 sendParticles 广播粒子，而不是 addParticle
        if (level instanceof ServerLevel serverLevel) {
            // ========== 冲击波环 ==========
            for (int ring = 0; ring < 4; ring++) {
                double radiusStart = 1.0 + ring * 2.0;
                double radiusEnd = radiusStart + 1.5;
                int particlesPerRing = 30 + ring * 15;
                for (int i = 0; i < particlesPerRing; i++) {
                    double angle = (i / (double) particlesPerRing) * 2 * Math.PI;
                    double r = radiusStart + (level.random.nextDouble() * (radiusEnd - radiusStart));
                    double px = this.getX() + r * Math.cos(angle);
                    double pz = this.getZ() + r * Math.sin(angle);
                    double py = this.getY() + 0.1 + level.random.nextDouble() * 0.5;

                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                            px, py, pz, 1,
                            (r - radiusStart) * 0.4, 0.1, (r - radiusStart) * 0.4, 0);
                }
            }

            // ========== 大环（END_ROD） ==========
            int ringParticles = 48;
            double bigRadius = 6.0;
            for (int i = 0; i < ringParticles; i++) {
                double angle = i * 2 * Math.PI / ringParticles;
                double px = this.getX() + bigRadius * Math.cos(angle);
                double pz = this.getZ() + bigRadius * Math.sin(angle);
                double py = this.getY() + 0.1;
                serverLevel.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, 0, 0, 0, 0);
            }

            // ========== 内圈环（ELECTRIC_SPARK） ==========
            for (int i = 0; i < 24; i++) {
                double angle = i * 2 * Math.PI / 24;
                double r = 3.0;
                double px = this.getX() + r * Math.cos(angle);
                double pz = this.getZ() + r * Math.sin(angle);
                double py = this.getY() + 0.1;
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 1, 0, 0, 0, 0);
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
        this.entityData.define(DATA_VERTICAL_SPEED, 0f);
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

    public float getFallSpeed(){
        return this.entityData.get(DATA_VERTICAL_SPEED);
    }

    public void setFallSpeed(float speed){
        this.entityData.set(DATA_VERTICAL_SPEED, speed);
    }

    /**
     * 增加速度
     * @param speed
     */
    public void addFallSpeed(float speed){
        this.setFallSpeed(getFallSpeed() + speed);
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
package com.yuo.bh3.Items.skill;

import com.yuo.bh3.Entity.SickleEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SickleSkill {

    /**
     * 拉取阶段持续时间
     */
    public static final int PULL_DURATION = 10;
    /**
     * 横扫阶段持续时间
     */
    public static final int SLASH_DURATION = 10;
    /**
     * 在横扫的第几 tick 造成伤害
     */
    public static final int SLASH_HIT_TICK = 2;

    /**
     * 拉取扇形半径
     */
    public static final double PULL_RANGE = 8.0;
    /**
     * 拉取扇形角度
     */
    public static final double PULL_ANGLE = 120.0;
    /**
     * 拉到玩家前方多少格
     */
    public static final double PULL_TARGET_DIST = 2.0;
    /**
     * 拉取强度
     */
    public static final double PULL_STRENGTH = 1.5;

    /**
     * 横扫半径
     */
    public static final double SLASH_RANGE = 5.0;
    /**
     * 横扫扇形角度
     */
    public static final double SLASH_ANGLE = 180.0;
    /**
     * 横扫伤害
     */
    public static final float SLASH_DAMAGE = 15f;

    /**
     * 消耗崩坏能
     */
    public static final float HONKAI_COST = 40f;
    /**
     * 扫掠起始角偏移（与渲染器保持一致）
     */
    public static final float SCAN_START_OFFSET = 30f;
    /**
     * 扫掠总角度
     */
    public static final float SCAN_TOTAL_ANGLE = 120f;
    /**
     * 扫掠半径（与渲染器一致）
     */
    public static final double SCAN_RADIUS = 4.5f;
    private static final Random RAND = new Random();

    public static void spawn(ServerPlayer player, ItemStack stack) {
        SickleEntity entity = new SickleEntity(player.level(), player);
        entity.setWeapon(stack);
        player.level().addFreshEntity(entity);
    }

    // ===================== 拉取阶段 =====================
    public static void tickPull(Player player, int tick) {
        Vec3 look = player.getLookAngle();
        Vec3 eye = player.position().add(0, player.getBbHeight() * 0.5, 0);

        // 扇形余弦值
        double cosAngle = Math.cos(Math.toRadians(PULL_ANGLE * 0.5));

        AABB box = player.getBoundingBox().inflate(PULL_RANGE, 4, PULL_RANGE);

        for (LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {

            Vec3 toMob = mob.position().subtract(eye);
            double dist = toMob.length();
            if (dist > PULL_RANGE || dist < 0.1) continue;

            // 扇形判定
            Vec3 dir = toMob.normalize();
            double dot = dir.dot(look);
            if (dot < cosAngle) continue;

            // 目标位置：玩家前方 PULL_TARGET_DIST 格
            Vec3 target = player.position().add(look.scale(PULL_TARGET_DIST));
            Vec3 toTarget = target.subtract(mob.position());
            double distToTarget = toTarget.length();

            if (distToTarget < 0.5) {
                // 已经到位，减速
                mob.setDeltaMovement(mob.getDeltaMovement().scale(0.4));
                mob.hurtMarked = true;
                continue;
            }

            // 拉力：距离越远拉得越快
            double strength = PULL_STRENGTH * Math.min(2.0, distToTarget / 3.0 + 0.5);
            Vec3 pull = toTarget.normalize().scale(strength);

            // 保留微小的 Y 速度，避免贴地
            mob.setDeltaMovement(pull.x, Math.min(pull.y + 0.05, 0.3), pull.z);
            mob.hurtMarked = true;
            mob.fallDistance = 0;
        }

        // 拉取时的音效反馈
        if (tick % 4 == 0) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5f, 1.8f);
        }

        // ===================== 吸取粒子 =====================
        if (player.level() instanceof ServerLevel serverLevel) {
            // 每 tick 生成一批，从扇形范围内的敌人位置向玩家汇聚
            for (LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {

                Vec3 mobPos = mob.position().add(0, mob.getBbHeight() * 0.5, 0);
                Vec3 toPlayer = eye.subtract(mobPos);
                double dist = toPlayer.length();
                if (dist < 0.1) continue;

                Vec3 dir = toPlayer.normalize();
                // 沿 mob → player 方向撒 2 个粒子，速度朝玩家
                double speed = 0.6 + 0.1 * tick;   // 越拉越快

                for (int i = 0; i < 2; i++) {
                    double px = mobPos.x + (RAND.nextDouble() - 0.5) * 0.5;
                    double py = mobPos.y + (RAND.nextDouble() - 0.5) * 0.5;
                    double pz = mobPos.z + (RAND.nextDouble() - 0.5) * 0.5;

                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, py, pz, 1, dir.x * speed, dir.y * speed, dir.z * speed, 0.0);
                }
            }

            // 玩家身前再撒一圈"汇聚"粒子，增强吸入感
            Vec3 pullCenter = player.position().add(look.scale(PULL_TARGET_DIST)).add(0, player.getBbHeight() * 0.5, 0);

            for (int i = 0; i < 6; i++) {
                double angle = RAND.nextDouble() * Math.PI * 2;
                double r = 1.5;
                double px = pullCenter.x + Math.cos(angle) * r;
                double pz = pullCenter.z + Math.sin(angle) * r;
                double py = pullCenter.y + (RAND.nextDouble() - 0.5) * 0.5;

                // 从外圈朝中心飞的粒子
                double vx = (pullCenter.x - px) * 1.5;
                double vy = 0.1;
                double vz = (pullCenter.z - pz) * 1.5;

                serverLevel.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, vx, vy, vz, 0.0);
            }
        }
    }

    // ===================== 横扫阶段 =====================

    public static void startSlash(Player player) {
        // 横扫起手音效
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.2f, 1.0f);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_RIPTIDE_2, SoundSource.PLAYERS, 1.0f, 1.2f);
    }

    public static void doSlashDamage(Player player) {
        Vec3 look = player.getLookAngle();
        Vec3 eye = player.position().add(0, player.getBbHeight() * 0.5, 0);

        double cosAngle = Math.cos(Math.toRadians(SLASH_ANGLE * 0.5));

        AABB box = player.getBoundingBox().inflate(SLASH_RANGE, 3, SLASH_RANGE);
        if (player.level() instanceof ServerLevel serverLevel) {
            float playerYaw = player.getYRot();
            int arcSteps = 24;   // 沿弧线撒 24 个点

            for (int i = 0; i < arcSteps; i++) {
                float t = i / (float) (arcSteps - 1);
                float scanYaw = playerYaw + SickleSkill.SCAN_START_OFFSET + t * SickleSkill.SCAN_TOTAL_ANGLE;

                // 世界方向：+Z 绕 Y 轴旋转 -scanYaw
                double rad = Math.toRadians(-scanYaw + 90f);
                double dx = Math.sin(rad);
                double dz = Math.cos(rad);

                double px = player.getX() + dx * SCAN_RADIUS;
                double pz = player.getZ() + dz * SCAN_RADIUS;
                double py = player.getY() + .6f;

                // 主刀光粒子
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, px, py, pz, 1, 0, 0, 0, 0.0);
            }
        }

        for (LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {

            Vec3 toMob = mob.position().subtract(eye);
            double dist = toMob.length();
            if (dist > SLASH_RANGE || dist < 0.1) continue;

            Vec3 dir = toMob.normalize();
            double dot = dir.dot(look);
            if (dot < cosAngle) continue;

            mob.invulnerableTime = 0; //无敌帧重置为0
            mob.hurt(player.damageSources().playerAttack(player), SLASH_DAMAGE);
            mob.hurtMarked = true;
        }

        // 击退音效
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.8f, 1.5f);
    }
}
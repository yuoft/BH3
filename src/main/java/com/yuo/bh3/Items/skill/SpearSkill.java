package com.yuo.bh3.Items.skill;

import com.yuo.bh3.BH3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SpearSkill {

    /**
     * 冲刺持续时间（tick）
     */
    public static final int DASH_DURATION = 10;
    /**
     * 冲刺速度（格/tick）
     */
    public static final double DASH_SPEED = 2;
    /**
     * 消耗崩坏能
     */
    public static final float HONKAI_COST = 35f;

    /**
     * 沿冲刺方向的击退
     */
    public static final double KNOCKBACK = 1.5;
    public static final double KNOCKBACK_UP = 0.3;

    /**
     * 可破坏方块的最大硬度（草/树叶/花 都是 0~0.5）
     */
    public static final float MAX_HARDNESS = 1.0f;

    private static final String TAG_TICKS = "bh3.spear_dash_ticks";
    private static final String TAG_YAW = "bh3.spear_dash_yaw";

    /**
     * 玩家 UUID → 已经伤害过的实体 ID
     */
    private static final Map<UUID, Set<Integer>> HIT_ENTITIES = new HashMap<>();

    public static void startServer(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData();
        tag.putInt(TAG_TICKS, DASH_DURATION);
        tag.putFloat(TAG_YAW, player.getYRot());

        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(look.x * DASH_SPEED * 1.5, look.y * DASH_SPEED * 1.5, look.z * DASH_SPEED * 1.5);
        player.hurtMarked = true;
        player.hasImpulse = true;
        player.fallDistance = 0;

        HIT_ENTITIES.put(player.getUUID(), new HashSet<>());

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_RIPTIDE_1, SoundSource.PLAYERS, 1.0f, 1.2f);
    }

    // ===================== 破坏方块 =====================
    private static void breakBlocks(Player p, Vec3 forward) {
        AABB box = p.getBoundingBox().expandTowards(forward.x * 3, forward.y * 3, forward.z * 3)      // ★ 前方延伸 4 格
                .inflate(1.2, 1, 1.2); ;
        BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = p.level().getBlockState(pos);
            if (state.isAir()) continue;

            float hardness = state.getDestroySpeed(p.level(), pos);
            // 只破坏低硬度方块（草、树叶、花等）
            if (hardness < 0 || hardness > MAX_HARDNESS) continue;

            p.level().destroyBlock(pos, false, p);
        }
    }

    // ===================== 攻击生物 =====================
    private static void attackEntities(Player p, Vec3 forward) {
        Set<Integer> hit = HIT_ENTITIES.computeIfAbsent(p.getUUID(), k -> new HashSet<>());

        AABB box = p.getBoundingBox().inflate(1.5, 1, 1.5);

        for (LivingEntity e : p.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != p && e.isAlive())) {

            if (hit.contains(e.getId())) continue;
            hit.add(e.getId());

            e.invulnerableTime = 0;
            e.hurt(p.damageSources().playerAttack(p), (float) p.getAttributeValue(Attributes.ATTACK_DAMAGE));

            // 沿冲刺方向击退
            Vec3 kb = forward.scale(KNOCKBACK).add(0, KNOCKBACK_UP, 0);
            e.setDeltaMovement(e.getDeltaMovement().add(kb));
            e.hurtMarked = true;

            p.level().playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 0.8f, 1.2f);
        }
    }

    // ===================== 粒子轨迹 =====================
    private static void spawnTrailParticles(ServerLevel level, Player p, Vec3 forward) {
        double x = p.getX();
        double y = p.getY() + p.getBbHeight() * 0.5;
        double z = p.getZ();

        // ---------- 身后尾迹（保持不变）----------
        for (int i = 0; i < 4; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.6;
            double offsetY = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.6;

            level.sendParticles(ParticleTypes.END_ROD, x + offsetX - forward.x * 0.3, y + offsetY, z + offsetZ - forward.z * 0.3, 1, 0, 0, 0, 0.0);
        }

        // ---------- 前方动能环 ----------
        spawnKineticRings(level, x, y, z, forward, p.tickCount);
    }

    /**
     * 在玩家前方生成多个同心动能环（垂直于冲刺方向）
     */
    private static void spawnKineticRings(ServerLevel level, double px, double py, double pz, Vec3 forward, int tick) {

        // ★ 构造垂直于 forward 的两个正交基向量
        Vec3 right = new Vec3(-forward.z, 0, forward.x);
        if (right.lengthSqr() < 1e-6) right = new Vec3(1, 0, 0);
        right = right.normalize();

        // up = forward × right（保证两两正交）
        Vec3 up = forward.cross(right).normalize();

        // 每 tick 生成 2 个环：近 + 远
        final int ringCount = 2;
        final int pointsPerRing = 14;   // 每环 14 个点

        // 环旋转动画（让点顺着圆周流动）
        double spin = tick * 0.35;

        for (int r = 0; r < ringCount; r++) {
            // 距离：第 0 个环近，第 1 个环远
            double distance = 0.6 + r * 0.8;
            // 半径：越远的环越大（向外扩散感）
            double radius = 0.25 + r * 0.35;

            // 环中心
            double cx = px + forward.x * distance;
            double cy = py;
            double cz = pz + forward.z * distance;

            // 环上的 N 个点
            for (int i = 0; i < pointsPerRing; i++) {
                double angle = i * (Math.PI * 2) / pointsPerRing + spin;
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);

                // 在 right-up 平面内画圆
                double ox = right.x * cos + up.x * sin;
                double oy = right.y * cos + up.y * sin;
                double oz = right.z * cos + up.z * sin;

                double ex = cx + ox * radius;
                double ey = cy + oy * radius;
                double ez = cz + oz * radius;

                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, ex, ey, ez, 1, 0, 0, 0, 0.0);
            }

            // 环中心加一个明亮的"动能核心"
            level.sendParticles(ParticleTypes.SONIC_BOOM, cx, cy, cz, 1, 0, 0, 0, 0.0);
        }
    }

    @Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerEvents {

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.START) return;
            Player p = event.player;
            if (p.level().isClientSide) return;

            CompoundTag tag = p.getPersistentData();
            int ticks = tag.getInt(TAG_TICKS);
            if (ticks <= 0) return;

            tag.putInt(TAG_TICKS, ticks - 1);

            float yaw = tag.getFloat(TAG_YAW);
            double rad = Math.toRadians(yaw);
            Vec3 forward = new Vec3(-Math.sin(rad), 0, Math.cos(rad));
            // 破坏沿途低硬度方块
            breakBlocks(p, forward);

            // 冲刺位移：水平沿 yaw，Y 保持不下落
            p.setDeltaMovement(forward.x * DASH_SPEED, forward.y * DASH_SPEED, forward.z * DASH_SPEED);
            p.hurtMarked = true;
            p.hasImpulse = true;
            p.fallDistance = 0;


            // 攻击沿途生物
            attackEntities(p, forward);

            // 粒子轨迹
            if (p.level() instanceof ServerLevel sl) {
                spawnTrailParticles(sl, p, forward);
            }

            // 结束
            if (ticks == 1) {
                HIT_ENTITIES.remove(p.getUUID());
            }
        }
    }
}
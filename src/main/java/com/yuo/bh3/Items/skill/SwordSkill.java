package com.yuo.bh3.Items.skill;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Client.effect.LineSlashEffect;
import com.yuo.bh3.Client.effect.SlashEffectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SwordSkill {

    // ===================== 冲刺阶段参数 =====================
    /** 冲刺持续时间（tick） */
    public static final int DASH_DURATION = 15;
    /** 冲刺速度 */
    public static final double DASH_SPEED = 5.0 / DASH_DURATION;
    /** 冷却 */
    public static final int COOLDOWN = 80;

    // ===================== 几何参数 =====================
    /** 中心点前进比例：实际冲刺距离的 50% */
    public static final double CENTER_FORWARD_RATIO = 0.5;
    /** 中心点固定高度 */
    public static final double CENTER_UP = 1.5;
    /** 球半径比例 */
    public static final double RADIUS_RATIO = 0.5;
    /** 球半径最小值 */
    public static final double RADIUS_MIN = 1.5;
    /** 球半径最大值 */
    public static final double RADIUS_MAX = 6.0;

    // ===================== 次元斩阶段参数 =====================
    /** 次元斩持续时间（tick） */
    public static final int BURST_DURATION = 20;
    /** 次元斩每多少 tick 打一次 */
    public static final int BURST_HIT_INTERVAL = 4;
    /** 次元斩单次伤害 */
    public static final float BURST_HIT_DAMAGE = 5.0f;
    /** 向上小幅击退 */
    public static final double UP_KNOCKBACK = 0.25;

    // ===================== NBT Keys =====================
    private static final String TAG_TICKS        = "bh3.dash_ticks";
    private static final String TAG_YAW          = "bh3.dash_yaw";
    private static final String TAG_START_X      = "bh3.dash_sx";
    private static final String TAG_START_Y      = "bh3.dash_sy";
    private static final String TAG_START_Z      = "bh3.dash_sz";
    private static final String TAG_DIR_X        = "bh3.dash_dx";
    private static final String TAG_DIR_Z        = "bh3.dash_dz";

    private static final String TAG_BURST_TICKS  = "bh3.burst_ticks";
    private static final String TAG_BURST_CX     = "bh3.burst_cx";
    private static final String TAG_BURST_CY     = "bh3.burst_cy";
    private static final String TAG_BURST_CZ     = "bh3.burst_cz";
    private static final String TAG_BURST_RADIUS = "bh3.burst_radius";

    // ===================== 公共计算 =====================

    /**
     * 根据实际冲刺距离推导几何参数
     *
     * @return [centerForward, centerUp, radius]
     */
    public static double[] computeGeometry(double actualDistance) {
        double centerForward = actualDistance * CENTER_FORWARD_RATIO;
        double radius = clamp(actualDistance * RADIUS_RATIO, RADIUS_MIN, RADIUS_MAX);
        return new double[]{centerForward, CENTER_UP, radius};
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    // ===================== 服务端 =====================

    public static void startServer(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData();

        Vec3 start = player.position();
        Vec3 look = player.getLookAngle();
        Vec3 dir = new Vec3(look.x, 0, look.z);
        if (dir.lengthSqr() < 1e-6) dir = new Vec3(0, 0, 1);
        dir = dir.normalize();

        // 清掉可能残留的 burst 状态
        ServerEvents.clearBurstTag(tag);

        tag.putInt(TAG_TICKS, DASH_DURATION);
        tag.putFloat(TAG_YAW, player.getYRot());
        tag.putDouble(TAG_START_X, start.x);
        tag.putDouble(TAG_START_Y, start.y);
        tag.putDouble(TAG_START_Z, start.z);
        tag.putDouble(TAG_DIR_X, dir.x);
        tag.putDouble(TAG_DIR_Z, dir.z);

        player.setDeltaMovement(dir.x * DASH_SPEED, 0.15, dir.z * DASH_SPEED);
        player.hurtMarked = true;
        player.hasImpulse = true;

        // 冲刺音效（服务端广播给附近玩家）
        player.level().playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_RIPTIDE_1,
                SoundSource.PLAYERS, 1.0f, 1.2f);
    }

    @Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerEvents {

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.START) return;
            Player p = event.player;
            if (p.level().isClientSide) return;

            CompoundTag tag = p.getPersistentData();

            // ---- 优先处理次元斩阶段 ----
            int burstTicks = tag.getInt(TAG_BURST_TICKS);
            if (burstTicks > 0) {
                handleBurstTick(p, tag, burstTicks);
                return;
            }

            // ---- 冲刺阶段 ----
            int ticks = tag.getInt(TAG_TICKS);
            if (ticks <= 0) return;
            tag.putInt(TAG_TICKS, ticks - 1);

            float yaw = tag.getFloat(TAG_YAW);
            double rad = Math.toRadians(yaw);
            Vec3 forward = new Vec3(-Math.sin(rad), 0, Math.cos(rad));

            p.setDeltaMovement(forward.x * DASH_SPEED * 2,
                    p.getDeltaMovement().y,
                    forward.z * DASH_SPEED * 2);
            p.hurtMarked = true;
            p.hasImpulse = true;
            p.fallDistance = 0;

            // 冲刺结束 → 启动次元斩阶段
            if (ticks == 1) {
                startBurst(p, tag);
                clearDashTag(tag);
            }
        }

        /** 冲刺结束：切换到次元斩阶段 */
        private static void startBurst(Player p, CompoundTag tag) {
            double[] center = getSlashCenter(p, tag);

            tag.putDouble(TAG_BURST_CX, center[0]);
            tag.putDouble(TAG_BURST_CY, center[1]);
            tag.putDouble(TAG_BURST_CZ, center[2]);
            tag.putDouble(TAG_BURST_RADIUS, center[3]);
            tag.putInt(TAG_BURST_TICKS, BURST_DURATION);

            // 全服可听到的爆发音效（雷击）
            p.level().playSound(null,
                    center[0], center[1], center[2],
                    SoundEvents.TRIDENT_THUNDER,
                    SoundSource.PLAYERS, 1.2f, 1.0f);
        }

        /** 每 tick 处理次元斩期间的计时与伤害 */
        private static void handleBurstTick(Player p, CompoundTag tag, int burstTicks) {
            int elapsed = BURST_DURATION - burstTicks;

            // 每 BURST_HIT_INTERVAL tick 打一次
            if (elapsed % BURST_HIT_INTERVAL == 0) {
                double cx = tag.getDouble(TAG_BURST_CX);
                double cy = tag.getDouble(TAG_BURST_CY);
                double cz = tag.getDouble(TAG_BURST_CZ);
                double radius = tag.getDouble(TAG_BURST_RADIUS);

                burstHit(p, cx, cy, cz, radius);
            }

            int newTicks = burstTicks - 1;
            tag.putInt(TAG_BURST_TICKS, newTicks);

            if (newTicks <= 0) {
                clearBurstTag(tag);
            }
        }

        /** 单次次元斩伤害 */
        private static void burstHit(Player p,
                                     double cx, double cy, double cz,
                                     double radius) {
            AABB box = new AABB(
                    cx - radius, cy - radius, cz - radius,
                    cx + radius, cy + radius, cz + radius
            );

            for (LivingEntity e : p.level().getEntitiesOfClass(
                    LivingEntity.class, box,
                    e -> e != p && e.isAlive())) {

                // 精筛：真正在球内
                double ex = e.getX() - cx;
                double ey = e.getY() + e.getBbHeight() * 0.5 - cy;
                double ez = e.getZ() - cz;
                double dsq = ex * ex + ey * ey + ez * ez;
                if (dsq > radius * radius) continue;

                e.invulnerableTime = 0;
                e.hurt(p.damageSources().playerAttack(p), BURST_HIT_DAMAGE);

                // ★ 向上小幅击退
                e.setDeltaMovement(e.getDeltaMovement().add(0, UP_KNOCKBACK, 0));
                e.hurtMarked = true;
            }
        }

        /** 根据冲刺起点、方向和当前进度计算斩击中心 */
        private static double[] getSlashCenter(Player p, CompoundTag tag) {
            double sx = tag.getDouble(TAG_START_X);
            double sy = tag.getDouble(TAG_START_Y);
            double sz = tag.getDouble(TAG_START_Z);
            double dx = tag.getDouble(TAG_DIR_X);
            double dz = tag.getDouble(TAG_DIR_Z);

            double actualDist = Math.sqrt(
                    Math.pow(p.getX() - sx, 2) + Math.pow(p.getZ() - sz, 2));
            if (actualDist < 0.5) actualDist = 0.5;

            double[] geo = computeGeometry(actualDist);
            double centerForward = geo[0];
            double centerUp = geo[1];
            double radius = geo[2];

            double cx = sx + dx * centerForward;
            double cy = sy + centerUp;
            double cz = sz + dz * centerForward;

            return new double[]{cx, cy, cz, radius};
        }

        private static void clearDashTag(CompoundTag tag) {
            tag.remove(TAG_TICKS);
            tag.remove(TAG_YAW);
            tag.remove(TAG_START_X);
            tag.remove(TAG_START_Y);
            tag.remove(TAG_START_Z);
            tag.remove(TAG_DIR_X);
            tag.remove(TAG_DIR_Z);
        }

        private static void clearBurstTag(CompoundTag tag) {
            tag.remove(TAG_BURST_TICKS);
            tag.remove(TAG_BURST_CX);
            tag.remove(TAG_BURST_CY);
            tag.remove(TAG_BURST_CZ);
            tag.remove(TAG_BURST_RADIUS);
        }
    }

    // ===================== 客户端 =====================

    private static final Random RAND = new Random();

    private static final Map<UUID, int[]> DASHING = new HashMap<>();
    private static final Map<UUID, Vec3> DASH_START = new HashMap<>();
    private static final Map<UUID, Vec3> DASH_DIR = new HashMap<>();

    private static final Map<UUID, int[]> BURSTING = new HashMap<>();
    /** [cx, cy, cz, radius] */
    private static final Map<UUID, double[]> BURST_CENTER = new HashMap<>();

    public static void startClient(LocalPlayer player) {
        UUID id = player.getUUID();

        DASHING.put(id, new int[]{DASH_DURATION, DASH_DURATION});
        DASH_START.put(id, player.position());

        // 清掉残留的 burst 状态
        BURSTING.remove(id);
        BURST_CENTER.remove(id);

        Vec3 look = player.getLookAngle();
        Vec3 dir = new Vec3(look.x, 0, look.z);
        if (dir.lengthSqr() < 1e-6) dir = new Vec3(0, 0, 1);
        DASH_DIR.put(id, dir.normalize());

        // 本地冲刺音效（无网络延迟）
        player.playNotifySound(
                SoundEvents.TRIDENT_RIPTIDE_1,
                SoundSource.PLAYERS,
                1.0f,
                1.2f
        );
    }

    @Mod.EventBusSubscriber(modid = BH3.MOD_ID, value = Dist.CLIENT,
            bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.START) return;

            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            UUID id = player.getUUID();

            // ---- 优先处理次元斩阶段 ----
            int[] burst = BURSTING.get(id);
            if (burst != null) {
                spawnBurstPulse(id, player, burst[0], burst[1]);
                if (--burst[0] <= 0) {
                    BURSTING.remove(id);
                    BURST_CENTER.remove(id);
                }
                return;
            }

            // ---- 冲刺阶段 ----
            int[] dash = DASHING.get(id);
            if (dash == null) return;

            if (--dash[0] <= 0) {
                startBurstClient(id, player);
                DASHING.remove(id);
                DASH_START.remove(id);
                DASH_DIR.remove(id);
            }
        }
    }

    /** 冲刺结束 → 生成初始次元斩 + 进入 burst 状态 */
    private static void startBurstClient(UUID id, LocalPlayer player) {
        Vec3 start = DASH_START.get(id);
        Vec3 dir = DASH_DIR.get(id);
        if (start == null || dir == null) return;

        double actualDist = Math.sqrt(
                Math.pow(player.getX() - start.x, 2) +
                        Math.pow(player.getZ() - start.z, 2));
        if (actualDist < 0.5) actualDist = 0.5;

        double[] geo = computeGeometry(actualDist);
        double centerForward = geo[0];
        double centerUp = geo[1];
        double radius = geo[2];

        double cx = start.x + dir.x * centerForward;
        double cy = start.y + centerUp;
        double cz = start.z + dir.z * centerForward;

        BURST_CENTER.put(id, new double[]{cx, cy, cz, radius});
        BURSTING.put(id, new int[]{BURST_DURATION, BURST_DURATION});

        // 本地三层爆发音效
        if (player.level() instanceof ClientLevel cl) {
            cl.playLocalSound(cx, cy, cz,
                    SoundEvents.TRIDENT_THUNDER,
                    SoundSource.PLAYERS, 1.2f, 1.0f, false);

            cl.playLocalSound(cx, cy, cz,
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0f, 1.6f, false);

            cl.playLocalSound(cx, cy, cz,
                    SoundEvents.PLAYER_ATTACK_CRIT,
                    SoundSource.PLAYERS, 1.0f, 1.2f, false);
        }

        float baseYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
        spawnInitialBurst(cx, cy, cz, radius, baseYaw);
    }

    /** 冲刺结束时的大面积初始次元斩刀光（life 覆盖整个 burst 期） */
    private static void spawnInitialBurst(double cx, double cy, double cz,
                                          double radius, float baseYaw) {
        int planes = 5;
        int linesPerPlane = 6;
        double planeSpread = radius * 0.8;

        for (int p = 0; p < planes; p++) {
            float planeYaw = baseYaw + (p / (float) planes) * 180f
                    + (RAND.nextFloat() - 0.5f) * 20f;
            float planePitch = (RAND.nextFloat() - 0.5f) * 30f;
            float planeRoll = RAND.nextFloat() * 360f;

            double perpX = -Math.cos(Math.toRadians(planeYaw));
            double perpZ = -Math.sin(Math.toRadians(planeYaw));

            for (int l = 0; l < linesPerPlane; l++) {
                double offset = (l - (linesPerPlane - 1) * 0.5)
                        * (planeSpread / linesPerPlane);

                double px = cx + perpX * offset;
                double py = cy + (RAND.nextDouble() - 0.5) * 0.6;
                double pz = cz + perpZ * offset;

                float slashLength = (float) (radius * 2.0f + RAND.nextFloat() * 2.0f);
                float slashWidth = 0.06f + RAND.nextFloat() * 0.06f;

                float yaw = planeYaw + (RAND.nextFloat() - 0.5f) * 8f;
                float pitch = planePitch + (RAND.nextFloat() - 0.5f) * 8f;
                float roll = planeRoll + (RAND.nextFloat() - 0.5f) * 15f;

                float cr = 0.55f + RAND.nextFloat() * 0.25f;
                float cg = 0.75f + RAND.nextFloat() * 0.2f;

                LineSlashEffect slash = new LineSlashEffect()
                        .setPos(px, py, pz)
                        .setFacing(yaw, pitch, roll)
                        .setShape(slashLength, slashWidth)
                        .setColor(cr, cg, 1.0f, 0.95f)
                        .setLife(BURST_DURATION + RAND.nextInt(5))
                        .setAdditive(true);

                SlashEffectManager.spawn(slash);
            }
        }

        // 白色主斩，压在长线之上
        int mainCount = 4 + RAND.nextInt(4);
        for (int i = 0; i < mainCount; i++) {
            float yaw = baseYaw + (RAND.nextFloat() - 0.5f) * 60f;
            float pitch = (RAND.nextFloat() - 0.5f) * 30f;
            float roll = RAND.nextFloat() * 360f;

            double px = cx + (RAND.nextDouble() - 0.5) * radius * 0.6;
            double py = cy + (RAND.nextDouble() - 0.5) * radius * 0.6;
            double pz = cz + (RAND.nextDouble() - 0.5) * radius * 0.6;

            LineSlashEffect main = new LineSlashEffect()
                    .setPos(px, py, pz)
                    .setFacing(yaw, pitch, roll)
                    .setShape(
                            (float) (radius * 1.4f + RAND.nextFloat()),
                            0.15f + RAND.nextFloat() * 0.1f)
                    .setColor(1.0f, 1.0f, 1.0f, 1.0f)
                    .setLife(BURST_DURATION + RAND.nextInt(4))
                    .setAdditive(true);

            SlashEffectManager.spawn(main);
        }
    }

    /** burst 期间每 tick 补充少量流动刀光，维持"持续切割"的观感 */
    private static void spawnBurstPulse(UUID id, LocalPlayer player,
                                        int remaining, int total) {
        double[] c = BURST_CENTER.get(id);
        if (c == null) return;

        int pulseCount = 3 + RAND.nextInt(3);

        for (int i = 0; i < pulseCount; i++) {
            // 球内均匀分布
            double u = RAND.nextDouble();
            double v = RAND.nextDouble();
            double w = RAND.nextDouble();
            double theta = u * Math.PI * 2;
            double phi = Math.acos(2 * v - 1);
            double r = c[3] * Math.cbrt(w) * 0.8;

            double px = c[0] + r * Math.sin(phi) * Math.cos(theta);
            double py = c[1] + r * Math.cos(phi);
            double pz = c[2] + r * Math.sin(phi) * Math.sin(theta);

            float yaw = RAND.nextFloat() * 360f;
            float pitch = (RAND.nextFloat() - 0.5f) * 30f;
            float roll = RAND.nextFloat() * 360f;

            float slashLength = (float) (c[3] * 1.8f + RAND.nextFloat() * 1.5f);
            float slashWidth = 0.05f + RAND.nextFloat() * 0.06f;

            float cr = 0.5f + RAND.nextFloat() * 0.3f;
            float cg = 0.7f + RAND.nextFloat() * 0.25f;

            LineSlashEffect slash = new LineSlashEffect()
                    .setPos(px, py, pz)
                    .setFacing(yaw, pitch, roll)
                    .setShape(slashLength, slashWidth)
                    .setColor(cr, cg, 1.0f, 0.85f)
                    .setLife(6 + RAND.nextInt(4))
                    .setAdditive(true);

            SlashEffectManager.spawn(slash);
        }
    }
}
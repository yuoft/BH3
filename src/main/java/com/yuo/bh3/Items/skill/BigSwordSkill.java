package com.yuo.bh3.Items.skill;

import com.yuo.bh3.Entity.BigSwordEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class BigSwordSkill {

    public static final int CHARGE_MIN_TICKS = 20;
    public static final int CHARGE_MAX_TICKS = 600;
    public static final int HONKAI_COST  = 100;
    /**
     * 长方体基础长度（前方延伸多少格）
     */
    public static final double SLASH_LENGTH = 20.0;
    /**
     * 宽度相对长度的比例
     */
    public static final double WIDTH_RATIO = 0.5;
    /**
     * 高度相对长度的比例
     */
    public static final double HEIGHT_RATIO = 0.7;
    public static final double KNOCKBACK = 2.5;
    public static final double KNOCKBACK_UP = 1.2;
    public static final int SLASH_ANIM_TICKS = 8;
    private static final Random RAND = new Random();
    /**
     * 火焰燃烧时间（秒）
     */
    private static final int FIRE_SECONDS = 6;
    /**
     * 未破坏方块转岩浆块的概率
     */
    private static final double MAGMA_CHANCE = 0.35;
    /**
     * 边缘最小破坏概率（中心是 1.0，边缘是这个值）
     */
    private static final double EDGE_BREAK_MIN = 0.3;

    public static void spawn(ServerPlayer player, ItemStack stack) {
        BigSwordEntity entity = new BigSwordEntity(player.level(), player);
        entity.setWeapon(stack);
        player.level().addFreshEntity(entity);
    }

    /**
     * 服务端执行劈下伤害与破坏
     */
    public static void execute(ServerLevel level, Player player, int chargeTicks) {
        float progress = Math.min(1f, (float) chargeTicks / CHARGE_MAX_TICKS);

        float playerDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float damage = playerDamage * 0.5f + (playerDamage * 1.25f - playerDamage) * progress;
        double length = SLASH_LENGTH;
        double halfWidth = length * WIDTH_RATIO * 0.5;
        double halfHeight = length * HEIGHT_RATIO * 0.5;

        // 水平视线方向
        Vec3 look = player.getLookAngle();
        Vec3 forward = new Vec3(look.x, 0, look.z);
        if (forward.lengthSqr() < 1e-6) forward = new Vec3(0, 0, 1);
        forward = forward.normalize();

        // 右手方向
        Vec3 right = new Vec3(-forward.z, 0, forward.x);

        // 起点：玩家身体中心
        Vec3 origin = player.position().add(0, player.getBbHeight() * 0.25, 0);

        breakBlocks(level, player, origin, forward, right, length, halfWidth, halfHeight);
        damageAndKnockback(player, origin, forward, right, length, halfWidth, halfHeight, damage);

        // 音效
        Vec3 center = origin.add(forward.scale(length * 0.5));
        level.playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.5f, 0.8f);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.2f, 1.0f);
    }

    /**
     * 破坏长方体范围内的方块
     * - 中心区域：全部破坏
     * - 边缘区域：按概率破坏
     * - 未破坏的地表方块：一定概率转成岩浆块
     */
    private static void breakBlocks(ServerLevel level, Player player, Vec3 origin, Vec3 forward, Vec3 right, double length, double halfWidth, double halfHeight) {

        Vec3 center = origin.add(forward.scale(length * 0.5));
        int r = (int) Math.ceil(Math.max(length, Math.max(halfWidth, halfHeight)));
        BlockPos centerPos = BlockPos.containing(center);

        for (BlockPos pos : BlockPos.betweenClosed(centerPos.offset(-r, -r, -r), centerPos.offset(r, r, r))) {

            double dx = pos.getX() + 0.5 - origin.x;
            double dy = pos.getY() + 0.5 - origin.y;
            double dz = pos.getZ() + 0.5 - origin.z;

            double alongForward = dx * forward.x + dz * forward.z;
            double alongRight = dx * right.x + dz * right.z;
            double alongUp = dy;

            // 长方体范围判定
            if (alongForward < 0 || alongForward > length) continue;
            if (Math.abs(alongRight) > halfWidth) continue;
            if (Math.abs(alongUp) > halfHeight) continue;

            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;
            float hardness = state.getDestroySpeed(level, pos);
            if (hardness < 0) continue;

            // ★ 归一化到 [-1, 1] 的三轴坐标，用于计算"离中心多远"
            double nx = (alongForward - length * 0.5) / (length * 0.5);
            double ny = alongRight / halfWidth;
            double nz = alongUp / halfHeight;
            double normDist = Math.sqrt(nx * nx + ny * ny + nz * nz);
            double d = Math.min(1.0, normDist);   // 0 = 中心，1 = 边缘

            // ★ 破坏概率：中心 100%，边缘 EDGE_BREAK_MIN
            double breakChance = 1.0 - (1.0 - EDGE_BREAK_MIN) * d;

            if (RAND.nextDouble() < breakChance) {
                // 破坏
                level.destroyBlock(pos, false, player);
            } else {
                // ★ 未破坏 → 部分转岩浆块
                // 只处理暴露在地表的方块（上方是空气），避免地下深处变岩浆
                BlockState above = level.getBlockState(pos.above());
                boolean solid = level.getBlockState(pos).isSolid();
                if (above.isAir() && RAND.nextDouble() < MAGMA_CHANCE && solid) {
                    level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
                }
            }
        }
    }

    /**
     * 对长方体范围内生物造成伤害并击飞，附加燃烧
     */
    private static void damageAndKnockback(Player player, Vec3 origin, Vec3 forward, Vec3 right, double length, double halfWidth, double halfHeight, float damage) {

        Vec3 center = origin.add(forward.scale(length * 0.5));
        AABB box = new AABB(center.x - length, center.y - halfHeight, center.z - length, center.x + length, center.y + halfHeight, center.z + length);

        for (LivingEntity e : player.level().getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
            e.invulnerableTime = 0;
            e.hurt(player.damageSources().playerAttack(player), damage);

            e.setSecondsOnFire(FIRE_SECONDS);

            // 击退
            Vec3 kb = forward.scale(KNOCKBACK).add(0, KNOCKBACK_UP, 0);
            e.setDeltaMovement(e.getDeltaMovement().add(kb));
            e.hurtMarked = true;
        }
    }
}
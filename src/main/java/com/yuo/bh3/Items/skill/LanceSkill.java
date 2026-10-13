package com.yuo.bh3.Items.skill;

import com.yuo.bh3.Entity.LanceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class LanceSkill {

    /**
     * 最短蓄力 tick（少于这个无法投掷）
     */
    public static final int CHARGE_MIN = 5;

    /**
     * 爆炸半径
     */
    public static final double EXPLOSION_RADIUS = 6.0;
    /**
     * 击退
     */
    public static final double KNOCKBACK = 2.0;
    public static final double KNOCKBACK_UP = 1.0;

    /**
     * 消耗崩坏能
     */
    public static final float HONKAI_COST = 45f;

    // ===================== 投掷 =====================

    public static void spawn(ServerPlayer player, ItemStack stack) {
        Level level = player.level();
        LanceEntity entity = new LanceEntity(level, player);
        entity.setWeapon(stack);
        float yaw = player.getYRot();
        float pitch = player.getXRot();
        entity.setYRot(yaw);
        entity.setXRot(pitch);

        Vec3 look = player.getLookAngle();
        entity.setDeltaMovement(look.scale(1.5f));

        // 从眼睛位置出枪
        entity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        level.addFreshEntity(entity);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    // ===================== 撞击爆炸 =====================

    public static void doImpact(ServerLevel level, Player owner, Vec3 pos) {
        // 音效
        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.5f, 0.8f);
        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 1.2f, 1.0f);

        // 破坏方块
        breakBlocks(level, owner, pos);

        // 伤害 + 击飞
        damageAndKnockback(level, owner, pos);
    }

    /**
     * 破坏周围方块：中心完全破坏，外部概率破坏，未破坏的方块生成 FallingBlockEntity 击飞
     */
    private static void breakBlocks(ServerLevel level, Player owner, Vec3 center) {
        int r = (int) Math.ceil(EXPLOSION_RADIUS);
        BlockPos centerPos = BlockPos.containing(center);

        // 中心半径（1/2）
        double coreRadius = EXPLOSION_RADIUS / 2.0;
        // 边缘最小破坏概率
        double edgeBreakChance = 0.3;

        Random rand = new Random();

        for (BlockPos p : BlockPos.betweenClosed(centerPos.offset(-r, -r, -r), centerPos.offset(r, r, r))) {

            double dx = p.getX() + 0.5 - center.x;
            double dy = p.getY() + 0.5 - center.y;
            double dz = p.getZ() + 0.5 - center.z;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > EXPLOSION_RADIUS) continue;

            BlockState state = level.getBlockState(p);
            if (state.isAir()) continue;
            float hardness = state.getDestroySpeed(level, p);
            if (hardness < 0 || hardness > 50) continue;   // 基岩等不可破坏

            // ===================== 破坏概率 =====================
            double breakChance;
            if (dist <= coreRadius) {
                breakChance = 1.0;
            } else {
                // ★ 外部：从 1.0 线性衰减到 edgeBreakChance
                double t = (dist - coreRadius) / (EXPLOSION_RADIUS - coreRadius);   // 0 ~ 1
                breakChance = 1.0 - (1.0 - edgeBreakChance) * t;
            }

            if (rand.nextDouble() < breakChance) {
                level.destroyBlock(p, false, owner);
            } else {
                // ---- 未破坏：部分方块被"击飞" ----
                // 只处理"暴露的方块"（上方是空气，且不是液体）
                if (!level.getBlockState(p.above()).isAir()) continue;

                // 只对可以掉落物品的方块生成 FallingBlock
                ItemStack drop = new ItemStack(state.getBlock().asItem());
                if (drop.isEmpty()) continue;

                // 击飞概率：更靠外、更松散的方块更容易被吹起
                double launchChance = 0.25 + 0.35 * (dist / EXPLOSION_RADIUS);
                if (rand.nextDouble() > launchChance) continue;

                // 清除原方块（不掉落，因为有 FallingBlock 代理）
                level.removeBlock(p, false);

                // 生成 FallingBlockEntity
                FallingBlockEntity falling = FallingBlockEntity.fall(level, p, state);

                // 设置击飞速度：向外 + 向上
                Vec3 outDir = new Vec3(dx, 0, dz);
                if (outDir.lengthSqr() < 1e-6) {
                    outDir = new Vec3(0, 0, 1);
                } else {
                    outDir = outDir.normalize();
                }

                double outwardSpeed = 0.3 + rand.nextDouble() * 0.3;
                double upwardSpeed = 0.6 + rand.nextDouble() * 0.5;

                falling.setDeltaMovement(outDir.x * outwardSpeed + (rand.nextDouble() - 0.5) * 0.2, upwardSpeed, outDir.z * outwardSpeed + (rand.nextDouble() - 0.5) * 0.2);
                falling.hurtMarked = true;

                // 加一点自旋效果（可选）
                falling.setStartPos(p);
                level.addFreshEntity(falling);
            }
        }
    }

    /**
     * 伤害 + 击飞周围生物
     */
    private static void damageAndKnockback(ServerLevel level, Player owner, Vec3 center) {
        AABB box = new AABB(center.x - EXPLOSION_RADIUS, center.y - EXPLOSION_RADIUS, center.z - EXPLOSION_RADIUS, center.x + EXPLOSION_RADIUS, center.y + EXPLOSION_RADIUS, center.z + EXPLOSION_RADIUS);

        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != owner && e.isAlive())) {

            Vec3 diff = e.position().add(0, e.getBbHeight() * 0.5, 0).subtract(center);
            double dist = diff.length();
            if (dist > EXPLOSION_RADIUS) continue;

            // 距离衰减
            double falloff = 1.0 - 0.5 * (dist / EXPLOSION_RADIUS);
            float dmg = (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * falloff);

            e.invulnerableTime = 0;
            e.hurt(owner.damageSources().playerAttack(owner), dmg);

            // 从爆炸中心向外击飞
            Vec3 dir = diff.lengthSqr() < 1e-4 ? new Vec3(0, 1, 0) : diff.normalize();
            Vec3 kb = dir.scale(KNOCKBACK).add(0, KNOCKBACK_UP, 0);
            e.setDeltaMovement(e.getDeltaMovement().add(kb));
            e.hurtMarked = true;
        }
    }

}
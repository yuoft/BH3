package com.yuo.bh3.Items.skill;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Entity.WeaponArrowEntity;
import com.yuo.bh3.Items.BH3BaseWeapon;
import com.yuo.bh3.Items.BH3WeaponBow;
import com.yuo.bh3.capability.HonkaiEnergyProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BowSkill {

    /**
     * 技能持续时间（tick）
     */
    public static final int DURATION = 60;          // 3 秒
    /**
     * 射箭间隔（tick）
     */
    public static final int FIRE_INTERVAL = 3;      // 每 0.1 秒一发
    /**
     * 消耗崩坏能
     */
    public static final float HONKAI_COST = 45f;

    /**
     * 玩家 UUID → 剩余 tick
     */
    private static final Map<UUID, Integer> ACTIVE = new HashMap<>();
    /**
     * 玩家 UUID → 上一 tick 是否在挥动（用于检测左键上升沿）
     */
    private static final Map<UUID, Boolean> WAS_SWINGING = new HashMap<>();

    /**
     * 服务端触发
     */
    public static void start(ServerPlayer player) {
        ACTIVE.put(player.getUUID(), DURATION);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_RIPTIDE_2, SoundSource.PLAYERS, 1.0f, 1.5f);
    }

    /**
     * 发射一支箭
     */
    private static void fireArrow(Player p) {
        ItemStack main = p.getMainHandItem();
        if (!(main.getItem() instanceof BH3WeaponBow bow)) return;
        if (!(p.level() instanceof ServerLevel level)) return;

        float damage = bow.getDamage();

        WeaponArrowEntity arrow = new WeaponArrowEntity(damage, p.getX(), p.getEyeY() - 0.1, p.getZ(), level);
        arrow.setOwner(p);

        // 随机散布
        float spreadPitch = (level.random.nextFloat() - 0.5f) * 10f;
        float spreadYaw = (level.random.nextFloat() - 0.5f) * 10f;

        arrow.shootFromRotation(p, p.getXRot() + spreadPitch, p.getYRot() + spreadYaw, 0.0f, 3.0f, 0.5f);

        arrow.setCritArrow(true);
        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

        level.addFreshEntity(arrow);

        level.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.6f, 1.2f);
    }

    @Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerEvents {

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.START) return;
            Player p = event.player;
            if (p.level().isClientSide) return;

            UUID id = p.getUUID();

            // ==================== 2. 检测触发（潜行 + 左键上升沿）====================
            boolean swinging = p.swinging;
            boolean wasSwinging = WAS_SWINGING.getOrDefault(id, false);
            WAS_SWINGING.put(id, swinging);

            if (swinging && !wasSwinging && p.isShiftKeyDown()) {
                ItemStack main = p.getMainHandItem();
                if (main.getItem() instanceof BH3WeaponBow) {
                    if (BH3BaseWeapon.isSkillEnergyCost(p.level(), p, HONKAI_COST)) {
                        if (p instanceof ServerPlayer sp) {
                            HonkaiEnergyProvider.get(p).ifPresent(h -> h.consume(HONKAI_COST));
                            start(sp);
                            main.hurtAndBreak(15, p, e -> e.broadcastBreakEvent(p.getUsedItemHand()));
                        }
                    }
                }
            }

            // ==================== 3. 连射处理 ====================
            int active = ACTIVE.getOrDefault(id, 0);
            if (active > 0) {
                int elapsed = DURATION - active;

                // 按间隔发射
                if (elapsed % FIRE_INTERVAL == 0) {
                    fireArrow(p);
                }

                int next = active - 1;
                if (next <= 0) {
                    ACTIVE.remove(id);
                } else {
                    ACTIVE.put(id, next);
                }
            }
        }
    }
}
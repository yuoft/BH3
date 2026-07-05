package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID)
public class AdvancementHandler {

    // 用于限制检查频率，避免每 tick 都统计（性能优化）
    private static final ConcurrentHashMap<UUID, Integer> lastCheckTick = new ConcurrentHashMap<>();
    private static final int CHECK_INTERVAL = 20 * 3; // 每 5 秒检查一次

    // 武器标签（对应 data/bh3/tags/items/weapons.json）
    private static final TagKey<Item> WEAPON_TAG = ItemTags.create(ModUtils.fa("bh3_weapons"));
    public static final Set<Item> uniqueWeapons = new HashSet<>();

    // 所有武器的总数量（请根据您的实际武器数量调整）
    private static final int TOTAL_WEAPONS = 44;
    private static final String WEAPON_COUNT_KEY = "bh3_weapon_count";

    /**
     * 玩家 Tick 事件：定期检查武器收集进度
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // 只处理服务端逻辑，且只在 Tick 结束时执行
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;

        Player player = event.player;
        UUID uuid = player.getUUID();
        int currentTick = player.tickCount;

        // 检查是否达到检查间隔
        Integer lastTick = lastCheckTick.get(uuid);
        if (lastTick != null && (currentTick - lastTick) < CHECK_INTERVAL) {
            return;
        }
        lastCheckTick.put(uuid, currentTick);

        // 执行武器收集检查
        checkWeaponAdvancements(player);
    }

    /**
     * 玩家登录事件：登入时立即检查一次
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        // 延迟 1 tick 执行，确保玩家数据完全加载
        event.getEntity().getServer().execute(() -> {
            checkWeaponAdvancements(event.getEntity());
        });
    }

    /**
     * 玩家捡起物品事件：捡起物品时立即检查（更及时）
     */
    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        // 捡到物品后立即检查，同时重置 tick 计时器，让定时检查重置
        checkWeaponAdvancements(event.getEntity());
        // 重置计时器，避免立即触发两次检查（但实际不会重复触发，因为同一 tick 内只会执行一次）
        lastCheckTick.put(event.getEntity().getUUID(), event.getEntity().tickCount);
    }

    /**
     * 核心方法：统计武器数量并解锁对应进度
     */
    private static void checkWeaponAdvancements(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // 1. 收集所有不同武器（去重）
        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;
            if (stack.is(WEAPON_TAG)) {
                uniqueWeapons.add(stack.getItem());
            }
        }
        int currentCount = uniqueWeapons.size();

        // 2. 持久化保存数量
        CompoundTag persistentData = serverPlayer.getPersistentData();
        persistentData.putInt(WEAPON_COUNT_KEY, currentCount);

        // 3. 解锁进度（仅在首次达到时触发，并显示消息）
        var advancements = serverPlayer.getAdvancements();
        var manager = serverPlayer.server.getAdvancements();

        if (currentCount >= 5) {
            var adv = manager.getAdvancement(ModUtils.fa("weapon_collector_1"));
            if (adv != null && !advancements.getOrStartProgress(adv).isDone()) {
                advancements.award(adv, "collect_5");
                serverPlayer.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§a已收集 §e" + currentCount + " §a种武器！"),
                        false
                );
            }
        }

        if (currentCount >= 15) {
            var adv = manager.getAdvancement(ModUtils.fa("weapon_collector_2"));
            if (adv != null && !advancements.getOrStartProgress(adv).isDone()) {
                advancements.award(adv, "collect_15");
                serverPlayer.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§a已收集 §e" + currentCount + " §a种武器！"),
                        false
                );
            }
        }

        if (currentCount >= TOTAL_WEAPONS) {
            var adv = manager.getAdvancement(ModUtils.fa("weapon_collector_3"));
            if (adv != null && !advancements.getOrStartProgress(adv).isDone()) {
                advancements.award(adv, "collect_all");
                serverPlayer.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§a🎉 集齐全部 §e" + TOTAL_WEAPONS + " §a种武器！"),
                        false
                );
            }
        }
    }
}
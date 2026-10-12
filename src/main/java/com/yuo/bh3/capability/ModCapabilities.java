package com.yuo.bh3.capability;

import com.yuo.bh3.BH3;
import com.yuo.bh3.ModUtils;
import com.yuo.bh3.packet.SyncHonkaiEnergyPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {

    public static final ResourceLocation HONKAI_ENERGY_ID = ModUtils.fa("honkai_energy");

    /**
     * 附加到玩家
     */
    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(HONKAI_ENERGY_ID, new HonkaiEnergyProvider());
        }
    }

    /**
     * 每 tick 回复 + 同步
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return;

        player.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
            float before = energy.getEnergy();
            energy.tick(player);

            // 每 5 tick 或数值变化超过 1 时同步
            if (player.tickCount % 5 == 0 || Math.abs(energy.getEnergy() - before) > 1f) {
                if (player instanceof ServerPlayer sp) {
                    SyncHonkaiEnergyPacket.send(sp, energy.getEnergy(), energy.getMaxEnergy());
                }
            }
        });
    }

    /**
     * 重生 / 换维度：保留能量
     */
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(HonkaiEnergyProvider.CAP).ifPresent(oldEnergy ->
                event.getEntity().getCapability(HonkaiEnergyProvider.CAP).ifPresent(newEnergy ->
                        ((HonkaiEnergyImpl) newEnergy).copyFrom(oldEnergy)));
        event.getOriginal().invalidateCaps();
    }

    /**
     * 登录时同步一次
     */
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            sp.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy ->
                    SyncHonkaiEnergyPacket.send(sp, energy.getEnergy(), energy.getMaxEnergy()));
        }
    }

    /**
     * 注册 Capability 本身
     */
    @Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void onRegister(RegisterCapabilitiesEvent event) {
            event.register(IHonkaiEnergy.class);
        }
    }
}
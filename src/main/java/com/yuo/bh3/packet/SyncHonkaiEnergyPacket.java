package com.yuo.bh3.packet;

import com.yuo.bh3.capability.HonkaiEnergyProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncHonkaiEnergyPacket {

    private final float energy;
    private final float maxEnergy;

    // 解码构造（网络 → 对象）
    public SyncHonkaiEnergyPacket(FriendlyByteBuf buffer) {
        this.energy = buffer.readFloat();
        this.maxEnergy = buffer.readFloat();
    }

    // 发送构造（对象 → 网络）
    public SyncHonkaiEnergyPacket(float energy, float maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(energy);
        buf.writeFloat(maxEnergy);
    }

    public static void handler(SyncHonkaiEnergyPacket msg, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player == null) return;
                    mc.player.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
                        energy.setMaxEnergy(msg.maxEnergy);
                        energy.setEnergy(msg.energy);
                    });
                })
        );
        ctx.get().setPacketHandled(true);
    }

    // ===================== 发送方法 =====================

    /** 发给单个玩家 */
    public static void send(ServerPlayer player, float energy, float maxEnergy) {
        NetWorkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncHonkaiEnergyPacket(energy, maxEnergy)
        );
    }

    /** 广播给所有玩家（可选） */
    public static void broadcast(float energy, float maxEnergy) {
        NetWorkHandler.INSTANCE.send(
                PacketDistributor.ALL.noArg(),
                new SyncHonkaiEnergyPacket(energy, maxEnergy)
        );
    }
}
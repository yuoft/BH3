package com.yuo.bh3.capability;

import com.yuo.bh3.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class SyncHonkaiEnergyPacket {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ModUtils.fa("main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private final float energy;
    private final float maxEnergy;

    public SyncHonkaiEnergyPacket(float energy, float maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    public static void register() {
        CHANNEL.registerMessage(0, SyncHonkaiEnergyPacket.class, SyncHonkaiEnergyPacket::encode, SyncHonkaiEnergyPacket::decode, SyncHonkaiEnergyPacket::handle);
    }

    public static void send(ServerPlayer player, float energy, float maxEnergy) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncHonkaiEnergyPacket(energy, maxEnergy));
    }

    private static void encode(SyncHonkaiEnergyPacket pkt, FriendlyByteBuf buf) {
        buf.writeFloat(pkt.energy);
        buf.writeFloat(pkt.maxEnergy);
    }

    private static SyncHonkaiEnergyPacket decode(FriendlyByteBuf buf) {
        return new SyncHonkaiEnergyPacket(buf.readFloat(), buf.readFloat());
    }

    private static void handle(SyncHonkaiEnergyPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player == null) return;
            player.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
                energy.setMaxEnergy(pkt.maxEnergy);
                energy.setEnergy(pkt.energy);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
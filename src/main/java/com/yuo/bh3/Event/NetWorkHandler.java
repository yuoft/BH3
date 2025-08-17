package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetWorkHandler {
    public static SimpleChannel INSTANCE;
    private static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }


    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(BH3.MOD_ID, "network"), //标识符
                () -> VERSION, //数据包版本
                (version) -> version.equals(VERSION), //客户端和服务端可以接收的版本号
                (version) -> version.equals(VERSION)
        );
        INSTANCE.registerMessage(nextID(),
                TotemPacket.class,
                TotemPacket::toBytes,
                TotemPacket::new,
                TotemPacket::handler);
    }
}

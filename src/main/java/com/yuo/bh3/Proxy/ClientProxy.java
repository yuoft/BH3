package com.yuo.bh3.Proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 客户端属性注册
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void registerHandlers(IEventBus modBus) {
        super.registerHandlers(modBus);
        modBus.addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {

    }
}

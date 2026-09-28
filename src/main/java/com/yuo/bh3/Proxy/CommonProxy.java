package com.yuo.bh3.Proxy;

import com.yuo.bh3.Event.NetWorkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements IProxy {
    @Override
    public void registerHandlers(IEventBus modBus) {
        IProxy.super.registerHandlers(modBus);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }
}

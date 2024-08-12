package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BH3.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvent {

    @SubscribeEvent
    public  static void registerModel(ModelBakeEvent event){
    }
}

package com.yuo.bh3.Proxy;

import com.yuo.bh3.Items.StaMinaPotion.SodaPopBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements IProxy {
    @Override
    public void registerHandlers(IEventBus modBus) {
        IProxy.super.registerHandlers(modBus);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // ★ 注册自定义酿造配方
            BrewingRecipeRegistry.addRecipe(new SodaPopBrewingRecipe());
        });
    }
}

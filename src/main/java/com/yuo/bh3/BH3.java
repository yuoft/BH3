package com.yuo.bh3;

import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Proxy.ClientProxy;
import com.yuo.bh3.Proxy.CommonProxy;
import com.yuo.bh3.Proxy.IProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bh3")
public class BH3 {
	public static final String MODID = "bh3";
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public BH3() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		//注册至mod总线
        BH3Items.ITEMS.register(modEventBus);
        SoundRegistry.SOUNDS.register(modEventBus);
        proxy.registerHandlers();

    }

}

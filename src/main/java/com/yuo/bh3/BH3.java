package com.yuo.bh3;

import com.yuo.bh3.Blocks.BH3Blocks;
import com.yuo.bh3.Entity.BH3EntityTypes;
import com.yuo.bh3.Event.NetWorkHandler;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Proxy.ClientProxy;
import com.yuo.bh3.Proxy.CommonProxy;
import com.yuo.bh3.Proxy.IProxy;
import com.yuo.bh3.Wrold.ModWorldGen;
import com.yuo.bh3.tab.BH3Tabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bh3")
public class BH3 {
	public static final String MOD_ID = "bh3";
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    @SuppressWarnings("removal")
    public BH3() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SERVER_CONFIG); //配置文件
        modEventBus.addListener(this::commonSetup);

		//注册至mod总线
        BH3Items.ITEMS.register(modEventBus);
        BH3Blocks.BLOCKS.register(modEventBus);
        BH3Tabs.TABS.register(modEventBus);
        SoundRegistry.SOUNDS.register(modEventBus);
        BH3EntityTypes.ENTITY_TYPES.register(modEventBus);

        proxy.registerHandlers();

        /*
         * 武器属性枚举类
         * 武器json简化，父json使用？
         * 史诗战斗兼容
         * 物理实体模型属性调整
         */

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }
}

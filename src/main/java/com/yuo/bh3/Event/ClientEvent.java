package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Client.BigSwordRenderer;
import com.yuo.bh3.Client.PlacedWeaponRenderer;
import com.yuo.bh3.Client.SickleRenderer;
import com.yuo.bh3.Entity.BH3EntityTypes;
import com.yuo.bh3.Client.WeaponArrowRender;
import com.yuo.bh3.Items.BH3BaseWeapon;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Items.BH3WeaponBow;
import com.yuo.bh3.ModUtils;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvent {

    //实体渲染注册
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BH3EntityTypes.WEAPON_ARROW.get(), WeaponArrowRender::new); //投掷物渲染

        event.registerEntityRenderer(BH3EntityTypes.PLACED_WEAPON.get(), PlacedWeaponRenderer::new);// 添加插地武器的渲染器

        event.registerEntityRenderer(BH3EntityTypes.BIG_SWORD.get(), BigSwordRenderer::new);
        event.registerEntityRenderer(BH3EntityTypes.SICKLE.get(), SickleRenderer::new);
    }

    /** 手动注册 2D 图标模型，避免因未被物品引用而不被烘焙 */
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        for (RegistryObject<Item> entry : BH3Items.ITEMS.getEntries()) {
            if (entry.get() instanceof BH3BaseWeapon || entry.get() instanceof BH3WeaponBow){
                String path = entry.getId().getPath();      // "bh3_fld"
                // 图标模型名 = 注册名 + "_icon"
                event.register(new ModelResourceLocation(ModUtils.fa(path + "_icon"), "inventory"));
            }
        }
    }
}

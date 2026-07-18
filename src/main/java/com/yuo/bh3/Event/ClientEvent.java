package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Client.ItemModel;
import com.yuo.bh3.Client.PlacedWeaponRenderer;
import com.yuo.bh3.Entity.BH3EntityTypes;
import com.yuo.bh3.Entity.WeaponArrowRender;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Items.BH3BaseWeapon;
import com.yuo.bh3.Items.BH3WeaponBow;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent.ModifyBakingResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvent {

    //实体渲染注册
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BH3EntityTypes.WEAPON_ARROW.get(), WeaponArrowRender::new); //投掷物渲染

        event.registerEntityRenderer(BH3EntityTypes.PLACED_WEAPON.get(), PlacedWeaponRenderer::new);// 添加插地武器的渲染器
    }

    //物品obj模型注册
    @SubscribeEvent
    public  static void registerModel(ModifyBakingResult event){
        Map<ResourceLocation, BakedModel> models = event.getModels();
        ModelResourceLocation diamondLoc = new ModelResourceLocation(ResourceLocation.parse("diamond_sword"), "inventory");
        BakedModel diamondModel = models.get(diamondLoc);
        for (RegistryObject<Item> entry : BH3Items.ITEMS.getEntries()) {
//            if (entry.get() == BH3Items.bh3_FLD.get()) continue;
            if (entry.get() instanceof BH3BaseWeapon || entry.get() instanceof BH3WeaponBow){
                ModelResourceLocation res = new ModelResourceLocation(entry.getId(), "inventory");
                BakedModel objModel = models.get(res);
                if (objModel != null && !(objModel instanceof ItemModel)) {
                    ItemModel itemModel = new ItemModel(objModel, diamondModel);
                    event.getModels().put(res, itemModel);
                }
            }
        }
    }
}

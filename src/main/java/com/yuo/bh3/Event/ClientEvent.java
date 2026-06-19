package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Client.ItemModel;
import com.yuo.bh3.Entity.BH3EntityTypes;
import com.yuo.bh3.Entity.WeaponArrowRender;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Items.BH3Weapon;
import com.yuo.bh3.Items.BH3WeaponBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent.BakingCompleted;
import net.minecraftforge.client.event.ModelEvent.ModifyBakingResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvent {

    //实体渲染注册
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BH3EntityTypes.WEAPON_ARROW.get(), WeaponArrowRender::new); //投掷物渲染
    }

    //物品obj模型注册
    @SubscribeEvent
    public  static void registerModel(ModifyBakingResult event){
        Map<ResourceLocation, BakedModel> models = event.getModels();
        ModelResourceLocation diamondLoc = new ModelResourceLocation(ResourceLocation.parse("diamond_sword"), "inventory");
        BakedModel diamondModel = models.get(diamondLoc);
        for (RegistryObject<Item> entry : BH3Items.ITEMS.getEntries()) {
            if (entry.get() instanceof BH3Weapon || entry.get() instanceof BH3WeaponBow){
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

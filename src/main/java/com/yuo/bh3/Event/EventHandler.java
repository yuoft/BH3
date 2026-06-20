package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Items.BH3Items;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Builder;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;

import java.util.Map;
import java.util.function.Function;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(modid = BH3.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void registerWeaponCapabilities(WeaponCapabilityPresetRegistryEvent event) {
        Map<ResourceLocation, Function<Item, CapabilityItem.Builder>> map = event.getTypeEntry();

        // 构建 CapabilityItem
        map.put(BH3Items.JM.getId(), (item) -> {
            return CapabilityItem.builder()
                    .category(WeaponCategories.SWORD)   // 武器类型：剑、大剑、太刀等
                    ;
        });

        /*
        .attributes(
                            CapabilityItem.Attributes.builder()
                                    .armorNegation(0.5F)      // 破甲
                                    .impact(1.0F)             // 冲击力
                                    .maxStrikes(2)            // 最大连击次数
                                    .damageBonus(2.0F)        // 额外伤害
                                    .speedBonus(0.1F)         // 速度加成
                                    .build()
                    )
         */
    }

    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        //首次登录时发送消息
        if (!player.getPersistentData().getBoolean("bh3:login")){
            player.getPersistentData().putBoolean("bh3:login", true);
            player.sendSystemMessage(Component.translatable("bh3.message.login")
                    .setStyle(Style.EMPTY.withHoverEvent(HoverEvent.Action.SHOW_TEXT.deserializeFromLegacy(Component.translatable("bh3.message.login0")))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))));
        }
    }

    @SubscribeEvent
    public static void playerDead(LivingDeathEvent event){
        LivingEntity living = event.getEntity();
        if (living instanceof Player player){
            ItemStack recoveryNeedle = getPlayerBagItem(player);
            if (!recoveryNeedle.isEmpty()){
                NetWorkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new TotemPacket(recoveryNeedle, player));
                player.removeAllEffects();
                player.setHealth(8.0f);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2600, 3));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 700, 1));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1100, 0));
                recoveryNeedle.shrink(1);
                event.setCanceled(true);
            }
        }
    }

    /**
     * 获取玩家背包中的应急复苏针
     * @param player 玩家
     * @return 图腾
     */
    private static ItemStack getPlayerBagItem(Player player){
        ItemStack mainhand = player.getMainHandItem();
        if (mainhand.getItem() == BH3Items.recoveryNeedle.get()){
            return mainhand;
        }
        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() == BH3Items.recoveryNeedle.get()){
            return offhand;
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == BH3Items.recoveryNeedle.get())
                return stack;
        }

        return ItemStack.EMPTY;
    }
}


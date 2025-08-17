package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Items.BH3Items;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(modid = BH3.MOD_ID)
public class EventHandler {
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
                NetWorkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new TotemPacket(recoveryNeedle, player));

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


package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(modid = BH3.MODID)
public class EventHandler {
    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();
        //首次登录时发送消息
        if (!player.getPersistentData().getBoolean("bh3:login")){
            player.getPersistentData().putBoolean("bh3:login", true);
            player.sendMessage(new TranslationTextComponent("bh3.message.login")
                    .setStyle(Style.EMPTY.setHoverEvent(HoverEvent.Action.SHOW_TEXT.deserialize(new TranslationTextComponent("bh3.message.login0")))
                            .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
        }
    }
}


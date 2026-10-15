package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Blocks.EnergyOre;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.capability.HonkaiEnergyProvider;
import com.yuo.bh3.packet.NetWorkHandler;
import com.yuo.bh3.packet.TotemPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(modid = BH3.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        LivingEntity living = event.getEntity();
        Level level = living.level();
        if (level.random.nextDouble() < 0.2){
            event.getDrops().add(new ItemEntity(level, living.getX(), living.getY(), living.getZ(),
                    new ItemStack(BH3Items.baseChip.get(), Mth.randomBetweenInclusive(level.random, 0, 5))));
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(BreakEvent event) {
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof EnergyOre){ //挖掘矿石增加能量
            HonkaiEnergyProvider.get(player).ifPresent(h -> h.add(0.5f));
        }
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


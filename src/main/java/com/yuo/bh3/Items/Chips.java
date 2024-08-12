package com.yuo.bh3.Items;

import com.yuo.bh3.tab.BH3Tabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * 学习芯片
 */
public class Chips extends Item {
    public Chips() {
        super(new Item.Properties().group(BH3Tabs.BH3_ITEM));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        int exp = 0;
        if (item == BH3Items.basechip.get()){
            exp = 5;
        }else if (item == BH3Items.advancedchips.get()){
            exp = 10;
        }else if (item == BH3Items.seniorchips.get()){
            exp = 20;
        }else if (item == BH3Items.premiumchips.get()){
            exp = 50;
        }
        if (!world.isRemote){
            player.giveExperiencePoints(exp);
        }

        return super.onItemRightClick(world, player, hand);
    }
}

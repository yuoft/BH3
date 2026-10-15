package com.yuo.bh3.Items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 学习芯片
 */
public class ExpChip extends Item {
    public ExpChip() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        int exp = 0;
        if (item == BH3Items.baseChip.get()){
            exp = 7;
        }else if (item == BH3Items.advancedChips.get()){
            exp = 14;
        }else if (item == BH3Items.seniorChips.get()){
            exp = 28;
        }else if (item == BH3Items.premiumChips.get()){
            exp = 56;
        }
        if (!level.isClientSide){
            player.giveExperiencePoints(exp);
        }

        return super.use(level, player, hand);
    }
}

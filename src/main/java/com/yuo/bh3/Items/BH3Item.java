package com.yuo.bh3.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BH3Item extends Item {
    public BH3Item() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> components, TooltipFlag flag) {
        Item item = stack.getItem();
        if (item == BH3Items.recoveryNeedle.get()) {
            components.add(Component.translatable("bh3.tips.recovery_needle"));
        }
    }
}

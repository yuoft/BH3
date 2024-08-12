package com.yuo.bh3.Items;

import com.yuo.bh3.tab.BH3Tabs;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class BH3Food extends Item {
    public BH3Food(Food food) {
        super(new Properties().group(BH3Tabs.BH3_ITEM).food(food));
    }
}

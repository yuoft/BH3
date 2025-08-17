package com.yuo.bh3.Items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BH3Food extends Item {
    public BH3Food(FoodProperties food) {
        super(new Properties().food(food));
    }
}

package com.yuo.bh3.Items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BH3Food extends Item {
    public BH3Food(FoodProperties food) {
        super(new Properties().food(food));
    }

    public static FoodProperties waterMelon = new FoodProperties.Builder().nutrition(3).saturationMod(9).build();
    public static FoodProperties sodaPop = new FoodProperties.Builder().nutrition(6).saturationMod(18)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 0), 1.0f).build();
    public static FoodProperties staMinaPotion = new FoodProperties.Builder().nutrition(10).saturationMod(30).build();
}

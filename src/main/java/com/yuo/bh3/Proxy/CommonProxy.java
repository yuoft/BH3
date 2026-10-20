package com.yuo.bh3.Proxy;

import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Items.StaMinaPotion.SodaPopBrewingRecipe;
import com.yuo.bh3.effects.BH3Potions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements IProxy {
    @Override
    public void registerHandlers(IEventBus modBus) {
        IProxy.super.registerHandlers(modBus);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // ★ 注册自定义酿造配方
            BrewingRecipeRegistry.addRecipe(new SodaPopBrewingRecipe());

            // 2. 注册能量满溢药水的基础配方
            // 粗制的药水 + 崩坏碎片 -> 基础能量满溢药水
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(BH3Items.honkaiChip.get()),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), BH3Potions.HONKAI_ENERGY.get())
            ));

            // 3. 注册强效升级配方
            // 基础能量满溢 + 荧石粉 -> 强效能量满溢
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), BH3Potions.HONKAI_ENERGY.get())),
                    Ingredient.of(Items.GLOWSTONE_DUST),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), BH3Potions.STRONG_HONKAI_ENERGY.get())
            ));

            // 4. 注册长效升级配方
            // 基础能量满溢 + 红石 -> 长效能量满溢
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), BH3Potions.HONKAI_ENERGY.get())),
                    Ingredient.of(Items.REDSTONE),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), BH3Potions.LONG_HONKAI_ENERGY.get())
            ));
        });
    }
}

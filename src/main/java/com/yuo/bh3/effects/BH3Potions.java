package com.yuo.bh3.effects;

import com.yuo.bh3.BH3;
import com.yuo.bh3.effects.BH3Effects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BH3Potions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, BH3.MOD_ID);

    // 基础版：50 秒，等级 I (amplifier 0)
    public static final RegistryObject<Potion> HONKAI_ENERGY = POTIONS.register("honkai_energy", 
            () -> new Potion(new MobEffectInstance(BH3Effects.honkaiEnergyUp.get(), 1000, 0)));

    // 强效版：30 秒，等级 II (amplifier 1)
    public static final RegistryObject<Potion> STRONG_HONKAI_ENERGY = POTIONS.register("strong_honkai_energy", 
            () -> new Potion(new MobEffectInstance(BH3Effects.honkaiEnergyUp.get(), 600, 1)));

    // 长效版：120 秒，等级 I (amplifier 0)
    public static final RegistryObject<Potion> LONG_HONKAI_ENERGY = POTIONS.register("long_honkai_energy", 
            () -> new Potion(new MobEffectInstance(BH3Effects.honkaiEnergyUp.get(), 2400, 0)));
}
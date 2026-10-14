package com.yuo.bh3.effects;

import com.yuo.bh3.BH3;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BH3Effects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BH3.MOD_ID);

    public static final Supplier<MobEffect> honkaiEnergyUp = EFFECTS.register("honkai_energy_up", HonkaiEnergyUpEffect::new);
}

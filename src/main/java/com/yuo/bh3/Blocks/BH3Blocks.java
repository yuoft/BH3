package com.yuo.bh3.Blocks;

import com.yuo.bh3.BH3;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BH3Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BH3.MOD_ID);

    public static RegistryObject<Block> energyOre = BLOCKS.register("energy_ore", () -> new EnergyOre(5, 10, UniformInt.of(1,3)));
    public static RegistryObject<Block> energyEndOre = BLOCKS.register("energy_end_ore", () -> new EnergyOre(6, 12, UniformInt.of(3,5)));
    public static RegistryObject<Block> energyNetherOre = BLOCKS.register("energy_nether_ore", () -> new EnergyOre(5.5f, 11, UniformInt.of(2,4)));
    public static RegistryObject<Block> crystalOre = BLOCKS.register("crystal_ore", () -> new EnergyOre(6, 15, UniformInt.of(4,6)));
}

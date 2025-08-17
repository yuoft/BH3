package com.yuo.bh3.Wrold;

import com.yuo.bh3.BH3;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, OreFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, OrePlacements::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, OreBiomeModifiers::bootstrap);

    public ModWorldGen(PackOutput output, CompletableFuture<Provider> registries) {
        super(output, registries, BUILDER, Set.of(BH3.MOD_ID));
    }
}
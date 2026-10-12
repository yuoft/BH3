package com.yuo.bh3.Wrold;

import com.yuo.bh3.BH3;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class OreBiomeModifiers {
    // 注册的key
    public static final ResourceKey<BiomeModifier> ADD_ENERGY_ORE = registerKey("add_energy_ore");
    public static final ResourceKey<BiomeModifier> ADD_ENERGY_DEEP_ORE = registerKey("add_energy_deep_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ENERGY_ORE = registerKey("add_nether_energy_ore");
    public static final ResourceKey<BiomeModifier> ADD_END_ENERGY_ORE = registerKey("add_end_energy_ore");
    public static final ResourceKey<BiomeModifier> ADD_CRYSTAL_ORE = registerKey("add_crystal_ore");
    public static final ResourceKey<BiomeModifier> ADD_CRYSTAL_DEEP_ORE = registerKey("add_crystal_deep_ore");

    // BootstapContext 数据生成的上下文
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // ★ 主世界矿
        context.register(ADD_ENERGY_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),                      // 主世界
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ENERGY_DEEP_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),                      // 主世界
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY_DEEP)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        // ★ 下界矿
        context.register(ADD_NETHER_ENERGY_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),                         // ★ 改成下界
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY_NETHER)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        // ★ 末地矿
        context.register(ADD_END_ENERGY_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),                            // ★ 改成末地
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY_END)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        // ★ 主世界水晶矿
        context.register(ADD_CRYSTAL_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_CRYSTAL)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_CRYSTAL_DEEP_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_CRYSTAL_DEEP)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(BH3.MOD_ID, name));
    }
}

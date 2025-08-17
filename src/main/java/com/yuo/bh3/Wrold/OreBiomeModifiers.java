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
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ENERGY_ORE = registerKey("add_nether_energy_ore");
    public static final ResourceKey<BiomeModifier> ADD_END_ENERGY_ORE = registerKey("add_end_energy_ore");
    public static final ResourceKey<BiomeModifier> ADD_CRYSTAL_ORE = registerKey("add_crystal_ore");
    // BootstapContext 数据生成的上下文
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        // 通过上下文获得PLACED_FEATURE的注册HolderGetter
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        // 通过上下文获得BIOME的HolderGetter
        var biomes = context.lookup(Registries.BIOME);
        // 生成json文件，第一个参数是key，第二个参数是BiomeModifiers，
        // 我们使用了子类AddFeaturesBiomeModifier，是指添加feature给biome
        // 第一个参数是holderset的biome ，这里是否是主世界的生物群系。即返回了主世界的生物群系
        // 第二个holdlerSet是指所有的feature，我们通过placedFeatures获得
        // 丢三个参数要求给出在世界生成的什么阶段加你的feature，我们这里是地下矿物生成的时候，你可以到该类下面看看，这是个枚举，
        context.register(ADD_ENERGY_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_NETHER_ENERGY_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY_NETHER)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_END_ENERGY_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_CRYSTAL)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_CRYSTAL_ORE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ENERGY_END)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(BH3.MOD_ID, name));
    }
}

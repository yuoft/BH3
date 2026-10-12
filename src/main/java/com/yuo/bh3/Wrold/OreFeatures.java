package com.yuo.bh3.Wrold;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Blocks.BH3Blocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class OreFeatures {
    // 创建OreFeature对应的ResourceKey
    //
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ENERGY = createKey("ore_energy");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ENERGY_DEEP = createKey("ore_energy_deep");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ENERGY_NETHER = createKey("ore_energy_nether");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ENERGY_END = createKey("ore_energy_end");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRYSTAL = createKey("ore_crystal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRYSTAL_DEEP = createKey("ore_crystal_deep");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOre = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOre = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrack = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endStone = new BlockMatchTest(Blocks.END_STONE);   // ★ 加末地

        // 主世界能量矿
        List<TargetBlockState> energyList = List.of(
                OreConfiguration.target(stoneOre, BH3Blocks.energyOre.get().defaultBlockState())
        );
        // 主世界能量矿
        List<TargetBlockState> energyDeepList = List.of(
                OreConfiguration.target(deepslateOre, BH3Blocks.energyDeepOre.get().defaultBlockState())
        );

        // 主世界水晶矿
        List<TargetBlockState> crystalList = List.of(
                OreConfiguration.target(stoneOre, BH3Blocks.crystalOre.get().defaultBlockState())
        );
        // 主世界水晶矿
        List<TargetBlockState> crystalDeepList = List.of(
                OreConfiguration.target(deepslateOre, BH3Blocks.crystalDeepOre.get().defaultBlockState())
        );

        // 下界能量矿
        List<TargetBlockState> netherList = List.of(
                OreConfiguration.target(netherrack, BH3Blocks.energyNetherOre.get().defaultBlockState())
        );

        // ★ 末地能量矿（用 END_STONE 替换）
        List<TargetBlockState> endList = List.of(
                OreConfiguration.target(endStone, BH3Blocks.energyEndOre.get().defaultBlockState())
        );

        FeatureUtils.register(ctx, ORE_ENERGY, Feature.ORE, new OreConfiguration(energyList, 4));  //每堆数量
        FeatureUtils.register(ctx, ORE_ENERGY_DEEP, Feature.ORE, new OreConfiguration(energyDeepList, 5));
        FeatureUtils.register(ctx, ORE_ENERGY_NETHER, Feature.ORE, new OreConfiguration(netherList, 4));
        FeatureUtils.register(ctx, ORE_ENERGY_END, Feature.ORE, new OreConfiguration(endList, 5));
        FeatureUtils.register(ctx, ORE_CRYSTAL, Feature.ORE, new OreConfiguration(crystalList, 8));
        FeatureUtils.register(ctx, ORE_CRYSTAL_DEEP, Feature.ORE, new OreConfiguration(crystalDeepList, 10));
    }
    // 创建ResourceKey的方法
    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String pName) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(BH3.MOD_ID,pName));
    }
}

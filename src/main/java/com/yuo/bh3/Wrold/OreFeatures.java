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
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ENERGY_NETHER = createKey("ore_energy_nether");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ENERGY_END = createKey("ore_energy_end");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRYSTAL = createKey("ore_crystal");

    //BootstapContext 是我们datagen的上下文，等会我们使用数据生成的时候说。
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> pContext) {
        //  创建对应的tag，如果有多个就创建多个
        RuleTest stoneOreReplaceRuleTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepSlateOreReplaceRuleTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherOreReplaceRuleTest = new BlockMatchTest(Blocks.NETHERRACK);

        // 创建一个list
        List<TargetBlockState> list0 = List.of(
                OreConfiguration.target(stoneOreReplaceRuleTest, BH3Blocks.energyOre.get().defaultBlockState()),
                OreConfiguration.target(deepSlateOreReplaceRuleTest, BH3Blocks.energyOre.get().defaultBlockState())
        );
        List<TargetBlockState> list1 = List.of(
                OreConfiguration.target(stoneOreReplaceRuleTest, BH3Blocks.crystalOre.get().defaultBlockState()),
                OreConfiguration.target(deepSlateOreReplaceRuleTest, BH3Blocks.crystalOre.get().defaultBlockState())
        );
        List<TargetBlockState> list00 = List.of(
                OreConfiguration.target(netherOreReplaceRuleTest, BH3Blocks.energyNetherOre.get().defaultBlockState())
        );
        List<TargetBlockState> list11 = List.of(
                OreConfiguration.target(netherOreReplaceRuleTest, BH3Blocks.energyEndOre.get().defaultBlockState())
        );
        // 注册对应orefeature，使用listOreConfiguration，9 上文提到的size
        FeatureUtils.register(pContext, ORE_ENERGY, Feature.ORE, new OreConfiguration(list0, 4));
        FeatureUtils.register(pContext, ORE_ENERGY_NETHER, Feature.ORE, new OreConfiguration(list00, 5));
        FeatureUtils.register(pContext, ORE_ENERGY_END, Feature.ORE, new OreConfiguration(list11, 5));
        FeatureUtils.register(pContext, ORE_CRYSTAL, Feature.ORE, new OreConfiguration(list1, 10));

    }
    // 创建ResourceKey的方法
    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String pName) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(BH3.MOD_ID,pName));
    }
}

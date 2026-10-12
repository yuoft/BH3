package com.yuo.bh3.Wrold;

import com.yuo.bh3.BH3;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacements {
    // 创建对应的placement的资源键
    public static final ResourceKey<PlacedFeature> ORE_ENERGY = createKey("ore_energy");
    public static final ResourceKey<PlacedFeature> ORE_ENERGY_DEEP = createKey("ore_energy_deep");
    public static final ResourceKey<PlacedFeature> ORE_CRYSTAL = createKey("ore_crystal");
    public static final ResourceKey<PlacedFeature> ORE_CRYSTAL_DEEP = createKey("ore_crystal_deep");
    public static final ResourceKey<PlacedFeature> ORE_ENERGY_NETHER = createKey("ore_energy_nether");
    public static final ResourceKey<PlacedFeature> ORE_ENERGY_END = createKey("ore_energy_end");

    public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
        HolderGetter<ConfiguredFeature<?, ?>> features = ctx.lookup(Registries.CONFIGURED_FEATURE);

        // ★ 变量名与配置严格对应
        Holder<ConfiguredFeature<?, ?>> energyHolder = features.getOrThrow(OreFeatures.ORE_ENERGY);
        Holder<ConfiguredFeature<?, ?>> energyDeepHolder = features.getOrThrow(OreFeatures.ORE_ENERGY_DEEP);
        Holder<ConfiguredFeature<?, ?>> energyNetherHolder = features.getOrThrow(OreFeatures.ORE_ENERGY_NETHER);
        Holder<ConfiguredFeature<?, ?>> energyEndHolder = features.getOrThrow(OreFeatures.ORE_ENERGY_END);
        Holder<ConfiguredFeature<?, ?>> crystalHolder = features.getOrThrow(OreFeatures.ORE_CRYSTAL);
        Holder<ConfiguredFeature<?, ?>> crystalDeepHolder = features.getOrThrow(OreFeatures.ORE_CRYSTAL_DEEP);

        // 主世界能量矿：-64 ~ 128，每区块 8 处
        PlacementUtils.register(ctx, ORE_ENERGY, energyHolder,
                commonOrePlacement(8,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128))));
        PlacementUtils.register(ctx, ORE_ENERGY_DEEP, energyDeepHolder,
                commonOrePlacement(8,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));

        // 下界能量矿：0 ~ 96，每区块 10 处
        PlacementUtils.register(ctx, ORE_ENERGY_NETHER, energyNetherHolder,
                commonOrePlacement(10,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(96))));

        // ★ 末地能量矿：0 ~ 80（末地地表在 Y=60~80 左右）
        PlacementUtils.register(ctx, ORE_ENERGY_END, energyEndHolder,
                commonOrePlacement(6,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(80))));

        // 主世界水晶矿：0 ~ 80，每区块 16 处
        PlacementUtils.register(ctx, ORE_CRYSTAL, crystalHolder,
                commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        PlacementUtils.register(ctx, ORE_CRYSTAL_DEEP, crystalDeepHolder,
                commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));
    }



    private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }
    // 我们没有使用这个，对应了原版的稀有矿物生成
    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }

    public static ResourceKey<PlacedFeature> createKey(String pKey) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(BH3.MOD_ID,pKey));
    }
}

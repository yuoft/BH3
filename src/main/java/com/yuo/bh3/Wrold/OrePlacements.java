package com.yuo.bh3.Wrold;

import com.yuo.bh3.BH3;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacements {
    // 创建对应的placement的资源键
    public static final ResourceKey<PlacedFeature> ORE_ENERGY = createKey("ore_energy");
    public static final ResourceKey<PlacedFeature> ORE_CRYSTAL = createKey("ore_crystal");
    public static final ResourceKey<PlacedFeature> ORE_ENERGY_NETHER = createKey("ore_energy_nether");
    public static final ResourceKey<PlacedFeature> ORE_ENERGY_END = createKey("ore_energy_end");

    // BootstapContext数据生成时候传入上下文对象，我们数据生成时候再说
    public static void bootstrap(BootstapContext<PlacedFeature> pContext) {
        // 我们通过上下文获得我们的设置的个矿物的configurationfeature
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = pContext.lookup(Registries.CONFIGURED_FEATURE);
        // 通过上下文获得的是一个HolderGetter，这个HolderGetter是对ConfiguredFeature主要用于处理null的问题，感兴趣可以去了解下，不过这里我们写了对应的ConfigurationFeature所有获得不会是空，就可以拿到对应的Holder
        Holder<ConfiguredFeature<?, ?>> oreIronHolder = holdergetter.getOrThrow(OreFeatures.ORE_ENERGY);
        Holder<ConfiguredFeature<?, ?>> oreIronNetherHolder = holdergetter.getOrThrow(OreFeatures.ORE_ENERGY_NETHER);
        Holder<ConfiguredFeature<?, ?>> oreGoldHolder = holdergetter.getOrThrow(OreFeatures.ORE_ENERGY_END);
        Holder<ConfiguredFeature<?, ?>> oreGoldNetherHolder = holdergetter.getOrThrow(OreFeatures.ORE_CRYSTAL);
        // 让后我们就可以使用PlacementUtils的register方法，注册我们的PlacedFeature
        // 第一个参数就是上下文，第二个参数是对应ConfiguredFeature的holder，第三个参数是list的PlacementModifier，说明你的PlacedFeature的设置内容，
        // 这里commonOrePlacement是指设置了一系列的PlacementModifier，对应的是常规的矿物的生成，和原版的一样。
        // 对于PlacementModifier有很多的子类，你可以去继承关系中看
        // 这里我们用的一个是HeightRangePlacement，是指设置生成的最低高度和最高的高度。
        // 对于commonOrePlacement方法中用的CountPlacement则是设置一个数值
        // HeightRangePlacement的uniform方法是指从低到高平均生成，还有一个三角的，是指中间生成多，两边生成少。
        PlacementUtils.register(pContext, ORE_ENERGY, oreIronHolder, commonOrePlacement(8,
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128))));
        PlacementUtils.register(pContext, ORE_ENERGY_NETHER, oreIronNetherHolder, commonOrePlacement(10,
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(96))));
        PlacementUtils.register(pContext, ORE_ENERGY_END, oreGoldNetherHolder, commonOrePlacement(6,
                HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(128))));
        PlacementUtils.register(pContext, ORE_CRYSTAL, oreGoldHolder, commonOrePlacement(16,
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(80))));
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

package com.yuo.bh3.Data;

import com.yuo.bh3.Items.BH3Items;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModDataRecipes extends RecipeProvider {
    public ModDataRecipes(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        // 水晶 = 钻石 + 绿宝石 + 崩坏能碎片
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BH3Items.crystal1.get())
                .requires(Items.DIAMOND).requires(Items.EMERALD).requires(BH3Items.honkaiChip.get())
                .unlockedBy("has_item", has(BH3Items.crystal1.get())).save(consumer);

        // 水晶5
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.crystal5.get())
                .pattern("zzz")
                .pattern("z z")
                .pattern("zzz")
                .define('z', BH3Items.crystal1.get())
                .unlockedBy("has_item", has(BH3Items.crystal5.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.crystal25.get())
                .pattern("zzz")
                .pattern("z z")
                .pattern("zzz")
                .define('z', BH3Items.crystal5.get())
                .unlockedBy("has_item", has(BH3Items.crystal25.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.crystal50.get())
                .pattern("z z")
                .pattern(" z ")
                .pattern("z z")
                .define('z', BH3Items.crystal25.get())
                .unlockedBy("has_item", has(BH3Items.crystal50.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.crystal100.get())
                .pattern("z z")
                .pattern(" z ")
                .pattern("z z")
                .define('z', BH3Items.crystal50.get())
                .unlockedBy("has_item", has(BH3Items.crystal100.get())).save(consumer);

        // 补给卡
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.precisionCard.get())
                .pattern(" z ")
                .pattern("zyz")
                .pattern(" z ")
                .define('y', Items.WRITABLE_BOOK).define('z', BH3Items.crystal100.get())
                .unlockedBy("has_item", has(BH3Items.precisionCard.get())).save(consumer);

        // 应急复苏针
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.recoveryNeedle.get())
                .pattern(" z ")
                .pattern("zyz")
                .pattern(" z ")
                .define('z', BH3Items.honkaiChip.get()).define('y', Items.TOTEM_OF_UNDYING)
                .unlockedBy("has_item", has(BH3Items.recoveryNeedle.get())).save(consumer);

        // 西瓜片
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BH3Items.waterMelon.get())
                .pattern(" z ")
                .pattern("zyz")
                .pattern(" z ")
                .define('z', BH3Items.honkaiChip.get()).define('y', Items.MELON_SLICE)
                .unlockedBy("has_item", has(BH3Items.waterMelon.get())).save(consumer);
        //汽水
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BH3Items.sodaPop.get())
                .pattern("xz ")
                .pattern("zy ")
                .pattern("   ")
                .define('x', BH3Items.waterMelon.get()).define('z', Items.GLISTERING_MELON_SLICE)
                .define('y', PotionUtils.setPotion(new ItemStack(BH3Items.sodaPop.get()), Potions.WATER).getItem())
                .unlockedBy("has_item", has(BH3Items.sodaPop.get())).save(consumer);

        //学习芯片
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BH3Items.advancedChips.get())
                .requires(Items.EXPERIENCE_BOTTLE).requires(BH3Items.honkaiChip.get()).requires(BH3Items.baseChip.get())
                .unlockedBy("has_item", has(BH3Items.advancedChips.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BH3Items.seniorChips.get())
                .requires(Items.EXPERIENCE_BOTTLE).requires(BH3Items.honkaiChip.get()).requires(BH3Items.advancedChips.get())
                .unlockedBy("has_item", has(BH3Items.seniorChips.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BH3Items.premiumChips.get())
                .requires(Items.EXPERIENCE_BOTTLE).requires(Items.EXPERIENCE_BOTTLE)
                .requires(BH3Items.honkaiChip.get()).requires(BH3Items.seniorChips.get())
                .unlockedBy("has_item", has(BH3Items.premiumChips.get())).save(consumer);

        // 三分结晶
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.tripleCrystal.get())
                .pattern("zzz")
                .pattern("z z")
                .pattern("zzz")
                .define('z', BH3Items.honkaiChip.get())
                .unlockedBy("has_item", has(BH3Items.tripleCrystal.get())).save(consumer);

        // 崩坏结晶
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.honkaiCrystal.get())
                .pattern(" z ")
                .pattern("z z")
                .pattern(" z ")
                .define('z', BH3Items.tripleCrystal.get())
                .unlockedBy("has_item", has(BH3Items.honkaiCrystal.get())).save(consumer);

        // 合成材料
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.titaniumAlloyBarrel.get())
                .pattern(" yz")
                .pattern("yxy")
                .pattern("zy ")
                .define('x', Items.ECHO_SHARD).define('y', Items.IRON_INGOT).define('z', Items.ENDER_PEARL)
                .unlockedBy("has_item", has(BH3Items.titaniumAlloyBarrel.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.smallReactor.get())
                .pattern(" yz")
                .pattern("yxy")
                .pattern("zy ")
                .define('x', Items.NETHER_STAR).define('y', Items.AMETHYST_SHARD).define('z', Items.DAYLIGHT_DETECTOR)
                .unlockedBy("has_item", has(BH3Items.smallReactor.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.nanoCeramic.get())
                .pattern(" yz")
                .pattern("yxy")
                .pattern("zy ")
                .define('x', Items.LAVA_BUCKET).define('y', ItemTags.TERRACOTTA).define('z', Items.BLAZE_ROD)
                .unlockedBy("has_item", has(BH3Items.nanoCeramic.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.brokenBlade.get())
                .pattern(" a ")
                .pattern("yxz")
                .pattern(" b ")
                .define('x', Items.TNT).define('y', Items.DIAMOND_SWORD).define('z', Items.GOLDEN_SWORD)
                .define('a', Items.IRON_SWORD).define('b', Items.OBSIDIAN)
                .unlockedBy("has_item", has(BH3Items.brokenBlade.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.phaseTransferMirror.get())
                .pattern("zzz")
                .pattern("xyx")
                .pattern("zzz")
                .define('x', BH3Items.nanoCeramic.get()).define('y', Items.CONDUIT).define('z', Tags.Items.GLASS_PANES)
                .unlockedBy("has_item", has(BH3Items.phaseTransferMirror.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.einsteinRingmagnet.get())
                .pattern(" z ")
                .pattern("xyx")
                .pattern(" z ")
                .define('x', BH3Items.titaniumAlloyBarrel.get()).define('y', BH3Items.smallReactor.get()).define('z', BH3Items.nanoCeramic.get())
                .unlockedBy("has_item", has(BH3Items.einsteinRingmagnet.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.spaceLens.get())
                .pattern(" z ")
                .pattern("xyx")
                .pattern(" z ")
                .define('x', BH3Items.phaseTransferMirror.get()).define('y', BH3Items.smallReactor.get()).define('z', BH3Items.nanoCeramic.get())
                .unlockedBy("has_item", has(BH3Items.spaceLens.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.soulTeel.get())
                .pattern(" z ")
                .pattern("xyx")
                .pattern(" z ")
                .define('x', BH3Items.einsteinRingmagnet.get()).define('y', BH3Items.honkaiCrystal.get()).define('z', BH3Items.spaceLens.get())
                .unlockedBy("has_item", has(BH3Items.soulTeel.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.superHydrogenium.get())
                .pattern("zxz")
                .pattern("xyx")
                .pattern("zxz")
                .define('x', Items.NETHERITE_INGOT).define('y', Items.BLUE_ICE).define('z', BH3Items.nanoCeramic.get())
                .unlockedBy("has_item", has(BH3Items.superHydrogenium.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.fluidAlloy.get())
                .pattern("zxz")
                .pattern("xyx")
                .pattern("mxm")
                .define('x', Items.NETHERITE_INGOT).define('y', BH3Items.superHydrogenium.get())
                .define('z', Items.LAVA_BUCKET).define('m', BH3Items.brokenBlade.get())
                .unlockedBy("has_item", has(BH3Items.fluidAlloy.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.fluidAlloyBlock.get())
                .pattern("zzz")
                .pattern("zxz")
                .pattern("zzz")
                .define('z', BH3Items.fluidAlloy.get()).define('x', BH3Items.honkaiCrystal.get())
                .unlockedBy("has_item", has(BH3Items.fluidAlloyBlock.get())).save(consumer);

        weaponRecipe(consumer);
        oreSmelting(consumer);
        discRecipe(consumer);
    }

    /**
     * 武器合成+超限升级配方
     */
    private void weaponRecipe(Consumer<FinishedRecipe> consumer){
        // 用流体合金块合成普通武器
        Item SWORD = BH3Items.weaponMoldSword.get();
        Item NB_SWORD = Items.NETHERITE_SWORD;

        weaponSmithing(consumer, BH3Items.bh3_CBZ,   SWORD, NB_SWORD);     // 磁暴·斩
        weaponSmithing(consumer, BH3Items.bh3_CNZY,  SWORD, NB_SWORD);  // 澄凝之钥
        weaponSmithing(consumer, BH3Items.bh3_CRY,   SWORD, NB_SWORD);  // 妖刀·赤染樱
        weaponSmithing(consumer, BH3Items.bh3_DZQL,  SWORD, NB_SWORD);       // 涤罪七雷·鸣
        weaponSmithing(consumer, BH3Items.bh3_FLD,   SWORD, NB_SWORD);       // 仿灵刀·冰昙天
        weaponSmithing(consumer, BH3Items.bh3_TJZY,  SWORD, NB_SWORD);      // 天殛之钥
        weaponSmithing(consumer, BH3Items.bh3_YYQW,  SWORD, NB_SWORD);      // 夜宴蔷薇
        Item BIG_SWORD = BH3Items.weaponMoldBigsword.get();
        Item NB_AXE = Items.NETHERITE_AXE;

        weaponSmithing(consumer, BH3Items.bh3_JM,  BIG_SWORD, NB_AXE);    // 劫灭
        weaponSmithing(consumer, BH3Items.bh3_SYJ, BIG_SWORD, NB_AXE);   // 神陨剑·史尔特尔
        Item LANCE = BH3Items.weaponMoldLance.get();
        Item TRIDENT = Items.TRIDENT;

        weaponSmithing(consumer, BH3Items.bh3_FNMZ,    LANCE, TRIDENT);  // 凡念梦兆
        weaponSmithing(consumer, BH3Items.bh3_HYBH,    LANCE, TRIDENT);   // 黑渊白花
        weaponSmithing(consumer, BH3Items.bh3_ZXX,     LANCE, TRIDENT);          // 星枪·主序星
        weaponSmithing(consumer, BH3Items.bh3_YJZHLE,  LANCE, TRIDENT);   // 永寂之赫勒尔
        weaponSmithing(consumer, BH3Items.bh3_ZYDHG,   LANCE, TRIDENT);             // 子夜的黑光
        Item SICKLE = BH3Items.weaponMoldSickle.get();
        Item NB_HOE = Items.NETHERITE_HOE;

        weaponSmithing(consumer, BH3Items.bh3_AXYW, SICKLE, NB_HOE); // 安息鸢尾
        weaponSmithing(consumer, BH3Items.bh3_BAX,  SICKLE, NB_HOE);         // 星刃·白矮星
        weaponSmithing(consumer, BH3Items.bh3_SYZ,  SICKLE, NB_HOE);       // 裁星刃·双鱼座
        weaponSmithing(consumer, BH3Items.bh3_BAZF, SICKLE, NB_HOE);      // 彼岸之扉
        weaponSmithing(consumer, BH3Items.bh3_HSZS, SICKLE, NB_HOE);            // 恒霜之斯卡蒂
        weaponSmithing(consumer, BH3Items.bh3_XSZY, SICKLE, NB_HOE);       // 撷生之谣
        weaponSmithing(consumer, BH3Items.bh3_XYZM, SICKLE, NB_HOE);         // 血渊之眸
        Item BOW_MOLD = BH3Items.weaponMoldBow.get();
        Item BOW = Items.BOW;

        weaponSmithing(consumer, BH3Items.bh3_WSDFH, BOW_MOLD, BOW);   // 往世的飞花
        weaponSmithing(consumer, BH3Items.bh3_ZWZY,  BOW_MOLD, BOW);     // 真我之钥

        // 用魂钢升级普通武器 → 超限武器(升级模版，基础武器，升级材料，分类，输出)
        ultraSmithing(consumer, BH3Items.bh3_ZCB,    SWORD, BH3Items.bh3_CBZ);     // 重磁暴·斩
        ultraSmithing(consumer, BH3Items.bh3_CNZJ,   SWORD, BH3Items.bh3_CNZY);    // 澄凝之境·自性纯一
        ultraSmithing(consumer, BH3Items.bh3_HYD,    SWORD, BH3Items.bh3_CRY);     // 魂妖刀·血樱寂灭
        ultraSmithing(consumer, BH3Items.bh3_DZQLM,  SWORD, BH3Items.bh3_DZQL);    // 涤罪七雷·鸣雷见
        ultraSmithing(consumer, BH3Items.bh3_YLD,    SWORD, BH3Items.bh3_FLD);     // 御灵刀·寒狱冰天
        ultraSmithing(consumer, BH3Items.bh3_TJZJ,   SWORD, BH3Items.bh3_TJZY);    // 天殛之境·裁决
        ultraSmithing(consumer, BH3Items.bh3_YYQWX,  SWORD, BH3Items.bh3_YYQW);    // 夜宴蔷薇·虚之冕

        ultraSmithing(consumer, BH3Items.bh3_JMWJ, BIG_SWORD, BH3Items.bh3_JM);    // 劫灭无烬
        ultraSmithing(consumer, BH3Items.bh3_NYJ,  BIG_SWORD, BH3Items.bh3_SYJ);   // 涅炎剑·史尔特尔

        ultraSmithing(consumer, BH3Items.bh3_FNMZC, LANCE, BH3Items.bh3_FNMZ);     // 凡念梦兆·诸相自为
        ultraSmithing(consumer, BH3Items.bh3_HYBHC,   LANCE, BH3Items.bh3_HYBH);    // 黑渊白花·创灭螺旋
        ultraSmithing(consumer, BH3Items.bh3_RMZ,     LANCE, BH3Items.bh3_ZXX);     // 贯星枪·人马座
        ultraSmithing(consumer, BH3Items.bh3_ZJZHLE,  LANCE, BH3Items.bh3_YJZHLE);  // 湛寂之赫勒尔
        ultraSmithing(consumer, BH3Items.bh3_ZYDATJN, LANCE, BH3Items.bh3_ZYDHG);   // 子夜的阿塔吉娜

        ultraSmithing(consumer, BH3Items.bh3_MFXX,   SICKLE, BH3Items.bh3_AXYW);    // 冥府携香
        ultraSmithing(consumer, BH3Items.bh3_BAZFL,  SICKLE, BH3Items.bh3_BAZF);    // 彼岸之扉·连理
        ultraSmithing(consumer, BH3Items.bh3_TSZS,   SICKLE, BH3Items.bh3_HSZS);    // 天霜之斯卡蒂
        ultraSmithing(consumer, BH3Items.bh3_XSZJ,   SICKLE, BH3Items.bh3_XSZY);    // 撷生之径·你我如一
        ultraSmithing(consumer, BH3Items.bh3_XYZNY,  SICKLE, BH3Items.bh3_XYZM);    // 血渊之眸·如一

        ultraSmithing(consumer, BH3Items.bh3_WSDFHA, BOW_MOLD, BH3Items.bh3_WSDFH); // 往世的飞花·爱之诗
        ultraSmithing(consumer, BH3Items.bh3_ZWZJ,   BOW_MOLD, BH3Items.bh3_ZWZY);  // 真我之境·无瑕回归
    }

    /**
     * 唱片配方
     */
    private void discRecipe(Consumer<FinishedRecipe> consumer){
        // 普通
        musicDisc(consumer, BH3Items.discBcy,       Items.IRON_INGOT,        "disc_bcy");
        musicDisc(consumer, BH3Items.discDacapo,    Items.COPPER_INGOT,      "disc_dacapo");
        musicDisc(consumer, BH3Items.discIras17514, Items.COAL,              "disc_iras17514");
        musicDisc(consumer, BH3Items.discNoceiling, Items.REDSTONE,          "disc_noceiling");
        musicDisc(consumer, BH3Items.discWlzj,      Items.LAPIS_LAZULI,      "disc_wlzj");
        musicDisc(consumer, BH3Items.discQyx,       Items.QUARTZ,            "disc_qyx");

        // 进阶
        musicDisc(consumer, BH3Items.discBefall,      Items.GOLD_INGOT,          "disc_befall");
        musicDisc(consumer, BH3Items.discBhsjdgj,     Items.GLOWSTONE_DUST,      "disc_bhsjdgj");
        musicDisc(consumer, BH3Items.discCyberangel,  Items.AMETHYST_SHARD,      "disc_cyberangel");
        musicDisc(consumer, BH3Items.discDualego,     Items.ENDER_PEARL,         "disc_dualego");
        musicDisc(consumer, BH3Items.discGirlinside,  Items.BLAZE_POWDER,        "disc_girlinside");
        musicDisc(consumer, BH3Items.discLz,          Items.RABBIT_HIDE,         "disc_lz");
        musicDisc(consumer, BH3Items.discMoonhalo,    Items.EXPERIENCE_BOTTLE,   "disc_moonhalo");
        musicDisc(consumer, BH3Items.discNightglow,   Items.PHANTOM_MEMBRANE,    "disc_nightglow");
        musicDisc(consumer, BH3Items.discQnzy,        Items.HONEY_BOTTLE,        "disc_qnzy");
        musicDisc(consumer, BH3Items.discReburn,      Items.NAUTILUS_SHELL,      "disc_reburn");

        // 稀有
        musicDisc(consumer, BH3Items.discRegression,  Items.DIAMOND,             "disc_regression");
        musicDisc(consumer, BH3Items.discReoracle,    Items.GHAST_TEAR,          "disc_reoracle");
        musicDisc(consumer, BH3Items.discRubia,       Items.HEART_OF_THE_SEA,    "disc_rubia");
        musicDisc(consumer, BH3Items.discStarfall,    Items.DRAGON_BREATH,       "disc_starfall");

        // 史诗
        musicDisc(consumer, BH3Items.discTrue0,       Items.NETHER_STAR,         "disc_true0");
        musicDisc(consumer, BH3Items.discXynxszr,     Items.ECHO_SHARD,          "disc_xynxszr");
        musicDisc(consumer, BH3Items.discZcfzq,       Items.WITHER_SKELETON_SKULL, "disc_zcfzq");

        // 传说：单独写（双材料）
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BH3Items.discOaths.get())
                .pattern("pnp")
                .pattern("pfp")
                .pattern("pdp")
                .define('p', Items.PAPER)
                .define('f', Items.DISC_FRAGMENT_5)
                .define('n', Items.NETHER_STAR)
                .define('d', Items.DIAMOND)
                .unlockedBy("has_item", has(BH3Items.discOaths.get()))
                .save(consumer, "bh3:music_disc/disc_oaths");
    }

    /**
     * 矿石熔炼配方
     */
    private void oreSmelting(Consumer<FinishedRecipe> consumer){
        //输入，分类，输出，经验，时间
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BH3Items.energyOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.5f, 200)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_smelting");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BH3Items.energyOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BH3Items.energyDeepOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.5f, 200)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_deep_smelting");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BH3Items.energyDeepOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_deep_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BH3Items.energyNetherOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.5f, 200)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_neter_smelting");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BH3Items.energyNetherOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_neter_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BH3Items.energyEndOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.5f, 200)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_end_smelting");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BH3Items.energyEndOre.get()), RecipeCategory.MISC,
                        BH3Items.honkaiChip.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BH3Items.honkaiChip.get()))
                .save(consumer, "bh3:honkai_chip_end_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BH3Items.crystalOre.get()), RecipeCategory.MISC,
                        BH3Items.crystal1.get(), 0.5f, 200)
                .unlockedBy("has_item", has(BH3Items.crystal1.get()))
                .save(consumer, "bh3:crystal_smelting");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BH3Items.crystalOre.get()), RecipeCategory.MISC,
                        BH3Items.crystal1.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BH3Items.crystal1.get()))
                .save(consumer, "bh3:crystal_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BH3Items.crystalDeepOre.get()), RecipeCategory.MISC,
                        BH3Items.crystal1.get(), 0.5f, 200)
                .unlockedBy("has_item", has(BH3Items.crystal1.get()))
                .save(consumer, "bh3:crystal_deep_smelting");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BH3Items.crystalDeepOre.get()), RecipeCategory.MISC,
                        BH3Items.crystal1.get(), 0.7f, 100)
                .unlockedBy("has_item", has(BH3Items.crystal1.get()))
                .save(consumer, "bh3:crystal_deep_blasting");
    }

    /**
     * 唱片配方：纸 + 碎片5 + 辅助材料
     */
    private void musicDisc(Consumer<FinishedRecipe> consumer, RegistryObject<Item> disc, Item addition, String id) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, disc.get())
                .pattern(" p ")
                .pattern("pfp")
                .pattern(" a ")
                .define('p', Items.PAPER)
                .define('f', Items.DISC_FRAGMENT_5)
                .define('a', addition)
                .unlockedBy("has_item", has(disc.get()))
                .save(consumer, "bh3:music_disc/" + id);
    }

    /** 普通武器锻造：模具 + 基础工具 + 独特材料 */
    private void weaponSmithing(Consumer<FinishedRecipe> consumer, RegistryObject<Item> output, Item mold, Item base) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(mold),
                        Ingredient.of(base),
                        Ingredient.of(BH3Items.fluidAlloyBlock.get()),
                        RecipeCategory.COMBAT,
                        output.get())
                .unlocks("has_item", has(BH3Items.fluidAlloyBlock.get()))
                .save(consumer, "bh3:weapon/" + output.getId().getPath());
    }

    /** 超限升级：模具 + 普通武器 + 灵魂钢（不变） */
    private void ultraSmithing(Consumer<FinishedRecipe> consumer, RegistryObject<Item> output, Item mold, RegistryObject<Item> baseWeapon) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(mold),
                        Ingredient.of(baseWeapon.get()),
                        Ingredient.of(BH3Items.soulTeel.get()),
                        RecipeCategory.COMBAT,
                        output.get())
                .unlocks("has_item", has(BH3Items.soulTeel.get()))
                .save(consumer, "bh3:ultra/" + output.getId().getPath());
    }
}

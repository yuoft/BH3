package com.yuo.bh3.Items;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Blocks.BH3Blocks;
import com.yuo.bh3.SoundRegistry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

//物品注册管理器
public class BH3Items {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BH3.MOD_ID);

	public static RegistryObject<Item> precisionCard = ITEMS.register("precision_card", PrecisionCard::new);

	public static RegistryObject<Item> crystal1 = ITEMS.register("crystal1", BH3BaseItem::new);
	public static RegistryObject<Item> crystal5 = ITEMS.register("crystal5", BH3BaseItem::new);
	public static RegistryObject<Item> crystal25 = ITEMS.register("crystal25", BH3BaseItem::new);
	public static RegistryObject<Item> crystal50 = ITEMS.register("crystal50", BH3BaseItem::new);
	public static RegistryObject<Item> crystal100 = ITEMS.register("crystal100", BH3BaseItem::new);

	public static RegistryObject<Item> baseChip = ITEMS.register("base_chip", Chips::new);
	public static RegistryObject<Item> advancedChips = ITEMS.register("advanced_chips", Chips::new);
	public static RegistryObject<Item> seniorChips = ITEMS.register("senior_chips", Chips::new);
	public static RegistryObject<Item> premiumChips = ITEMS.register("premium_chips", Chips::new);

	public static RegistryObject<Item> einsteinRingmagnet = ITEMS.register("einstein_ringmagnet", BH3BaseItem::new);
	public static RegistryObject<Item> superHydrogenium = ITEMS.register("super_hydrogenium", BH3BaseItem::new);
	public static RegistryObject<Item> phaseTransferMirror = ITEMS.register("phase_transfer_mirror", BH3BaseItem::new);
	public static RegistryObject<Item> brokenBlade = ITEMS.register("broken_blade", BH3BaseItem::new);
	public static RegistryObject<Item> nanoCeramic = ITEMS.register("nano_ceramic", BH3BaseItem::new);
	public static RegistryObject<Item> recoveryNeedle = ITEMS.register("recovery_needle", BH3BaseItem::new);
	public static RegistryObject<Item> smallReactor = ITEMS.register("small_reactor", BH3BaseItem::new);
	public static RegistryObject<Item> titaniumAlloyBarrel = ITEMS.register("titanium_alloy_barrel", BH3BaseItem::new);
	public static RegistryObject<Item> soulTeel = ITEMS.register("soul_teel", BH3BaseItem::new);
	public static RegistryObject<Item> spaceLens = ITEMS.register("space_lens", BH3BaseItem::new);

	public static RegistryObject<Item> fluidAlloyBlock = ITEMS.register("fluid_alloy_block", BH3BaseItem::new);
	public static RegistryObject<Item> fluidAlloy = ITEMS.register("fluid_alloy", BH3BaseItem::new);
	public static RegistryObject<Item> honkaiCrystal = ITEMS.register("honkai_crystal", BH3BaseItem::new);
	public static RegistryObject<Item> tripleCrystal = ITEMS.register("triple_crystal", BH3BaseItem::new);
	public static RegistryObject<Item> honkaiChip = ITEMS.register("honkai_chip", BH3BaseItem::new);

	public static RegistryObject<Item> weaponMold0 = ITEMS.register("weapon_mold0", BH3BaseItem::new);
	public static RegistryObject<Item> weaponMold1 = ITEMS.register("weapon_mold1", BH3BaseItem::new);
	public static RegistryObject<Item> weaponMold2 = ITEMS.register("weapon_mold2", BH3BaseItem::new);
	public static RegistryObject<Item> weaponMold3 = ITEMS.register("weapon_mold3", BH3BaseItem::new);
	public static RegistryObject<Item> weaponMold4 = ITEMS.register("weapon_mold4", BH3BaseItem::new);

	public static RegistryObject<Item> waterMelon = ITEMS.register("water_melon",
			() -> new BH3Food(new FoodProperties.Builder().nutrition(3).saturationMod(9).build()));
	public static RegistryObject<Item> sodaPop = ITEMS.register("soda_pop",
			() -> new BH3Food(new FoodProperties.Builder().nutrition(6).saturationMod(18).build()));
	public static RegistryObject<Item> staminaPotion = ITEMS.register("stamina_potion",
			() -> new BH3Food(new FoodProperties.Builder().nutrition(10).saturationMod(30).build()));

	// 武器
	public static RegistryObject<Item> bh3_AXYW = ITEMS.register("bh3_axyw", () -> new BH3WeaponSickle(WeaponAttributes.AXYW));
	public static RegistryObject<Item> bh3_BAX = ITEMS.register("bh3_bax", () -> new BH3WeaponSickle(WeaponAttributes.BAX));
	public static RegistryObject<Item> bh3_BAZF = ITEMS.register("bh3_bazf", () -> new BH3WeaponSickle(WeaponAttributes.BAZF));
	public static RegistryObject<Item> bh3_BAZFL = ITEMS.register("bh3_bazfl", () -> new BH3WeaponSickle(WeaponAttributes.BAZFL));
	public static RegistryObject<Item> bh3_CBZ = ITEMS.register("bh3_cbz", () -> new BH3WeaponSword(WeaponAttributes.CBZ));
	public static RegistryObject<Item> bh3_CNZJ = ITEMS.register("bh3_cnzj", () -> new BH3WeaponSword(WeaponAttributes.CNZJ));
	public static RegistryObject<Item> bh3_CNZY = ITEMS.register("bh3_cnzy", () -> new BH3WeaponSword(WeaponAttributes.CNZY));
	public static RegistryObject<Item> bh3_CRY = ITEMS.register("bh3_cry", () -> new BH3WeaponSword(WeaponAttributes.CRY));
	public static RegistryObject<Item> bh3_DZQL = ITEMS.register("bh3_dzql", () -> new BH3WeaponSword(WeaponAttributes.DZQL));
	public static RegistryObject<Item> bh3_DZQLM = ITEMS.register("bh3_dzqlm", () -> new BH3WeaponSword(WeaponAttributes.DZQLM));
	public static RegistryObject<Item> bh3_FLD = ITEMS.register("bh3_fld", () -> new FLD(WeaponAttributes.FLD));
	public static RegistryObject<Item> bh3_FNMZ = ITEMS.register("bh3_fnmz", () -> new BH3WeaponLance(WeaponAttributes.FNMZ));
	public static RegistryObject<Item> bh3_FNMZC = ITEMS.register("bh3_fnmzc", () -> new BH3WeaponLance(WeaponAttributes.FNMZC));
	public static RegistryObject<Item> bh3_HSZS = ITEMS.register("bh3_hszs", () -> new BH3WeaponSickle(WeaponAttributes.HSZS));
	public static RegistryObject<Item> bh3_HYBH = ITEMS.register("bh3_hybh", () -> new BH3WeaponLance(WeaponAttributes.HYBH));
	public static RegistryObject<Item> bh3_HYBHC = ITEMS.register("bh3_hybhc", () -> new BH3WeaponLance(WeaponAttributes.HYBHC));
	public static RegistryObject<Item> bh3_HYD = ITEMS.register("bh3_hyd", () -> new BH3WeaponSword(WeaponAttributes.HYD));
	public static RegistryObject<Item> bh3_JM = ITEMS.register("bh3_jm", () -> new BH3WeaponBigSword(WeaponAttributes.JM));
	public static RegistryObject<Item> bh3_JMWJ = ITEMS.register("bh3_jmwj", () -> new BH3WeaponBigSword(WeaponAttributes.JMWJ));
	public static RegistryObject<Item> bh3_MFXX = ITEMS.register("bh3_mfxx", () -> new BH3WeaponSickle(WeaponAttributes.MFXX));
	public static RegistryObject<Item> bh3_NYJ = ITEMS.register("bh3_nyj", () -> new BH3WeaponBigSword(WeaponAttributes.NYJ));
	public static RegistryObject<Item> bh3_RMZ = ITEMS.register("bh3_rmz", () -> new BH3WeaponLance(WeaponAttributes.RMZ));
	public static RegistryObject<Item> bh3_SYJ = ITEMS.register("bh3_syj", () -> new BH3WeaponBigSword(WeaponAttributes.SYJ));
	public static RegistryObject<Item> bh3_SYZ = ITEMS.register("bh3_syz", () -> new BH3WeaponSickle(WeaponAttributes.SYZ));
	public static RegistryObject<Item> bh3_TJZJ = ITEMS.register("bh3_tjzj", () -> new BH3WeaponSword(WeaponAttributes.TJZJ));
	public static RegistryObject<Item> bh3_TJZY = ITEMS.register("bh3_tjzy", () -> new BH3WeaponSword(WeaponAttributes.TJZY));
	public static RegistryObject<Item> bh3_TSZS = ITEMS.register("bh3_tszs", () -> new BH3WeaponSickle(WeaponAttributes.TSZS));
	public static RegistryObject<Item> bh3_WSDFH = ITEMS.register("bh3_wsdfh", () -> new BH3WeaponBow(WeaponAttributes.WSDFH));
	public static RegistryObject<Item> bh3_WSDFHA = ITEMS.register("bh3_wsdfha", () -> new BH3WeaponBow(WeaponAttributes.WSDFHA));
	public static RegistryObject<Item> bh3_XSZJ = ITEMS.register("bh3_xszj", () -> new BH3WeaponSickle(WeaponAttributes.XSZJ));
	public static RegistryObject<Item> bh3_XSZY = ITEMS.register("bh3_xszy", () -> new BH3WeaponSickle(WeaponAttributes.XSZY));
	public static RegistryObject<Item> bh3_XYZM = ITEMS.register("bh3_xyzm", () -> new BH3WeaponSickle(WeaponAttributes.XYZM));
	public static RegistryObject<Item> bh3_XYZNY = ITEMS.register("bh3_xyzmy", () -> new BH3WeaponSickle(WeaponAttributes.XYZMY));
	public static RegistryObject<Item> bh3_YJZHLE = ITEMS.register("bh3_yjzhle", () -> new BH3WeaponLance(WeaponAttributes.YJZHLE));
	public static RegistryObject<Item> bh3_YLD = ITEMS.register("bh3_yld", () -> new BH3WeaponSword(WeaponAttributes.YLD));
	public static RegistryObject<Item> bh3_YYQW = ITEMS.register("bh3_yyqw", () -> new BH3WeaponSword(WeaponAttributes.YYQW));
	public static RegistryObject<Item> bh3_YYQWX = ITEMS.register("bh3_yyqwx", () -> new BH3WeaponSword(WeaponAttributes.YYQWX));
	public static RegistryObject<Item> bh3_ZCB = ITEMS.register("bh3_zcb", () -> new BH3WeaponSword(WeaponAttributes.ZCB));
	public static RegistryObject<Item> bh3_ZJZHLE = ITEMS.register("bh3_zjzhle", () -> new BH3WeaponLance(WeaponAttributes.ZJZHLE));
	public static RegistryObject<Item> bh3_ZWZJ = ITEMS.register("bh3_zwzj", () -> new BH3WeaponBow(WeaponAttributes.ZWZJ));
	public static RegistryObject<Item> bh3_ZWZY = ITEMS.register("bh3_zwzy", () -> new BH3WeaponBow(WeaponAttributes.ZWZY));
	public static RegistryObject<Item> bh3_ZXX = ITEMS.register("bh3_zxx", () -> new BH3WeaponLance(WeaponAttributes.ZXX));
	public static RegistryObject<Item> bh3_ZYDATJN = ITEMS.register("bh3_zydatjn", () -> new BH3WeaponLance(WeaponAttributes.ZYDATJN));
	public static RegistryObject<Item> bh3_ZYDHG = ITEMS.register("bh3_zydhg", () -> new BH3WeaponLance(WeaponAttributes.ZYDHG));

	// 唱片
	private static final int baseTick = 20;
	public static RegistryObject<Item> discBcy = ITEMS.register("disc_bcy", () -> new BH3MusicDisc(() -> SoundRegistry.bcy.get(), baseTick * 206));
	public static RegistryObject<Item> discBefall = ITEMS.register("disc_befall", () -> new BH3MusicDisc(() -> SoundRegistry.befall.get(), baseTick * 191));
	public static RegistryObject<Item> discBhsjdgj = ITEMS.register("disc_bhsjdgj", () -> new BH3MusicDisc(() -> SoundRegistry.bhsjdgj.get(), baseTick * 189));
	public static RegistryObject<Item> discCyberangel = ITEMS.register("disc_cyberangel", () -> new BH3MusicDisc(() -> SoundRegistry.cyberangel.get(), baseTick * 180));
	public static RegistryObject<Item> discDacapo = ITEMS.register("disc_dacapo", () -> new BH3MusicDisc(() -> SoundRegistry.dacapo.get(), baseTick * 134));
	public static RegistryObject<Item> discDualego = ITEMS.register("disc_dualego", () -> new BH3MusicDisc(() -> SoundRegistry.dualego.get(), baseTick * 188));
	public static RegistryObject<Item> discGirlinside = ITEMS.register("disc_girlinside", () -> new BH3MusicDisc(() -> SoundRegistry.girlinside.get(), baseTick * 189));
	public static RegistryObject<Item> discIras17514 = ITEMS.register("disc_iras17514", () -> new BH3MusicDisc(() -> SoundRegistry.iras17514.get(), baseTick * 134));
	public static RegistryObject<Item> discLz = ITEMS.register("disc_lz", () -> new BH3MusicDisc(() -> SoundRegistry.lz.get(), baseTick * 176));
	public static RegistryObject<Item> discMoonhalo = ITEMS.register("disc_moonhalo", () -> new BH3MusicDisc(() -> SoundRegistry.moonhalo.get(), baseTick * 204));
	public static RegistryObject<Item> discNightglow = ITEMS.register("disc_nightglow", () -> new BH3MusicDisc(() -> SoundRegistry.nightglow.get(), baseTick * 183));
	public static RegistryObject<Item> discNoceiling = ITEMS.register("disc_noceiling", () -> new BH3MusicDisc(() -> SoundRegistry.noceiling.get(), baseTick * 130));
	public static RegistryObject<Item> discOaths = ITEMS.register("disc_oaths", () -> new BH3MusicDisc(() -> SoundRegistry.oaths.get(), baseTick * 240));
	public static RegistryObject<Item> discQnzy = ITEMS.register("disc_qnzy", () -> new BH3MusicDisc(() -> SoundRegistry.qnzy.get(), baseTick * 197));
	public static RegistryObject<Item> discQyx = ITEMS.register("disc_qyx", () -> new BH3MusicDisc(() -> SoundRegistry.qyx.get(), baseTick * 175));
	public static RegistryObject<Item> discReburn = ITEMS.register("disc_reburn", () -> new BH3MusicDisc(() -> SoundRegistry.reburn.get(), baseTick * 180));
	public static RegistryObject<Item> discRegression = ITEMS.register("disc_regression", () -> new BH3MusicDisc(() -> SoundRegistry.regression.get(), baseTick * 236));
	public static RegistryObject<Item> discReoracle = ITEMS.register("disc_reoracle", () -> new BH3MusicDisc(() -> SoundRegistry.reoracle.get(), baseTick * 200));
	public static RegistryObject<Item> discRubia = ITEMS.register("disc_rubia", () -> new BH3MusicDisc(() -> SoundRegistry.rubia.get(), baseTick * 194));
	public static RegistryObject<Item> discStarfall = ITEMS.register("disc_starfall", () -> new BH3MusicDisc(() -> SoundRegistry.starfall.get(), baseTick * 191));
	public static RegistryObject<Item> discTrue0 = ITEMS.register("disc_true0", () -> new BH3MusicDisc(() -> SoundRegistry.true0.get(), baseTick * 187));
	public static RegistryObject<Item> discWlzj = ITEMS.register("disc_wlzj", () -> new BH3MusicDisc(() -> SoundRegistry.wlzj.get(), baseTick * 163));
	public static RegistryObject<Item> discXynxszr = ITEMS.register("disc_xynxszr", () -> new BH3MusicDisc(() -> SoundRegistry.xynxszr.get(), baseTick * 201));
	public static RegistryObject<Item> discZcfzq = ITEMS.register("disc_zcfzq", () -> new BH3MusicDisc(() -> SoundRegistry.zcfzq.get(), baseTick * 288));

	public static final Item.Properties BLOCK_TAB = new Item.Properties();

	public static RegistryObject<BlockItem> energyOre = ITEMS.register("energy_ore", () -> new BlockItem(BH3Blocks.energyOre.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> energyEndOre = ITEMS.register("energy_end_ore", () -> new BlockItem(BH3Blocks.energyEndOre.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> energyNetherOre = ITEMS.register("energy_nether_ore", () -> new BlockItem(BH3Blocks.energyNetherOre.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> crystalOre = ITEMS.register("crystal_ore", () -> new BlockItem(BH3Blocks.crystalOre.get(), BLOCK_TAB));
}

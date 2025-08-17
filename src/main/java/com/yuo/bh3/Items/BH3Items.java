package com.yuo.bh3.Items;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Blocks.BH3Blocks;
import com.yuo.bh3.SoundRegistry;
import com.yuo.bh3.tab.BH3Tabs;
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

	public static RegistryObject<Item> crystal1 = ITEMS.register("crystal1", BH3Item::new);
	public static RegistryObject<Item> crystal5 = ITEMS.register("crystal5", BH3Item::new);
	public static RegistryObject<Item> crystal25 = ITEMS.register("crystal25", BH3Item::new);
	public static RegistryObject<Item> crystal50 = ITEMS.register("crystal50", BH3Item::new);
	public static RegistryObject<Item> crystal100 = ITEMS.register("crystal100", BH3Item::new);

	public static RegistryObject<Item> baseChip = ITEMS.register("base_chip", Chips::new);
	public static RegistryObject<Item> advancedChips = ITEMS.register("advanced_chips", Chips::new);
	public static RegistryObject<Item> seniorChips = ITEMS.register("senior_chips", Chips::new);
	public static RegistryObject<Item> premiumChips = ITEMS.register("premium_chips", Chips::new);

	public static RegistryObject<Item> einsteinRingmagnet = ITEMS.register("einstein_ringmagnet", BH3Item::new);
	public static RegistryObject<Item> superHydrogenium = ITEMS.register("super_hydrogenium", BH3Item::new);
	public static RegistryObject<Item> phaseTransferMirror = ITEMS.register("phase_transfer_mirror", BH3Item::new);
	public static RegistryObject<Item> brokenBlade = ITEMS.register("broken_blade", BH3Item::new);
	public static RegistryObject<Item> nanoCeramic = ITEMS.register("nano_ceramic", BH3Item::new);
	public static RegistryObject<Item> recoveryNeedle = ITEMS.register("recovery_needle", BH3Item::new);
	public static RegistryObject<Item> smallReactor = ITEMS.register("small_reactor", BH3Item::new);
	public static RegistryObject<Item> titaniumAlloyBarrel = ITEMS.register("titanium_alloy_barrel", BH3Item::new);
	public static RegistryObject<Item> soulTeel = ITEMS.register("soul_teel", BH3Item::new);
	public static RegistryObject<Item> spaceLens = ITEMS.register("space_lens", BH3Item::new);

	public static RegistryObject<Item> fluidAlloyBlock = ITEMS.register("fluid_alloy_block", BH3Item::new);
	public static RegistryObject<Item> fluidAlloy = ITEMS.register("fluid_alloy", BH3Item::new);
	public static RegistryObject<Item> honkaiCrystal = ITEMS.register("honkai_crystal", BH3Item::new);
	public static RegistryObject<Item> tripleCrystal = ITEMS.register("triple_crystal", BH3Item::new);
	public static RegistryObject<Item> honkaiChip = ITEMS.register("honkai_chip", BH3Item::new);

	public static RegistryObject<Item> weaponMold0 = ITEMS.register("weapon_mold0", BH3Item::new);
	public static RegistryObject<Item> weaponMold1 = ITEMS.register("weapon_mold1", BH3Item::new);
	public static RegistryObject<Item> weaponMold2 = ITEMS.register("weapon_mold2", BH3Item::new);
	public static RegistryObject<Item> weaponMold3 = ITEMS.register("weapon_mold3", BH3Item::new);
	public static RegistryObject<Item> weaponMold4 = ITEMS.register("weapon_mold4", BH3Item::new);

	public static RegistryObject<Item> waterMelon = ITEMS.register("water_melon",
			() -> new BH3Food(new FoodProperties.Builder().nutrition(3).saturationMod(9).build()));
	public static RegistryObject<Item> sodaPop = ITEMS.register("soda_pop",
			() -> new BH3Food(new FoodProperties.Builder().nutrition(6).saturationMod(18).build()));
	public static RegistryObject<Item> staminaPotion = ITEMS.register("stamina_potion",
			() -> new BH3Food(new FoodProperties.Builder().nutrition(10).saturationMod(30).build()));

	//武器
	public static RegistryObject<Item> AXYW = ITEMS.register("axyw", BH3WeaponSickle::new);
	public static RegistryObject<Item> BAX = ITEMS.register("bax", BH3WeaponSickle::new);
	public static RegistryObject<Item> BAZF = ITEMS.register("bazf", BH3WeaponSickle::new);
	public static RegistryObject<Item> BAZFL = ITEMS.register("bazfl", BH3WeaponSickle::new);
	public static RegistryObject<Item> CBZ = ITEMS.register("cbz", BH3WeaponSword::new);
	public static RegistryObject<Item> CNZJ = ITEMS.register("cnzj", BH3WeaponSword::new);
	public static RegistryObject<Item> CNZY = ITEMS.register("cnzy", BH3WeaponSword::new);
	public static RegistryObject<Item> CRY = ITEMS.register("cry", BH3WeaponSword::new);
	public static RegistryObject<Item> DZQL = ITEMS.register("dzql", BH3WeaponSword::new);
	public static RegistryObject<Item> DZQLM = ITEMS.register("dzqlm", BH3WeaponSword::new);
	public static RegistryObject<Item> FLD = ITEMS.register("fld", BH3WeaponSword::new);
	public static RegistryObject<Item> FNMZ = ITEMS.register("fnmz", BH3WeaponLance::new);
	public static RegistryObject<Item> FNMZC = ITEMS.register("fnmzc", BH3WeaponLance::new);
	public static RegistryObject<Item> HSZS = ITEMS.register("hszs", BH3WeaponSickle::new);
	public static RegistryObject<Item> HYBH = ITEMS.register("hybh", BH3WeaponLance::new);
	public static RegistryObject<Item> HYBHC = ITEMS.register("hybhc", BH3WeaponLance::new);
	public static RegistryObject<Item> HYD = ITEMS.register("hyd", BH3WeaponSword::new);
	public static RegistryObject<Item> JM = ITEMS.register("jm", BH3WeaponBigSword::new);
	public static RegistryObject<Item> JMWJ = ITEMS.register("jmwj", BH3WeaponBigSword::new);
	public static RegistryObject<Item> MFXX = ITEMS.register("mfxx", BH3WeaponSickle::new);
	public static RegistryObject<Item> NYJ = ITEMS.register("nyj", BH3WeaponBigSword::new);
	public static RegistryObject<Item> RMZ = ITEMS.register("rmz", BH3WeaponLance::new);
	public static RegistryObject<Item> SYJ = ITEMS.register("syj", BH3WeaponBigSword::new);
	public static RegistryObject<Item> SYZ = ITEMS.register("syz", BH3WeaponSickle::new);
	public static RegistryObject<Item> TJZJ = ITEMS.register("tjzj", BH3WeaponSword::new);
	public static RegistryObject<Item> TJZY = ITEMS.register("tjzy", BH3WeaponSword::new);
	public static RegistryObject<Item> TSZS = ITEMS.register("tszs", BH3WeaponSickle::new);
	public static RegistryObject<Item> WSDFH = ITEMS.register("wsdfh", BH3WeaponBow::new);
	public static RegistryObject<Item> WSDFHA = ITEMS.register("wsdfha", BH3WeaponBow::new);
	public static RegistryObject<Item> XSZJ = ITEMS.register("xszj", BH3WeaponSickle::new);
	public static RegistryObject<Item> XSZY = ITEMS.register("xszy", BH3WeaponSickle::new);
	public static RegistryObject<Item> XYZM = ITEMS.register("xyzm", BH3WeaponSickle::new);
	public static RegistryObject<Item> XYZNY = ITEMS.register("xyzmy", BH3WeaponSickle::new);
	public static RegistryObject<Item> YJZHLE = ITEMS.register("yjzhle", BH3WeaponLance::new);
	public static RegistryObject<Item> YLD = ITEMS.register("yld", BH3WeaponSword::new);
	public static RegistryObject<Item> YYQW = ITEMS.register("yyqw", BH3WeaponSword::new);
	public static RegistryObject<Item> YYQWX = ITEMS.register("yyqwx", BH3WeaponSword::new);
	public static RegistryObject<Item> ZCB = ITEMS.register("zcb", BH3WeaponSword::new);
	public static RegistryObject<Item> ZJZHLE = ITEMS.register("zjzhle", BH3WeaponLance::new);
	public static RegistryObject<Item> ZWZJ = ITEMS.register("zwzj", BH3WeaponBow::new);
	public static RegistryObject<Item> ZWZY = ITEMS.register("zwzy", BH3WeaponBow::new);
	public static RegistryObject<Item> ZXX = ITEMS.register("zxx", BH3WeaponLance::new);
	public static RegistryObject<Item> ZYDATJN = ITEMS.register("zydatjn", BH3WeaponLance::new);
	public static RegistryObject<Item> ZYDHG = ITEMS.register("zydhg", BH3WeaponLance::new);

	//唱片
	private static final int baseTick = 20;
	public static RegistryObject<Item> bcy = ITEMS.register("bcy", () -> new BH3MusicDisc(() -> SoundRegistry.bcy.get(), baseTick * 206));
	public static RegistryObject<Item> befall = ITEMS.register("befall", () -> new BH3MusicDisc(() -> SoundRegistry.befall.get(), baseTick * 191));
	public static RegistryObject<Item> bhsjdgj = ITEMS.register("bhsjdgj", () -> new BH3MusicDisc(() -> SoundRegistry.bhsjdgj.get(), baseTick * 189));
	public static RegistryObject<Item> cyberangel = ITEMS.register("cyberangel", () -> new BH3MusicDisc(() -> SoundRegistry.cyberangel.get(), baseTick * 180));
	public static RegistryObject<Item> dacapo = ITEMS.register("dacapo", () -> new BH3MusicDisc(() -> SoundRegistry.dacapo.get(), baseTick * 134));
	public static RegistryObject<Item> dualego = ITEMS.register("dualego", () -> new BH3MusicDisc(() -> SoundRegistry.dualego.get(), baseTick * 188));
	public static RegistryObject<Item> girlinside = ITEMS.register("girlinside", () -> new BH3MusicDisc(() -> SoundRegistry.girlinside.get(), baseTick * 189));
	public static RegistryObject<Item> iras17514 = ITEMS.register("iras17514", () -> new BH3MusicDisc(() -> SoundRegistry.iras17514.get(), baseTick * 134));
	public static RegistryObject<Item> lz = ITEMS.register("lz", () -> new BH3MusicDisc(() -> SoundRegistry.lz.get(), baseTick * 176));
	public static RegistryObject<Item> moonhalo = ITEMS.register("moonhalo", () -> new BH3MusicDisc(() -> SoundRegistry.moonhalo.get(), baseTick * 204));
	public static RegistryObject<Item> nightglow = ITEMS.register("nightglow", () -> new BH3MusicDisc(() -> SoundRegistry.nightglow.get(), baseTick * 183));
	public static RegistryObject<Item> noceiling = ITEMS.register("noceiling", () -> new BH3MusicDisc(() -> SoundRegistry.noceiling.get(), baseTick * 130));
	public static RegistryObject<Item> oaths = ITEMS.register("oaths", () -> new BH3MusicDisc(() -> SoundRegistry.oaths.get(), baseTick * 240));
	public static RegistryObject<Item> qnzy = ITEMS.register("qnzy", () -> new BH3MusicDisc(() -> SoundRegistry.qnzy.get(), baseTick * 197));
	public static RegistryObject<Item> qyx = ITEMS.register("qyx", () -> new BH3MusicDisc(() -> SoundRegistry.qyx.get(), baseTick * 175));
	public static RegistryObject<Item> reburn = ITEMS.register("reburn", () -> new BH3MusicDisc(() -> SoundRegistry.reburn.get(), baseTick * 180));
	public static RegistryObject<Item> regression = ITEMS.register("regression", () -> new BH3MusicDisc(() -> SoundRegistry.regression.get(), baseTick * 236));
	public static RegistryObject<Item> reoracle = ITEMS.register("reoracle", () -> new BH3MusicDisc(() -> SoundRegistry.reoracle.get(), baseTick * 200));
	public static RegistryObject<Item> rubia = ITEMS.register("rubia", () -> new BH3MusicDisc(() -> SoundRegistry.rubia.get(), baseTick * 194));
	public static RegistryObject<Item> starfall = ITEMS.register("starfall", () -> new BH3MusicDisc(() -> SoundRegistry.starfall.get(), baseTick * 191));
	public static RegistryObject<Item> true0 = ITEMS.register("true0", () -> new BH3MusicDisc(() -> SoundRegistry.true0.get(), baseTick * 187));
	public static RegistryObject<Item> wlzj = ITEMS.register("wlzj", () -> new BH3MusicDisc(() -> SoundRegistry.wlzj.get(), baseTick * 163));
	public static RegistryObject<Item> xynxszr = ITEMS.register("xynxszr", () -> new BH3MusicDisc(() -> SoundRegistry.xynxszr.get(), baseTick * 201));
	public static RegistryObject<Item> zcfzq = ITEMS.register("zcfzq", () -> new BH3MusicDisc(() -> SoundRegistry.zcfzq.get(), baseTick * 288));

	public static final Item.Properties BLOCK_TAB = new Item.Properties();

	public static RegistryObject<BlockItem> energyOre = ITEMS.register("energy_ore", () -> new BlockItem(BH3Blocks.energyOre.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> energyEndOre = ITEMS.register("energy_end_ore", () -> new BlockItem(BH3Blocks.energyEndOre.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> energyNetherOre = ITEMS.register("energy_nether_ore", () -> new BlockItem(BH3Blocks.energyNetherOre.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> crystalOre = ITEMS.register("crystal_ore", () -> new BlockItem(BH3Blocks.crystalOre.get(), BLOCK_TAB));
}

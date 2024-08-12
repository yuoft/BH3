package com.yuo.bh3.Items;

import com.yuo.bh3.BH3;
import com.yuo.bh3.SoundRegistry;
import net.minecraft.item.Food.Builder;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class BH3Items {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BH3.MODID);

	public static RegistryObject<Item> precisioncard = ITEMS.register("precisioncard", PrecisionCard::new);
	public static RegistryObject<Item> crystal1 = ITEMS.register("crystal1", BH3Item::new);
	public static RegistryObject<Item> crystal5 = ITEMS.register("crystal5", BH3Item::new);
	public static RegistryObject<Item> crystal35 = ITEMS.register("crystal35", BH3Item::new);
	public static RegistryObject<Item> basechip = ITEMS.register("basechip", Chips::new);
	public static RegistryObject<Item> advancedchips = ITEMS.register("advancedchips", Chips::new);
	public static RegistryObject<Item> seniorchips = ITEMS.register("seniorchips", Chips::new);
	public static RegistryObject<Item> premiumchips = ITEMS.register("premiumchips", Chips::new);
	public static RegistryObject<Item> einsteinringmagnet = ITEMS.register("einsteinringmagnet", BH3Item::new);
	public static RegistryObject<Item> supermetalhydrogen = ITEMS.register("supermetalhydrogen", BH3Item::new);
	public static RegistryObject<Item> fluidalloyblock = ITEMS.register("fluidalloyblock", BH3Item::new);
	public static RegistryObject<Item> sfjj = ITEMS.register("sfjj", BH3Item::new);
	public static RegistryObject<Item> xzyjm = ITEMS.register("xzyjm", BH3Item::new);
	public static RegistryObject<Item> watermelon = ITEMS.register("watermelon",
			() -> new BH3Food(new Builder().hunger(3).saturation(9).build()));
	public static RegistryObject<Item> sodapop = ITEMS.register("sodapop",
			() -> new BH3Food(new Builder().hunger(6).saturation(18).build()));
	public static RegistryObject<Item> staminapotion = ITEMS.register("staminapotion",
			() -> new BH3Food(new Builder().hunger(10).saturation(30).build()));

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
	public static RegistryObject<Item> bcy = ITEMS.register("bcy", () -> new BH3MusicDisc(() -> SoundRegistry.bcy.get()));
	public static RegistryObject<Item> befall = ITEMS.register("befall", () -> new BH3MusicDisc(() -> SoundRegistry.befall.get()));
	public static RegistryObject<Item> bhsjdgj = ITEMS.register("bhsjdgj", () -> new BH3MusicDisc(() -> SoundRegistry.bhsjdgj.get()));
	public static RegistryObject<Item> cyberangel = ITEMS.register("cyberangel", () -> new BH3MusicDisc(() -> SoundRegistry.cyberangel.get()));
	public static RegistryObject<Item> dacapo = ITEMS.register("dacapo", () -> new BH3MusicDisc(() -> SoundRegistry.dacapo.get()));
	public static RegistryObject<Item> dualego = ITEMS.register("dualego", () -> new BH3MusicDisc(() -> SoundRegistry.dualego.get()));
	public static RegistryObject<Item> girlinside = ITEMS.register("girlinside", () -> new BH3MusicDisc(() -> SoundRegistry.girlinside.get()));
	public static RegistryObject<Item> iras17514 = ITEMS.register("iras17514", () -> new BH3MusicDisc(() -> SoundRegistry.iras17514.get()));
	public static RegistryObject<Item> lz = ITEMS.register("lz", () -> new BH3MusicDisc(() -> SoundRegistry.lz.get()));
	public static RegistryObject<Item> moonhalo = ITEMS.register("moonhalo", () -> new BH3MusicDisc(() -> SoundRegistry.moonhalo.get()));
	public static RegistryObject<Item> nightglow = ITEMS.register("nightglow", () -> new BH3MusicDisc(() -> SoundRegistry.nightglow.get()));
	public static RegistryObject<Item> noceiling = ITEMS.register("noceiling", () -> new BH3MusicDisc(() -> SoundRegistry.noceiling.get()));
	public static RegistryObject<Item> oaths = ITEMS.register("oaths", () -> new BH3MusicDisc(() -> SoundRegistry.oaths.get()));
	public static RegistryObject<Item> qnzy = ITEMS.register("qnzy", () -> new BH3MusicDisc(() -> SoundRegistry.qnzy.get()));
	public static RegistryObject<Item> qyx = ITEMS.register("qyx", () -> new BH3MusicDisc(() -> SoundRegistry.qyx.get()));
	public static RegistryObject<Item> reburn = ITEMS.register("reburn", () -> new BH3MusicDisc(() -> SoundRegistry.reburn.get()));
	public static RegistryObject<Item> regression = ITEMS.register("regression", () -> new BH3MusicDisc(() -> SoundRegistry.regression.get()));
	public static RegistryObject<Item> reoracle = ITEMS.register("reoracle", () -> new BH3MusicDisc(() -> SoundRegistry.reoracle.get()));
	public static RegistryObject<Item> rubia = ITEMS.register("rubia", () -> new BH3MusicDisc(() -> SoundRegistry.rubia.get()));
	public static RegistryObject<Item> starfall = ITEMS.register("starfall", () -> new BH3MusicDisc(() -> SoundRegistry.starfall.get()));
	public static RegistryObject<Item> true0 = ITEMS.register("true0", () -> new BH3MusicDisc(() -> SoundRegistry.true0.get()));
	public static RegistryObject<Item> wlzj = ITEMS.register("wlzj", () -> new BH3MusicDisc(() -> SoundRegistry.wlzj.get()));
	public static RegistryObject<Item> xynxszr = ITEMS.register("xynxszr", () -> new BH3MusicDisc(() -> SoundRegistry.xynxszr.get()));
	public static RegistryObject<Item> zcfzq = ITEMS.register("zcfzq", () -> new BH3MusicDisc(() -> SoundRegistry.zcfzq.get()));

}

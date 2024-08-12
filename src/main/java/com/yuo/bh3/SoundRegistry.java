package com.yuo.bh3;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BH3.MODID);

    public static RegistryObject<SoundEvent> bcy = SOUNDS.register("bcy",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "bcy")));
    public static RegistryObject<SoundEvent> befall = SOUNDS.register("befall",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "befall")));
    public static RegistryObject<SoundEvent> bhsjdgj = SOUNDS.register("bhsjdgj",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "bhsjdgj")));
    public static RegistryObject<SoundEvent> cyberangel = SOUNDS.register("cyberangel",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "cyberangel")));
    public static RegistryObject<SoundEvent> dacapo = SOUNDS.register("dacapo",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "dacapo")));
    public static RegistryObject<SoundEvent> dualego = SOUNDS.register("dualego",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "dualego")));
    public static RegistryObject<SoundEvent> girlinside = SOUNDS.register("girlinside",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "girlinside")));
    public static RegistryObject<SoundEvent> iras17514 = SOUNDS.register("iras17514",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "iras17514")));
    public static RegistryObject<SoundEvent> lz = SOUNDS.register("lz",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "lz")));
    public static RegistryObject<SoundEvent> moonhalo = SOUNDS.register("moonhalo",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "moonhalo")));
    public static RegistryObject<SoundEvent> nightglow = SOUNDS.register("nightglow",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "nightglow")));
    public static RegistryObject<SoundEvent> noceiling = SOUNDS.register("noceiling",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "noceiling")));
    public static RegistryObject<SoundEvent> oaths = SOUNDS.register("oaths",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "oaths")));
    public static RegistryObject<SoundEvent> qnzy = SOUNDS.register("qnzy",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "qnzy")));
    public static RegistryObject<SoundEvent> qyx = SOUNDS.register("qyx",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "qyx")));
    public static RegistryObject<SoundEvent> reburn = SOUNDS.register("reburn",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "reburn")));
    public static RegistryObject<SoundEvent> regression = SOUNDS.register("regression",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "regression")));
    public static RegistryObject<SoundEvent> reoracle = SOUNDS.register("reoracle",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "reoracle")));
    public static RegistryObject<SoundEvent> rubia = SOUNDS.register("rubia",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "rubia")));
    public static RegistryObject<SoundEvent> starfall = SOUNDS.register("starfall",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "starfall")));
    public static RegistryObject<SoundEvent> true0 = SOUNDS.register("true0",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "true0")));
    public static RegistryObject<SoundEvent> wlzj = SOUNDS.register("wlzj",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "wlzj")));
    public static RegistryObject<SoundEvent> xynxszr = SOUNDS.register("xynxszr",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "xynxszr")));
    public static RegistryObject<SoundEvent> zcfzq = SOUNDS.register("zcfzq",
            () -> new SoundEvent(new ResourceLocation(BH3.MODID, "zcfzq")));

}

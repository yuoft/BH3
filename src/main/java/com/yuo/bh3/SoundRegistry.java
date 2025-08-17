package com.yuo.bh3;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BH3.MOD_ID);

    public static RegistryObject<SoundEvent> bcy = SOUNDS.register("bcy",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "bcy")));
    public static RegistryObject<SoundEvent> befall = SOUNDS.register("befall",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "befall")));
    public static RegistryObject<SoundEvent> bhsjdgj = SOUNDS.register("bhsjdgj",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "bhsjdgj")));
    public static RegistryObject<SoundEvent> cyberangel = SOUNDS.register("cyberangel",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "cyberangel")));
    public static RegistryObject<SoundEvent> dacapo = SOUNDS.register("dacapo",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "dacapo")));
    public static RegistryObject<SoundEvent> dualego = SOUNDS.register("dualego",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "dualego")));
    public static RegistryObject<SoundEvent> girlinside = SOUNDS.register("girlinside",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "girlinside")));
    public static RegistryObject<SoundEvent> iras17514 = SOUNDS.register("iras17514",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "iras17514")));
    public static RegistryObject<SoundEvent> lz = SOUNDS.register("lz",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "lz")));
    public static RegistryObject<SoundEvent> moonhalo = SOUNDS.register("moonhalo",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "moonhalo")));
    public static RegistryObject<SoundEvent> nightglow = SOUNDS.register("nightglow",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "nightglow")));
    public static RegistryObject<SoundEvent> noceiling = SOUNDS.register("noceiling",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "noceiling")));
    public static RegistryObject<SoundEvent> oaths = SOUNDS.register("oaths",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "oaths")));
    public static RegistryObject<SoundEvent> qnzy = SOUNDS.register("qnzy",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "qnzy")));
    public static RegistryObject<SoundEvent> qyx = SOUNDS.register("qyx",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "qyx")));
    public static RegistryObject<SoundEvent> reburn = SOUNDS.register("reburn",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "reburn")));
    public static RegistryObject<SoundEvent> regression = SOUNDS.register("regression",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "regression")));
    public static RegistryObject<SoundEvent> reoracle = SOUNDS.register("reoracle",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "reoracle")));
    public static RegistryObject<SoundEvent> rubia = SOUNDS.register("rubia",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "rubia")));
    public static RegistryObject<SoundEvent> starfall = SOUNDS.register("starfall",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "starfall")));
    public static RegistryObject<SoundEvent> true0 = SOUNDS.register("true0",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "true0")));
    public static RegistryObject<SoundEvent> wlzj = SOUNDS.register("wlzj",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "wlzj")));
    public static RegistryObject<SoundEvent> xynxszr = SOUNDS.register("xynxszr",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "xynxszr")));
    public static RegistryObject<SoundEvent> zcfzq = SOUNDS.register("zcfzq",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BH3.MOD_ID, "zcfzq")));

}

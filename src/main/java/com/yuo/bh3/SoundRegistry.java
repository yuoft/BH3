package com.yuo.bh3;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BH3.MOD_ID);

    public static RegistryObject<SoundEvent> bcy = SOUNDS.register("bcy",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("bcy")));
    public static RegistryObject<SoundEvent> befall = SOUNDS.register("befall",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("befall")));
    public static RegistryObject<SoundEvent> bhsjdgj = SOUNDS.register("bhsjdgj",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("bhsjdgj")));
    public static RegistryObject<SoundEvent> cyberangel = SOUNDS.register("cyberangel",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("cyberangel")));
    public static RegistryObject<SoundEvent> dacapo = SOUNDS.register("dacapo",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("dacapo")));
    public static RegistryObject<SoundEvent> dualego = SOUNDS.register("dualego",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("dualego")));
    public static RegistryObject<SoundEvent> girlinside = SOUNDS.register("girlinside",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("girlinside")));
    public static RegistryObject<SoundEvent> iras17514 = SOUNDS.register("iras17514",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("iras17514")));
    public static RegistryObject<SoundEvent> lz = SOUNDS.register("lz",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("lz")));
    public static RegistryObject<SoundEvent> moonhalo = SOUNDS.register("moonhalo",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("moonhalo")));
    public static RegistryObject<SoundEvent> nightglow = SOUNDS.register("nightglow",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("nightglow")));
    public static RegistryObject<SoundEvent> noceiling = SOUNDS.register("noceiling",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("noceiling")));
    public static RegistryObject<SoundEvent> oaths = SOUNDS.register("oaths",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("oaths")));
    public static RegistryObject<SoundEvent> qnzy = SOUNDS.register("qnzy",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("qnzy")));
    public static RegistryObject<SoundEvent> qyx = SOUNDS.register("qyx",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("qyx")));
    public static RegistryObject<SoundEvent> reburn = SOUNDS.register("reburn",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("reburn")));
    public static RegistryObject<SoundEvent> regression = SOUNDS.register("regression",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("regression")));
    public static RegistryObject<SoundEvent> reoracle = SOUNDS.register("reoracle",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("reoracle")));
    public static RegistryObject<SoundEvent> rubia = SOUNDS.register("rubia",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("rubia")));
    public static RegistryObject<SoundEvent> starfall = SOUNDS.register("starfall",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("starfall")));
    public static RegistryObject<SoundEvent> true0 = SOUNDS.register("true0",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("true0")));
    public static RegistryObject<SoundEvent> wlzj = SOUNDS.register("wlzj",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("wlzj")));
    public static RegistryObject<SoundEvent> xynxszr = SOUNDS.register("xynxszr",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("xynxszr")));
    public static RegistryObject<SoundEvent> zcfzq = SOUNDS.register("zcfzq",
            () -> SoundEvent.createVariableRangeEvent(ModUtils.fa("zcfzq")));

}

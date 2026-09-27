package com.yuo.bh3.Data;

import com.yuo.bh3.BH3;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvent {

    @SubscribeEvent
    public static void addLoot(GatherDataEvent event){
        boolean b = event.includeServer();
        boolean b0 = event.includeClient();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
//        generator.addProvider(b, new ModDataRecipes(output));
//        generator.addProvider(true, new ModLanguageProvider(output, Kaguya.MOD_ID, "en_us"));
        generator.addProvider(b0, new ModItemModelProvider(output, BH3.MOD_ID, fileHelper));
        CompletableFuture<Provider> future = event.getLookupProvider();
//        generator.addProvider(b, new DamageTypeInit(generator.getPackOutput(), future));
    }

//    public static class DamageTypeInit extends DatapackBuiltinEntriesProvider {
//
//        public DamageTypeInit(PackOutput output, CompletableFuture<Provider> future) {
//            super(output, future, DanmakuDamageTypes.DAMAGE_BUILDER, Set.of("minecraft", BH3.MOD_ID));
//        }
//    }
}

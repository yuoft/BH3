package com.yuo.bh3.Data;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Wrold.ModWorldGen;
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
        CompletableFuture<Provider> future = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(b, new ModWorldGen(output, future));
        generator.addProvider(b0, new ModItemModelProvider(output, BH3.MOD_ID, fileHelper));
    }

}

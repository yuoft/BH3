package com.yuo.bh3.Items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;

import java.util.function.Supplier;

public class BH3MusicDisc extends RecordItem {
    public BH3MusicDisc(Supplier<SoundEvent> sound, int tick) {
        super(2, sound, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON), tick);
    }
}

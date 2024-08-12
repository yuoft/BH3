package com.yuo.bh3.Items;

import com.yuo.bh3.tab.BH3Tabs;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class BH3MusicDisc extends MusicDiscItem {
    public BH3MusicDisc(Supplier<SoundEvent> sound) {
        super(2, sound, new Properties().group(BH3Tabs.BH3_ITEM).rarity(Rarity.UNCOMMON));
    }
}

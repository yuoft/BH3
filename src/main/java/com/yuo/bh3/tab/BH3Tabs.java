package com.yuo.bh3.tab;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Items.BH3Weapon;
import com.yuo.bh3.Items.BH3WeaponBow;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

//创造模式物品栏
public class BH3Tabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BH3.MOD_ID);
	public static final RegistryObject<CreativeModeTab> WEAPON_TAB = TABS.register(BH3.MOD_ID + "_tab0", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.tab.bh3_weapon"))
			.icon(() -> BH3Items.crystal1.get().getDefaultInstance())
			.displayItems((parameters, output) -> {
				for (RegistryObject<Item> entry : BH3Items.ITEMS.getEntries()) {
					if (entry.get() instanceof BH3Weapon || entry.get() instanceof BH3WeaponBow) {
						output.accept(new ItemStack(entry.get()));
					}
				}
			}).build());
	public static final RegistryObject<CreativeModeTab> ITEM_TAB = TABS.register(BH3.MOD_ID + "_tab1", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.tab.bh3_item"))
			.icon(() -> BH3Items.waterMelon.get().getDefaultInstance())
			.displayItems((parameters, output) -> {
				for (RegistryObject<Item> entry : BH3Items.ITEMS.getEntries()) {
					if (!(entry.get() instanceof BH3Weapon || entry.get() instanceof BH3WeaponBow)) {
						output.accept(new ItemStack(entry.get()));
					}
				}
			}).build());
}

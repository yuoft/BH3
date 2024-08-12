package com.yuo.bh3.tab;

import com.yuo.bh3.Items.BH3Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//创造模式物品栏
public class BH3Tabs {

	public static BH3GroupWeapon BH3_WEAPON = new BH3GroupWeapon();
	public static BH3GroupItem BH3_ITEM = new BH3GroupItem();

	public static class BH3GroupWeapon extends ItemGroup {
		public BH3GroupWeapon() {
			super(ItemGroup.GROUPS.length, "bh3_weapon"); //页码11开始，名称
		}

		//图标
		@Override
		public ItemStack createIcon() {
			return new ItemStack(BH3Items.CBZ.get());
		}
	}
	public static class BH3GroupItem extends ItemGroup {
		public BH3GroupItem() {
			super(ItemGroup.GROUPS.length, "bh3_item");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(BH3Items.bcy.get());
		}
	}
}

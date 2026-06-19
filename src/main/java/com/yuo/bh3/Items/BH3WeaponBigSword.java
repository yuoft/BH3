package com.yuo.bh3.Items;

import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

/**
 * 大剑
 */
public class BH3WeaponBigSword extends BH3Weapon implements WeaponCategory {

	public BH3WeaponBigSword() {
		super(BH3ItemTier.BH_32, 2, -3.0f);
	}


	@Override
	public int universalOrdinal() {
		return WeaponCategories.GREATSWORD.universalOrdinal();
	}
}

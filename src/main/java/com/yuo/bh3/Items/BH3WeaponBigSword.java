package com.yuo.bh3.Items;

/**
 * 大剑
 */
public class BH3WeaponBigSword extends BH3Weapon {

	public BH3WeaponBigSword(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed());
	}
}

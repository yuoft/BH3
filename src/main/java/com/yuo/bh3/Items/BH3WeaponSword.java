package com.yuo.bh3.Items;

/**
 * 太刀
 */
public class BH3WeaponSword extends BH3Weapon {
	public BH3WeaponSword(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed());
	}
}

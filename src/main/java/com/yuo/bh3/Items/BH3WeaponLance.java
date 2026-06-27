package com.yuo.bh3.Items;

/**
 * 枪
 */
public class BH3WeaponLance extends BH3Weapon {

	public BH3WeaponLance(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed());
	}

}

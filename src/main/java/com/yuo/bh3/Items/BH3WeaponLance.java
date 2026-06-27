package com.yuo.bh3.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 枪
 */
public class BH3WeaponLance extends BH3BaseWeapon {

	public BH3WeaponLance(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed());
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_lance"));
		super.appendHoverText(stack, world, components, flag);
	}
}

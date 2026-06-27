package com.yuo.bh3.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 镰刀
 */
public class BH3WeaponSickle extends BH3BaseWeapon {

	public BH3WeaponSickle(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed());
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_sickle"));
		super.appendHoverText(stack, world, components, flag);
	}
}

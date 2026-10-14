package com.yuo.bh3.Items;

import com.yuo.bh3.Items.skill.SpearSkill;
import com.yuo.bh3.capability.HonkaiEnergyProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 长枪
 */
public class BH3WeaponSpear extends BH3BaseWeapon {

	public BH3WeaponSpear(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed(), wa);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_spear"));
		super.appendHoverText(stack, world, components, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		// 服务端：检查崩坏能
		if (!BH3BaseWeapon.isSkillEnergyCost(level, player, SpearSkill.HONKAI_COST)) {
			return InteractionResultHolder.fail(stack);
		}

		if (!level.isClientSide && player instanceof ServerPlayer sp) {
			SpearSkill.startServer(sp);
			HonkaiEnergyProvider.get(player).ifPresent(h -> h.consume(SpearSkill.HONKAI_COST));
		}

		player.swing(hand, true);
		return InteractionResultHolder.success(stack);
	}
}

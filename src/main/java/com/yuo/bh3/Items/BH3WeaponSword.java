package com.yuo.bh3.Items;

import com.yuo.bh3.Items.skill.SwordSkill;
import com.yuo.bh3.capability.HonkaiEnergyProvider;
import net.minecraft.client.player.LocalPlayer;
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
 * 太刀
 */
public class BH3WeaponSword extends BH3BaseWeapon {
	public BH3WeaponSword(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed(), wa);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_sword"));
		super.appendHoverText(stack, world, components, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!isSkillEnergyCost(level, player, SwordSkill.HONKAI_COST)) return InteractionResultHolder.fail(stack);

		if (level.isClientSide) {
			if (player instanceof LocalPlayer lp) {
				SwordSkill.startClient(lp);
			}
		} else {
			if (player instanceof ServerPlayer sp) {
				SwordSkill.startServer(sp);
				HonkaiEnergyProvider.get(player).ifPresent(h -> h.consume(SwordSkill.HONKAI_COST));
			}
		}

		player.swing(hand, true);   // 挥一下手臂
		return InteractionResultHolder.success(stack);
	}
}

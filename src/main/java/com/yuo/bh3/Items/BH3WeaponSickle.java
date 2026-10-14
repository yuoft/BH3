package com.yuo.bh3.Items;

import com.yuo.bh3.Items.skill.SickleSkill;
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

public class BH3WeaponSickle extends BH3BaseWeapon {

	public BH3WeaponSickle(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed(), wa);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_sickle"));
		super.appendHoverText(stack, world, components, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!BH3BaseWeapon.isSkillEnergyCost(level, player, SickleSkill.HONKAI_COST)) return InteractionResultHolder.fail(stack);

		if (player instanceof ServerPlayer sp) {
			SickleSkill.spawn(sp, stack);
			HonkaiEnergyProvider.get(player).ifPresent(h -> h.consume(SickleSkill.HONKAI_COST));
			stack.hurtAndBreak(13, player, p -> p.broadcastBreakEvent(hand));
		}

		player.swing(hand, true);
		return InteractionResultHolder.success(stack);
	}
}
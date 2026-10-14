package com.yuo.bh3.Items;

import com.yuo.bh3.Items.skill.LanceSkill;
import com.yuo.bh3.Items.skill.SickleSkill;
import com.yuo.bh3.capability.HonkaiEnergyProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 骑枪
 */
public class BH3WeaponLance extends BH3BaseWeapon {

	public BH3WeaponLance(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed(), wa);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_lance"));
		super.appendHoverText(stack, world, components, flag);
	}

	@Override
	public @NotNull UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!BH3BaseWeapon.isSkillEnergyCost(level, player, LanceSkill.HONKAI_COST)) {
			return InteractionResultHolder.fail(stack);
		}

		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (!(entity instanceof Player player)) return;

		int chargeTicks = getUseDuration(stack) - timeLeft;
		if (chargeTicks < LanceSkill.CHARGE_MIN) return;

		if (player instanceof ServerPlayer sp) {
			LanceSkill.spawn(sp, stack.copy());
			HonkaiEnergyProvider.get(player).ifPresent(h -> h.consume(LanceSkill.HONKAI_COST));
			stack.hurtAndBreak(15, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
		}

		player.swing(player.getUsedItemHand(), true);
	}
}
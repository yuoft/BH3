package com.yuo.bh3.Items;

import com.yuo.bh3.Items.skill.BigSwordSkill;
import com.yuo.bh3.capability.HonkaiEnergyProvider;
import com.yuo.bh3.capability.IHonkaiEnergy;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * 大剑
 */
public class BH3WeaponBigSword extends BH3BaseWeapon {

	public BH3WeaponBigSword(WeaponAttributes wa) {
		super(wa.getTier(), wa.getDamage(), wa.getDamageSpeed(), wa);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(Component.translatable("bh3.tips.weapon_type_big_sword"));
		super.appendHoverText(stack, world, components, flag);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;   // 三叉戟蓄力动画
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;   // 无限，靠松手释放
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!isSkillEnergyCost(level, player, BigSwordSkill.HONKAI_COST)) return InteractionResultHolder.fail(stack);

		// 服务端生成实体
		if (player instanceof ServerPlayer sp) {
			BigSwordSkill.spawn(sp, stack);
		}
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (entity instanceof Player player) {
			Optional<IHonkaiEnergy> honkaiEnergy = HonkaiEnergyProvider.get(player).resolve();
			int chargeTicks = getUseDuration(stack) - timeLeft;
			if (chargeTicks >= BigSwordSkill.CHARGE_MIN_TICKS) { //完成技能释放，消耗崩坏能
				honkaiEnergy.ifPresent(iHonkaiEnergy -> iHonkaiEnergy.consume(BigSwordSkill.HONKAI_COST));
				stack.hurtAndBreak(17, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
			}
		}
	}
}

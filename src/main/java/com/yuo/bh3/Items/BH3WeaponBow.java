package com.yuo.bh3.Items;

import com.yuo.bh3.Entity.BH3EntityTypes;
import com.yuo.bh3.Entity.PlacedWeaponEntity;
import com.yuo.bh3.Entity.WeaponArrowEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.function.Predicate;

/**
 * 弓
 */
public class BH3WeaponBow extends BowItem {

	public BH3WeaponBow() {
		super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.create("bh3:weapon", ChatFormatting.GOLD)));
	}

	// BH3Weapon.java
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();

		if (!level.isClientSide() && player != null) {
			// 获取玩家视角方向，决定武器的朝向
			float yaw = player.getYRot(); // 水平角度
			float pitch = 0; // 默认垂直

			// 如果玩家蹲下+右键，可以改变倾斜角度
			if (player.isShiftKeyDown()) {
				pitch = 90; // 水平放置（类似插在墙上）
			}

			// 计算放置位置（点击的方块表面）
			BlockPos placePos = pos.relative(context.getClickedFace());
			ItemStack stack = player.getItemInHand(hand);

			// 创建并放置实体
			PlacedWeaponEntity weaponEntity = BH3EntityTypes.PLACED_WEAPON.get().create(level);
			if (weaponEntity != null) {
				weaponEntity.setWeapon(stack.copy(), placePos, yaw, pitch);
				level.addFreshEntity(weaponEntity);

				// 如果不是创造模式，消耗一个物品
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}


	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player player) {
			boolean flag = player.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
			ItemStack itemstack = findAmmo(stack, player);

			int i = this.getUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPowerForTime(i);
				if (!((double)f < 0.1D)) {
					boolean flag1 = player.isCreative() || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player));
					if (!worldIn.isClientSide) {
						AbstractArrow abstractarrowentity = new WeaponArrowEntity(BH3EntityTypes.WEAPON_ARROW.get(), player.getX(), player.getEyeY(), player.getZ() ,worldIn);
						abstractarrowentity = customArrow(abstractarrowentity);
						abstractarrowentity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
						abstractarrowentity.setCritArrow(true);

						int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
						if (j > 0) {
							abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + (double)j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
						if (k > 0) {
							abstractarrowentity.setKnockback(k);
						}

						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
							abstractarrowentity.setRemainingFireTicks(100);
						}

						stack.hurtAndBreak(1, player, (player1) -> {
							player1.broadcastBreakEvent(player.getUsedItemHand());
						});
						if (flag1 || player.isCreative() && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							abstractarrowentity.pickup = Pickup.CREATIVE_ONLY;
						}

						worldIn.addFreshEntity(abstractarrowentity);
					}

					worldIn.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !player.isCreative()) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							player.getInventory().removeItem(itemstack);
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

	//寻找弹药
	private ItemStack findAmmo(ItemStack shootable, Player player) {
		if (!(shootable.getItem() instanceof ProjectileWeaponItem)) {
			return ItemStack.EMPTY;
		} else {
			Predicate<ItemStack> predicate = ((ProjectileWeaponItem)shootable.getItem()).getSupportedHeldProjectiles();
			ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
			if (!itemstack.isEmpty()) {
				return itemstack;
			} else {
				predicate = ((ProjectileWeaponItem)shootable.getItem()).getAllSupportedProjectiles();

				for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
					ItemStack itemstack1 = player.getInventory().getItem(i);
					if (predicate.test(itemstack1)) {
						return ForgeHooks.getProjectile(player, shootable, itemstack1);
					}
				}

				return player.isCreative() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
			}
		}
	}
}

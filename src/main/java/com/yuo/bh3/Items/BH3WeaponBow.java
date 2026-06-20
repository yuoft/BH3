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
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Predicate;

/**
 * 弓
 */
public class BH3WeaponBow extends BowItem {

	public BH3WeaponBow() {
		super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.create("bh3:weapon", ChatFormatting.GOLD)));
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, Player player) {
		Level level = player.level();
		if (!level.isClientSide()) {
			double x = player.getX() + player.getLookAngle().x * 1.5;
			double y = player.getY() + player.getEyeHeight() - 0.3;
			double z = player.getZ() + player.getLookAngle().z * 1.5;

			float yaw = player.getYRot();
			float pitch = 0;

			PlacedWeaponEntity weaponEntity = BH3EntityTypes.PLACED_WEAPON.get().create(level);
			if (weaponEntity != null) {
				weaponEntity.setWeapon(stack.copy(), new BlockPos((int)x, (int)y, (int)z), yaw, pitch);
				weaponEntity.setPos(x, y, z);
				level.addFreshEntity(weaponEntity);

				stack.shrink(1);
				level.playSound(null, weaponEntity.getX(), weaponEntity.getY(), weaponEntity.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 1.0F);
				return false;
			}
		}
		return true;
	}


	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player player) {
			boolean flag = player.isCreative() || stack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;
			ItemStack itemstack = findAmmo(stack, player);

			int i = this.getUseDuration(stack) - timeLeft;
			i = ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPowerForTime(i);
				if (!((double)f < 0.1D)) {
					boolean flag1 = player.isCreative() || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player));
					if (!worldIn.isClientSide) {
						AbstractArrow arrow = new WeaponArrowEntity(BH3EntityTypes.WEAPON_ARROW.get(), player.getX(), player.getEyeY(), player.getZ() ,worldIn);
						arrow = customArrow(arrow);
						arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
						arrow.setCritArrow(true);
						arrow.setNoPhysics(true);

						int j = stack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
						if (j > 0) {
							arrow.setBaseDamage(arrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
						}

						int k = stack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
						if (k > 0) {
							arrow.setKnockback(k);
						}

						if (stack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
							arrow.setRemainingFireTicks(100);
						}

						stack.hurtAndBreak(1, player, (player1) -> {
							player1.broadcastBreakEvent(player.getUsedItemHand());
						});
						if (flag1 || player.isCreative() && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							arrow.pickup = Pickup.CREATIVE_ONLY;
						}

						worldIn.addFreshEntity(arrow);
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

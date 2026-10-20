package com.yuo.bh3.Items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class StaMinaPotion extends Item {
    public StaMinaPotion() {
        super(new Properties().food(BH3Food.staMinaPotion));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (!level.isClientSide) {
            for (MobEffectInstance effect : PotionUtils.getMobEffects(stack)) {
                if (effect.getEffect().isInstantenous()) {
                    effect.getEffect().applyInstantenousEffect(null, null, living, effect.getAmplifier(), 1.0D);
                } else {
                    living.addEffect(new MobEffectInstance(effect));
                }
            }
        }
        if (living instanceof Player player) {
            player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        }
        return super.finishUsingItem(stack, level, living);
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 14;
    }

    public static class SodaPopBrewingRecipe implements IBrewingRecipe {

        @Override
        public boolean isInput(ItemStack input) {
            // ★ 输入必须是普通药水（不接收空瓶、水）
            return input.getItem() == Items.POTION
                    && PotionUtils.getPotion(input) != Potions.EMPTY
                    && PotionUtils.getPotion(input) != Potions.WATER;
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return ingredient.getItem() == BH3Items.waterMelon.get();
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (!isInput(input) || !isIngredient(ingredient)) return ItemStack.EMPTY;

            ItemStack output = new ItemStack(BH3Items.staminaPotion.get(), 1);
            PotionUtils.setPotion(output, PotionUtils.getPotion(input));
            // 复制自定义效果（比如通过命令加的特殊效果）
            PotionUtils.setCustomEffects(output, PotionUtils.getCustomEffects(input));
            return output;
        }
    }
}

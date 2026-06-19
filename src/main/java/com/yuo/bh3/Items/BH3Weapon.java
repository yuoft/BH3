package com.yuo.bh3.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 使用obj模组的崩坏三武器
 * @author yuo
 */
public class BH3Weapon extends SwordItem {

    public BH3Weapon(Tier tier, int damage, float speed) {
        super(tier, damage, speed, new Properties().stacksTo(1).fireResistant().rarity(Rarity.create("bh3:weapon", ChatFormatting.GOLD)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
        String registryName = stack.getItem().getDescriptionId();
        String[] split = registryName.split("\\.");
        if (split.length == 3 && split[2] != null) {
            components.add(Component.translatable("bh3.tips." + split[2]));
        }
    }
}

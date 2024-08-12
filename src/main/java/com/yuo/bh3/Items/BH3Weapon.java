package com.yuo.bh3.Items;

import com.yuo.bh3.tab.BH3Tabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 使用obj模组的崩坏三武器
 * @author yuo
 */
public class BH3Weapon extends SwordItem {

    public BH3Weapon(IItemTier tier, int damage, float speed) {
        super(tier, damage, speed, new Properties().group(BH3Tabs.BH3_WEAPON).maxStackSize(1).isImmuneToFire());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName != null) {
            String path = registryName.getPath();
            components.add(new TranslationTextComponent("bh3.tips." + path));
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent(getTranslationKey()).mergeStyle(TextFormatting.GOLD);
    }
}

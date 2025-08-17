package com.yuo.bh3.Items;

import com.yuo.bh3.Client.BH3ItemRender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

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

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new BH3ItemRender();
            }
        });
    }
}

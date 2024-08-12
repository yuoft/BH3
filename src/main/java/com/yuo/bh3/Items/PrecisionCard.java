package com.yuo.bh3.Items;

import com.yuo.bh3.tab.BH3Tabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class PrecisionCard extends Item {
    public PrecisionCard() {
        super(new Properties().group(BH3Tabs.BH3_ITEM).rarity(Rarity.EPIC));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        super.addInformation(stack, world, components, flag);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            if (world.rand.nextDouble() < 0.648){ //64.8%出货
                Item item = Registry.ITEM.getRandom(world.rand);
                Direction facing = player.getHorizontalFacing();
                BlockPos position = player.getPosition();
                BlockPos pos = position.up().offset(facing);
                ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
                world.addEntity(entity);
            }else {
                player.sendMessage(new TranslationTextComponent("bh3.message.null_item"), UUID.randomUUID());
            }
            stack.shrink(1);
        }
        return super.onItemRightClick(world, player, hand);
    }
}

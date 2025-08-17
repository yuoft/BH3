package com.yuo.bh3.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PrecisionCard extends Item {
    public PrecisionCard() {
        super(new Properties().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, world, components, flag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (level.random.nextDouble() < 0.648){ //64.8%出货
                Item item = BuiltInRegistries.ITEM.getRandom(level.random).get().get();
                Direction facing = player.getMotionDirection();
                BlockPos position = player.getOnPos();
                BlockPos pos = position.above().relative(facing);
                ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
                level.addFreshEntity(entity);
            }else {
                player.displayClientMessage(Component.translatable("bh3.message.null_item"), true);
            }
            stack.shrink(1);
            player.getCooldowns().addCooldown(stack.getItem(), 5);
        }
        return super.use(level, player, hand);
    }
}

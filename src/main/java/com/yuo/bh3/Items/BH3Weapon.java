package com.yuo.bh3.Items;

import com.yuo.bh3.Entity.BH3EntityTypes;
import com.yuo.bh3.Entity.PlacedWeaponEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
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

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            // 获取玩家视线方向，计算丢出位置（约 1.5 格前方，0.5 格高度）
            double x = player.getX() + player.getLookAngle().x * 1.5;
            double y = player.getY() + player.getEyeHeight() - 0.3;
            double z = player.getZ() + player.getLookAngle().z * 1.5;

            // 获取玩家朝向
            float yaw = player.getYRot();
            float pitch = 0; // 默认垂直插地

            PlacedWeaponEntity weaponEntity = BH3EntityTypes.PLACED_WEAPON.get().create(level);
            if (weaponEntity != null) {
                weaponEntity.setWeapon(stack.copy(), new BlockPos((int)x, (int)y, (int)z), yaw, pitch);
                weaponEntity.setPos(x, y, z);
                level.addFreshEntity(weaponEntity);

                stack.shrink(1);
                level.playSound(null, weaponEntity.getX(), weaponEntity.getY(), weaponEntity.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                // 返回 false 取消原掉落物生成
                return false;
            }
        }
        return true;
    }
}

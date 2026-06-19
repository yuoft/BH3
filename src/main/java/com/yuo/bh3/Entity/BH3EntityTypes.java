package com.yuo.bh3.Entity;

import com.yuo.bh3.BH3;
import com.yuo.bh3.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BH3EntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BH3.MOD_ID);
    // 地面物品实体
    public static final RegistryObject<EntityType<PlacedWeaponEntity>> PLACED_WEAPON =
            ENTITY_TYPES.register("placed_weapon",
                    () -> EntityType.Builder.of(PlacedWeaponEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F) // 碰撞箱大小
                            .clientTrackingRange(80) // 客户端同步距离
                            .updateInterval(1) // 更新间隔
                            .build(ModUtils.fa("placed_weapon").toString())
            );

    //箭
    public static final RegistryObject<EntityType<WeaponArrowEntity>> WEAPON_ARROW = ENTITY_TYPES.register("weapon_arrow",
            () -> EntityType.Builder.<WeaponArrowEntity>of(WeaponArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5F).build("weapon_arrow"));


}

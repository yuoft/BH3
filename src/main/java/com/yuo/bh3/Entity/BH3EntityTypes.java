package com.yuo.bh3.Entity;

import com.yuo.bh3.BH3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BH3EntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BH3.MOD_ID);

    //箭
    public static final RegistryObject<EntityType<WeaponArrowEntity>> WEAPON_ARROW = ENTITY_TYPES.register("weapon_arrow",
            () -> EntityType.Builder.<WeaponArrowEntity>of(WeaponArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5F).build("weapon_arrow"));
}

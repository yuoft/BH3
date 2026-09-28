package com.yuo.bh3.Items;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

@SuppressWarnings("removal")
public enum BH3ItemTiers implements Tier {
    BH_30(5, 3121, 20.0F, 15.0F, 30, () -> Ingredient.of(Items.NETHERITE_INGOT)), //镰刀
    BH_300(6, 3611, 25.0F, 18.0F, 35, () -> Ingredient.of(Items.NETHERITE_INGOT)), //镰刀超限
    BH_31(5, 4624, 20.0F, 15.0F, 30, () -> Ingredient.of(Items.NETHERITE_INGOT)), //太刀
    BH_310(6, 5014, 25.0F, 18.0F, 35, () -> Ingredient.of(Items.NETHERITE_INGOT)), //太刀超限
    BH_32(5, 3464, 20.0F, 15.0F, 30, () -> Ingredient.of(Items.NETHERITE_INGOT)), //大剑
    BH_320(6, 3914, 25.0F, 18.0F, 35, () -> Ingredient.of(Items.NETHERITE_INGOT)), //大剑超限
    BH_33(5, 2976, 20.0F, 15.0F, 30, () -> Ingredient.of(Items.NETHERITE_INGOT)), //骑枪
    BH_330(6, 3456, 25.0F, 18.0F, 35, () -> Ingredient.of(Items.NETHERITE_INGOT)), //骑枪超限
    BH_34(5, 3701, 20.0F, 15.0F, 30, () -> Ingredient.of(Items.NETHERITE_INGOT)), //弓
    BH_340(6, 4121, 25.0F, 18.0F, 35, () -> Ingredient.of(Items.NETHERITE_INGOT)); //弓超限

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    BH3ItemTiers(int lv, int use, float eff, float damage, int enchant, Supplier<Ingredient> supplier) {
        this.harvestLevel = lv;
        this.maxUses = use;
        this.efficiency = eff;
        this.attackDamage = damage;
        this.enchantability = enchant;
        this.repairMaterial = new LazyLoadedValue<>(supplier);
    }

    public int getUses() {
        return this.maxUses;
    }

    public float getSpeed() {
        return this.efficiency;
    }

    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    public int getLevel() {
        return this.harvestLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}

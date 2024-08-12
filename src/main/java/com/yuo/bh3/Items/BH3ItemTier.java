package com.yuo.bh3.Items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum BH3ItemTier implements IItemTier {
    BH_30(5, 3121, 20.0F, 15.0F, 30, () -> Ingredient.fromItems(Items.NETHERITE_INGOT)), //镰刀
    BH_31(5, 4624, 20.0F, 15.0F, 30, () -> Ingredient.fromItems(Items.NETHERITE_INGOT)), //太刀
    BH_32(5, 3464, 20.0F, 15.0F, 30, () -> Ingredient.fromItems(Items.NETHERITE_INGOT)), //大剑
    BH_33(5, 2976, 20.0F, 15.0F, 30, () -> Ingredient.fromItems(Items.NETHERITE_INGOT)), //骑枪
    BH_34(5, 3701, 20.0F, 15.0F, 30, () -> Ingredient.fromItems(Items.NETHERITE_INGOT)); //弓

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    private BH3ItemTier(int lv, int use, float eff, float damage, int enchant, Supplier supplier) {
        this.harvestLevel = lv;
        this.maxUses = use;
        this.efficiency = eff;
        this.attackDamage = damage;
        this.enchantability = enchant;
        this.repairMaterial = new LazyValue(supplier);
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
}

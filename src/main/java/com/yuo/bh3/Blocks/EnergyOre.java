package com.yuo.bh3.Blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.material.MapColor;

public class EnergyOre extends DropExperienceBlock {
    public EnergyOre(float hardness, float resistance, UniformInt xpRange) {
        super(Properties.of().mapColor(MapColor.STONE).strength(hardness, resistance).requiresCorrectToolForDrops().lightLevel(e -> 5), xpRange);
    }
}

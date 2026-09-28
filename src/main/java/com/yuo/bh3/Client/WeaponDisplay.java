package com.yuo.bh3.Client;

import net.minecraft.world.item.ItemDisplayContext;

public final class WeaponDisplay {

    private final ItemTransform thirdRight, thirdLeft;
    private final ItemTransform firstRight, firstLeft;
    private final ItemTransform gui, fixed;

    public WeaponDisplay(ItemTransform thirdRight, ItemTransform thirdLeft,
                         ItemTransform firstRight, ItemTransform firstLeft,
                         ItemTransform gui, ItemTransform fixed) {
        this.thirdRight = thirdRight;
        this.thirdLeft  = thirdLeft;
        this.firstRight = firstRight;
        this.firstLeft  = firstLeft;
        this.gui        = gui;
        this.fixed      = fixed;
    }

    public ItemTransform get(ItemDisplayContext ctx) {
        return switch (ctx) {
            case THIRD_PERSON_RIGHT_HAND -> thirdRight;
            case THIRD_PERSON_LEFT_HAND  -> thirdLeft;
            case FIRST_PERSON_RIGHT_HAND -> firstRight;
            case FIRST_PERSON_LEFT_HAND  -> firstLeft;
            case GUI                     -> gui;
            case FIXED                   -> fixed;
            default                      -> ItemTransform.IDENTITY;
        };
    }
}
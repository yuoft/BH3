package com.yuo.bh3;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("removal")
public class ModUtils {
    public static ResourceLocation fa(String path) {
        return new ResourceLocation(BH3.MOD_ID, path);
    }

    public static ResourceLocation fa(String id, String path) {
        return new ResourceLocation(id, path);
    }

    public static ResourceLocation def(String path) {
        return new ResourceLocation(path);
    }

    public static ResourceLocation tayParse(String path) {
        try {
            return new ResourceLocation(path);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}

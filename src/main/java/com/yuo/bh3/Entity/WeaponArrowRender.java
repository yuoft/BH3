package com.yuo.bh3.Entity;

import com.yuo.bh3.ModUtils;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WeaponArrowRender extends ArrowRenderer<WeaponArrowEntity> {
    private static final ResourceLocation TEXTURE = ModUtils.fa("textures/entity/weapon_arrow.png");

    public WeaponArrowRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(WeaponArrowEntity entity) {
        return TEXTURE;
    }
}
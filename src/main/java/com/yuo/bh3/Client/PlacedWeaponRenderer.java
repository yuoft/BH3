package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.bh3.Entity.PlacedWeaponEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PlacedWeaponRenderer extends EntityRenderer<PlacedWeaponEntity> {
    private final ItemRenderer itemRenderer;

    public PlacedWeaponRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(PlacedWeaponEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = entity.getWeaponStack();
        if (stack.isEmpty()) return;

        poseStack.pushPose();

        // 1. 姿势变换（先添加 → 后作用于顶点）
        poseStack.mulPose(Axis.ZP.rotationDegrees(-135));
        poseStack.mulPose(Axis.XP.rotationDegrees(0));
        poseStack.translate(-0.75, -0.75, -0.25);

// 2. 自旋：最后添加 → 最先作用于顶点 → 绕模型中心
        float ticksAlive = entity.tickCount + partialTick;
        if (ticksAlive < 20) {
            float progress = ticksAlive / 20f;
            float eased = 1f - (float) Math.pow(1f - progress, 3);
            float angle = eased * 720;
            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
        }

// 3. 渲染
        BakedModel model = itemRenderer.getModel(stack, entity.level(), null, 0);
        itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, buffer,
                packedLight, OverlayTexture.NO_OVERLAY, model);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PlacedWeaponEntity entity) {
        return null;
    }
}
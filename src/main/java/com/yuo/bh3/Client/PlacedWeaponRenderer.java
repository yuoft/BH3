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

        // 1. 基础位置（由实体位置决定）
        // 2. 旋转：让武器竖直（剑尖朝下）
        poseStack.mulPose(Axis.ZP.rotationDegrees(-135));
        poseStack.mulPose(Axis.XP.rotationDegrees(0));
//        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));

        // 3. 微调位置使剑尖着地（根据模型偏移）
        poseStack.translate(-0.75, -0.75, -0.25); // 向下偏移，数值需根据模型实际高度调整

        // 4. 如果需要缩放，可在此时应用

        // 5. 获取模型并渲染（直接从ItemStack获取，自动使用OBJ模型）
        BakedModel model = itemRenderer.getModel(stack, entity.level(), null, 0);

        // 使用ItemRenderer渲染，它会自动应用模型的变换
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
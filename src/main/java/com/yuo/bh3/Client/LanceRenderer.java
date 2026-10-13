package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.bh3.Entity.LanceEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LanceRenderer extends EntityRenderer<LanceEntity> {

    public LanceRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(LanceEntity entity, float entityYaw, float pt, PoseStack pose, MultiBufferSource buffer, int light) {

        pose.pushPose();

        pose.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(entity.getXRot() + 90f));
        pose.mulPose(Axis.ZP.rotationDegrees(45f));
        float scale = 2.5f;
        pose.scale(scale, scale, scale);
        pose.translate(-1.3f, -1.3f, -0.8f);

        WeaponTESR.renderModel(pose, buffer, light, entity.getWeapon());

        pose.popPose();

        super.render(entity, entityYaw, pt, pose, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(LanceEntity entity) {
        return null;
    }
}
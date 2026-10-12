package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.bh3.Entity.SickleEntity;
import com.yuo.bh3.Items.skill.SickleSkill;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.yuo.bh3.Client.WeaponTESR.getItemID;

public class SickleRenderer extends EntityRenderer<SickleEntity> {

    private static final float MODEL_SCALE = 3.0f;
    private static final float BASE_ROTATION = 40f;

    public SickleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(SickleEntity entity, float entityYaw, float pt, PoseStack pose, MultiBufferSource buffer, int light) {

        int tick = entity.tickCount;
        int pullEnd = SickleSkill.PULL_DURATION;
        int slashEnd = pullEnd + SickleSkill.SLASH_DURATION;

        // 只在横扫阶段渲染武器
        if (tick < pullEnd || tick >= slashEnd) {
//            super.render(entity, entityYaw, pt, pose, buffer, light);
//            return;
        }

        // 横扫进度
        float progress = (tick - pullEnd + pt) / SickleSkill.SLASH_DURATION;
        progress = Math.max(0f, Math.min(1f, progress));

        float scanYaw = entityYaw + SickleSkill.SCAN_START_OFFSET + progress * SickleSkill.SCAN_TOTAL_ANGLE;

        pose.pushPose();
        try {
            // ① 抬到横扫高度
            pose.translate(0, 0.6f, 0);

            pose.mulPose(Axis.YP.rotationDegrees(-scanYaw));

            // ④ 武器放平 + 摆正（绕弧线位置的原点旋转）
            pose.mulPose(Axis.XP.rotationDegrees(90f));
            pose.mulPose(Axis.ZP.rotationDegrees(BASE_ROTATION - 90f));

            pose.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);

            // ★ 把"柄"移到弧线上，而不是包围盒中心
            pose.translate(.5f, .5f, 0);

            // 渲染
            WeaponTESR.renderModel(pose, buffer, light, entity.getWeapon());
        } finally {
            pose.popPose();
        }

//        super.render(entity, entityYaw, pt, pose, buffer, light);
    }


    @Override
    public ResourceLocation getTextureLocation(SickleEntity entity) {
        return null;
    }
}
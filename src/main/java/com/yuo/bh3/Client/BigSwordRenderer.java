package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.bh3.Entity.BigSwordEntity;
import com.yuo.bh3.Items.skill.BigSwordSkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BigSwordRenderer extends EntityRenderer<BigSwordEntity> {

    private static final float MODEL_SCALE = 3.5f;
    private static final double MODEL_HEIGHT = MODEL_SCALE * 1.65f;
    private static final float CENTER_X = 0.85f;
    private static final float CENTER_Y = 0.80f;
    private static final float CENTER_Z = 0.765f;
    private static final float BASE_ROTATION = 40f;

    public BigSwordRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    /**
     * 三层叠加的金色圣剑光柱
     */
    private static void renderDivineBeam(PoseStack pose, MultiBufferSource buffer, float progress, float pt, long gameTime) {
        // ★ 能量脉动：光柱半径周期性呼吸
        float pulse = 1.0f + 0.12f * (float) Math.sin(gameTime * 0.35);

        // ---------- 第一层：外圈金光（最粗，滚动最慢）----------
        float goldRadius = (0.35f + progress * 0.5f) * pulse;
        float goldGlow = goldRadius * 1.4f;
        BeaconRenderer.renderBeaconBeam(pose, buffer, BeaconRenderer.BEAM_LOCATION, pt, 0.5f, gameTime,     // textureScale 0.5 → 纹理慢滚
                0, 384, new float[]{1.0f, 0.72f, 0.20f},   // 金色
                goldRadius, goldGlow);

        // ---------- 第二层：中层橙金（中等粗细）----------
        float midRadius = goldRadius * 0.55f;
        float midGlow = midRadius * 1.35f;
        BeaconRenderer.renderBeaconBeam(pose, buffer, BeaconRenderer.BEAM_LOCATION, pt, 0.9f, gameTime,     // 速度中等
                0, 384, new float[]{1.0f, 0.88f, 0.45f},   // 橙金
                midRadius, midGlow);

        // ---------- 第三层：内芯纯白（最细，滚动最快）----------
        float coreRadius = goldRadius * 0.22f;
        float coreGlow = coreRadius * 1.2f;
        BeaconRenderer.renderBeaconBeam(pose, buffer, BeaconRenderer.BEAM_LOCATION, pt, 1.6f, gameTime,     // 速度快 → 能量流动感强
                0, 384, new float[]{1.0f, 1.0f, 0.95f},    // 近纯白
                coreRadius, coreGlow);
    }

    @Override
    public void render(BigSwordEntity entity, float entityYaw, float pt, PoseStack pose, MultiBufferSource buffer, int light) {

        Minecraft mc = Minecraft.getInstance();

        float progress = entity.getChargeProgress();
        int state = entity.getState();
        int slashTicks = entity.getSlashTicks();

        // 劈下进度
        float slashProgress = 0f;
        if (state == BigSwordEntity.STATE_SLASHING) {
            slashProgress = (slashTicks + pt) / BigSwordSkill.SLASH_ANIM_TICKS;
            slashProgress = Math.max(0f, Math.min(1f, slashProgress));
        }

        // ===== 大剑模型 =====
        pose.pushPose();
        try {
            pose.translate(0, MODEL_HEIGHT, 0);
            pose.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() + 90f));

            // 摆正
            pose.mulPose(Axis.ZP.rotationDegrees(BASE_ROTATION));
            // 向前劈下
            pose.mulPose(Axis.ZP.rotationDegrees(slashProgress * 120f));

            pose.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
            pose.translate(-CENTER_X, -CENTER_Y + slashProgress * 0.75f, -CENTER_Z);

            WeaponTESR.renderModel(pose, buffer, light, entity.getWeapon());
        } finally {
            pose.popPose();
        }

        /// ===== 圣剑解放光柱 =====
        if (state == BigSwordEntity.STATE_CHARGING && mc.level != null) {
            pose.pushPose();
            try {
                // 从剑尖开始
                pose.translate(-0.5, MODEL_HEIGHT + 4.8 - 0.5, -0.5);
                renderDivineBeam(pose, buffer, progress, pt, mc.level.getGameTime());
            } finally {
                pose.popPose();
            }
        }

        super.render(entity, entityYaw, pt, pose, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(BigSwordEntity entity) {
        return null;
    }
}
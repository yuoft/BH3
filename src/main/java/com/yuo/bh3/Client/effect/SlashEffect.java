package com.yuo.bh3.Client.effect;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.yuo.bh3.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class SlashEffect {

    public static final ResourceLocation TEXTURE = ModUtils.fa("textures/effect/slash.png");

    /** 世界坐标 */
    public double x, y, z;
    /** 朝向 */
    public float yaw, pitch, roll;
    /** 形状 */
    public float radius = 1.6f;
    public float width  = 0.6f;
    public float arcDeg = 120f;
    /** 颜色 */
    public float r = 0.4f, g = 0.7f, b = 1.0f, a = 0.9f;
    /** 生命周期 */
    public int life = 8, maxLife = 8;
    /** 加法混合 */
    public boolean additive = true;

    public SlashEffect setPos(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
        return this;
    }

    public SlashEffect setFacing(float yaw, float pitch, float roll) {
        this.yaw = yaw; this.pitch = pitch; this.roll = roll;
        return this;
    }

    public SlashEffect setShape(float radius, float width, float arcDeg) {
        this.radius = radius; this.width = width; this.arcDeg = arcDeg;
        return this;
    }

    public SlashEffect setColor(float r, float g, float b, float a) {
        this.r = r; this.g = g; this.b = b; this.a = a;
        return this;
    }

    public SlashEffect setLife(int life) {
        this.life = life; this.maxLife = life;
        return this;
    }

    public SlashEffect setAdditive(boolean additive) {
        this.additive = additive;
        return this;
    }

    public void update() {
        --this.life;
    }

    public float alpha(float pt) {
        return this.a * Math.max(0f, (this.life - pt) / (float) this.maxLife);
    }

    /** 在世界里渲染（由 Manager 调用） */
    public void render(PoseStack pose, float pt) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        pose.pushPose();
        pose.translate(x - cam.x, y - cam.y, z - cam.z);
        pose.mulPose(Axis.YP.rotationDegrees(-this.yaw));
        pose.mulPose(Axis.XP.rotationDegrees(this.pitch));
        pose.mulPose(Axis.ZP.rotationDegrees(this.roll));

        // --- 渲染状态 ---
        RenderSystem.enableBlend();
        if (additive) {
            RenderSystem.blendFunc(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE);
        } else {
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
        }
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionTexLightmapColorShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR);

        float alpha = alpha(pt);
        drawArc(buf, pose.last().pose(), alpha);

        BufferUploader.drawWithShader(buf.end());

        // --- 恢复 ---
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        pose.popPose();
    }

    /**
     * 在局部 XZ 平面画一段弧形
     * 沿弧长方向从中心到两端 alpha 渐变，宽度的两端也收窄
     */
    private void drawArc(VertexConsumer buf, Matrix4f m, float alpha) {
        int segments = 24;
        float halfArc = (float) Math.toRadians(this.arcDeg) / 2f;
        float step = halfArc * 2f / segments;

        for (int i = 0; i < segments; i++) {
            float a0 = -halfArc + i * step;
            float a1 = a0 + step;

            // 弧中心最粗，两端收窄
            float w0 = this.width * (1f - Math.abs(a0 / halfArc));
            float w1 = this.width * (1f - Math.abs(a1 / halfArc));
            float rIn0  = this.radius - w0 * 0.5f;
            float rOut0 = this.radius + w0 * 0.5f;
            float rIn1  = this.radius - w1 * 0.5f;
            float rOut1 = this.radius + w1 * 0.5f;

            float sin0 = (float) Math.sin(a0), cos0 = (float) Math.cos(a0);
            float sin1 = (float) Math.sin(a1), cos1 = (float) Math.cos(a1);

            // 内弧两个点
            float xi0 = sin0 * rIn0,  zi0 = cos0 * rIn0;
            float xi1 = sin1 * rIn1,  zi1 = cos1 * rIn1;
            // 外弧两个点
            float xo0 = sin0 * rOut0, zo0 = cos0 * rOut0;
            float xo1 = sin1 * rOut1, zo1 = cos1 * rOut1;

            // 外弧整体暗一些，形成"内亮外淡"的观感
            float ao = alpha * 0.35f;

            addVertex(buf, m, xi0, 0f, zi0, 0f, 0f, r, g, b, alpha);
            addVertex(buf, m, xo0, 0f, zo0, 0f, 1f, r, g, b, ao);
            addVertex(buf, m, xo1, 0f, zo1, 1f, 1f, r, g, b, ao);
            addVertex(buf, m, xi1, 0f, zi1, 1f, 0f, r, g, b, alpha);
        }
    }

    private void addVertex(VertexConsumer buf, Matrix4f m,
                           float x, float y, float z,
                           float u, float v,
                           float r, float g, float b, float a) {
        buf.vertex(m, x, y, z)
                .uv(u, v)
                .uv2(0xF000F0, 0xF000F0)
                .color(r, g, b, a)
                .endVertex();
    }
}
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

public class LineSlashEffect {

    /** 复用同一张纹理，或换成直线型的也行 */
    public static final ResourceLocation TEXTURE = ModUtils.fa("textures/effect/slash.png");

    // 位置
    public double x, y, z;
    // 朝向
    public float yaw, pitch, roll;

    // 形状：直线型
    public float length = 3.0f;   // 总长度
    public float width  = 0.6f;   // 线宽（厚度）

    // 颜色
    public float r = 0.4f, g = 0.75f, b = 1.0f, a = 0.9f;
    // 生命周期
    public int life = 6, maxLife = 6;
    public boolean additive = true;

    public LineSlashEffect setPos(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
        return this;
    }

    public LineSlashEffect setFacing(float yaw, float pitch, float roll) {
        this.yaw = yaw; this.pitch = pitch; this.roll = roll;
        return this;
    }

    public LineSlashEffect setShape(float length, float width) {
        this.length = length; this.width = width;
        return this;
    }

    public LineSlashEffect setColor(float r, float g, float b, float a) {
        this.r = r; this.g = g; this.b = b; this.a = a;
        return this;
    }

    public LineSlashEffect setLife(int life) {
        this.life = life; this.maxLife = life;
        return this;
    }

    public LineSlashEffect setAdditive(boolean additive) {
        this.additive = additive;
        return this;
    }

    public void update() {
        --this.life;
    }

    public float alpha(float pt) {
        return this.a * Math.max(0f, (this.life - pt) / (float) this.maxLife);
    }

    public void render(PoseStack pose, float pt) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        pose.pushPose();
        pose.translate(x - cam.x, y - cam.y, z - cam.z);
        pose.mulPose(Axis.YP.rotationDegrees(-this.yaw));
        pose.mulPose(Axis.XP.rotationDegrees(this.pitch));
        pose.mulPose(Axis.ZP.rotationDegrees(this.roll));

        RenderSystem.enableBlend();
        if (additive) {
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
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

        drawLine(buf, pose.last().pose(), alpha(pt));

        BufferUploader.drawWithShader(buf.end());

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        pose.popPose();
    }

    /**
     * 沿局部 X 轴画一条直线刀光
     * X 从 -length/2 到 +length/2，Y 从 -width/2 到 +width/2
     * 中心最亮，两端逐渐淡出；宽度方向中间最亮，边缘淡
     */
    private void drawLine(VertexConsumer buf, Matrix4f m, float alpha) {
        float halfL = length * 0.5f;
        float halfW = width * 0.5f;

        // 沿长度方向分段，让两端渐变
        int segs = 16;
        float segLen = length / segs;

        for (int i = 0; i < segs; i++) {
            float x0 = -halfL + i * segLen;
            float x1 = x0 + segLen;

            // 两端淡出：|x|/halfL 越靠近 1 越暗
            float a0 = alpha * (1f - Math.abs(x0 / halfL) * Math.abs(x0 / halfL));
            float a1 = alpha * (1f - Math.abs(x1 / halfL) * Math.abs(x1 / halfL));

            // U 从左到右映射 0..1，让纹理沿长度方向拉伸
            float u0 = (x0 + halfL) / length;
            float u1 = (x1 + halfL) / length;

            // 宽度方向：外缘更淡（用 4 个顶点做上下两条边）
            // 上边
            addVertex(buf, m, x0, halfW, 0, u0, 0f, r, g, b, a0 * 0.4f);
            addVertex(buf, m, x1, halfW, 0, u1, 0f, r, g, b, a1 * 0.4f);
            // 下边
            addVertex(buf, m, x1, -halfW, 0, u1, 1f, r, g, b, a1 * 0.4f);
            addVertex(buf, m, x0, -halfW, 0, u0, 1f, r, g, b, a0 * 0.4f);

            // 中心加一层更亮的细线（可选，让线条更锐利）
            float halfWInner = halfW * 0.3f;
            addVertex(buf, m, x0, halfWInner, 0.001f, u0, 0.3f, r, g, b, a0);
            addVertex(buf, m, x1, halfWInner, 0.001f, u1, 0.3f, r, g, b, a1);
            addVertex(buf, m, x1, -halfWInner, 0.001f, u1, 0.7f, r, g, b, a1);
            addVertex(buf, m, x0, -halfWInner, 0.001f, u0, 0.7f, r, g, b, a0);
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
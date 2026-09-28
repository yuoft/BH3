package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * 复刻 ItemTransform.apply 的逻辑
 * 用于在 BEWLR 里手动应用 display json 的变换
 */
public final class ItemTransformHelper {

    private ItemTransformHelper() {}

    /** 对应 json 里的一个 transform 段 */
    public static final class Transform {
        public final float rotX, rotY, rotZ;
        public final float tx, ty, tz;   // 像素单位，就是 json 里的数字
        public final float scale;

        public Transform(float rotX, float rotY, float rotZ, float tx, float ty, float tz, float scale) {
            this.rotX = rotX; this.rotY = rotY; this.rotZ = rotZ;
            this.tx = tx; this.ty = ty; this.tz = tz;
            this.scale = scale;
        }

        public static Transform of(float rx, float ry, float rz,
                                   float tx, float ty, float tz,
                                   float sx, float sy, float sz) {
            // json 里通常 sx=sy=sz，简化处理；若不等，需要分开存
            return new Transform(rx, ry, rz, tx, ty, tz, sx);
        }
    }

    public static final Transform IDENTITY = new Transform(0, 0, 0, 0, 0, 0, 1);

    // ===== 你这段 json 对应的各个上下文 =====
    public static final Transform THIRD_PERSON_RIGHT = new Transform(-30, -90, 0,   4.5f, 0f,   3f,  1f);
    public static final Transform THIRD_PERSON_LEFT  = new Transform(-30,  90, 0,   4.5f, 0f,   3f,  1f);
    public static final Transform FIRST_PERSON_RIGHT = new Transform(-30, -90, 0,   4.5f, 0.5f, 0.5f, 0.85f);
    public static final Transform FIRST_PERSON_LEFT  = new Transform(-30,  90, 0,   4.5f, 0.5f, 0.5f, 0.85f);
    public static final Transform GUI                = new Transform(0, 0, 0,       -4f,  -4f,   0f,  0.75f);
    public static final Transform FIXED              = new Transform(0, 0, 0,       -4f,  -4f,  -3f,  0.75f);

    /** 按上下文取变换 */
    public static Transform forContext(ItemDisplayContext ctx) {
        return switch (ctx) {
            case THIRD_PERSON_RIGHT_HAND -> THIRD_PERSON_RIGHT;
            case THIRD_PERSON_LEFT_HAND  -> THIRD_PERSON_LEFT;
            case FIRST_PERSON_RIGHT_HAND -> FIRST_PERSON_RIGHT;
            case FIRST_PERSON_LEFT_HAND  -> FIRST_PERSON_LEFT;
            case GUI                     -> GUI;
            case FIXED                   -> FIXED;
            default                      -> IDENTITY;
        };
    }

    /**
     * 复刻 ItemTransform.apply 的行为
     *
     * @param poseStack  当前 PoseStack
     * @param t          transform 参数
     * @param leftHand   是否左手
     */
    public static void apply(PoseStack poseStack, Transform t, boolean leftHand) {
        int sign = leftHand ? -1 : 1;

        // 1. 平移：json 里是像素，代码里除 16；X、Z 取反；左手 X 再翻一次
        float tx = -t.tx * 0.0625f * sign;
        float ty =  t.ty * 0.0625f;
        float tz = -t.tz * 0.0625f;
        poseStack.translate(tx, ty, tz);

        // 2. 旋转顺序：Z → Y → X
        if (t.rotZ != 0f) poseStack.mulPose(Axis.ZP.rotationDegrees(t.rotZ));
        if (t.rotY != 0f) poseStack.mulPose(Axis.YP.rotationDegrees(t.rotY * sign));
        if (t.rotX != 0f) poseStack.mulPose(Axis.XP.rotationDegrees(t.rotX));

        // 3. 缩放
        poseStack.scale(t.scale, t.scale, t.scale);
    }

    /** 便捷版：自动根据 ctx 判断左右手 */
    public static void apply(PoseStack poseStack, ItemDisplayContext ctx) {
        boolean leftHand = (ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        apply(poseStack, forContext(ctx), leftHand);
    }
}
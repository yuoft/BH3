package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

/**
 * 对应 display json 里的一个上下文段
 * 注意 translation 单位是像素，除 16 才是方块
 */
public final class ItemTransform {

    public static final ItemTransform IDENTITY = new ItemTransform(0, 0, 0, 0, 0, 0, 1);

    public final float rotX, rotY, rotZ;
    public final float tx, ty, tz;      // 像素
    public final float scale;

    public ItemTransform(float rotX, float rotY, float rotZ,
                         float tx, float ty, float tz,
                         float scale) {
        this.rotX = rotX; this.rotY = rotY; this.rotZ = rotZ;
        this.tx = tx; this.ty = ty; this.tz = tz;
        this.scale = scale;
    }

    /** 复刻 ItemTransform.apply 的行为 */
    public void apply(PoseStack pose, boolean leftHand) {
        int sign = leftHand ? -1 : 1;

        // 平移：像素 → 方块；X、Z 取反；左手 X 再翻
        pose.translate(-tx * 0.0625f * sign, ty * 0.0625f, -tz * 0.0625f);

        // 旋转顺序：Z → Y → X（与原版一致，不能改）
        if (rotZ != 0f) pose.mulPose(Axis.ZP.rotationDegrees(rotZ));
        if (rotY != 0f) pose.mulPose(Axis.YP.rotationDegrees(rotY * sign));
        if (rotX != 0f) pose.mulPose(Axis.XP.rotationDegrees(rotX));

        // 缩放
        if (scale != 1f) pose.scale(scale, scale, scale);
    }
}
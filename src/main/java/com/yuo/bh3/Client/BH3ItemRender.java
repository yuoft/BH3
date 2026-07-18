package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.bh3.BH3;
import com.yuo.bh3.ModUtils;
import com.yuo.endless.Client.Lib.CCModel;
import com.yuo.endless.Client.Lib.CCRenderState;
import com.yuo.endless.Client.Lib.OBJParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BH3ItemRender extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation WEAPON_OBJ = ModUtils.fa("model/bh3_fld.obj");
    private static final ResourceLocation WEAPON_TEXTURE = ModUtils.fa("textures/weapon/fld.png");

    private final CCModel weaponModel;

    public BH3ItemRender() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        // 在构造函数中加载 OBJ 模型（或延迟加载）
        this.weaponModel = new OBJParser(WEAPON_OBJ).parse().get("model");
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();

        // 1. 应用变换（根据上下文调整位置/旋转/缩放）
        applyTransform(ctx, poseStack);

        // 2. 设置渲染类型（如果模型有透明度，使用 translucent；否则用 cutout）
        RenderType renderType = RenderType.entityCutoutNoCull(WEAPON_TEXTURE);

        // 3. 初始化 CCRenderState（与无尽贪婪相同）
        CCRenderState cc = CCRenderState.instance();
        cc.reset();
        cc.bind(renderType, buffer, poseStack);
        cc.baseColour = 0xFFFFFFFF; // 白色不透明，可修改为动态颜色

        // 4. 渲染模型
        weaponModel.render(cc);

        poseStack.popPose();
    }

    private void applyTransform(ItemDisplayContext ctx, PoseStack poseStack) {
        // 参考 GapingVoidRender 中的变换逻辑，或自定义
        switch (ctx) {
            case GUI -> {
                poseStack.scale(1.0F, 1.0F, 1.0F);
                poseStack.translate(0.5, 0.5, 0.5);
            }
            case GROUND -> {
                poseStack.scale(0.8F, 0.8F, 0.8F);
                poseStack.translate(0.0, 0.1, 0.0);
            }
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> {
                poseStack.scale(1.2F, 1.2F, 1.2F);
                poseStack.translate(0.3, 0.2, 0.0);
            }
            // 其他上下文...
            default -> {
            }
        }
    }
}
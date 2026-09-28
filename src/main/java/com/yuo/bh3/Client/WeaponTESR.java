package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yuo.bh3.Items.BH3WeaponBigSword;
import com.yuo.bh3.Items.BH3WeaponLance;
import com.yuo.bh3.Items.BH3WeaponSickle;
import com.yuo.bh3.ModUtils;
import com.yuo.endless.client.lib.CCModel;
import com.yuo.endless.client.lib.CCRenderState;
import com.yuo.endless.client.lib.OBJParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class WeaponTESR extends BlockEntityWithoutLevelRenderer {

    private static final float SCALE_GROUND  = 0.95F;
    private static final float SCALE_DEFAULT = 0.95F;

    /** 缓存每个物品对应的 2D 图标模型 */
    private static final Map<ResourceLocation, BakedModel> ICON_CACHE = new HashMap<>();
    /** 缓存 OBJ 模型，避免每次解析 */
    private static final Map<ResourceLocation, CCModel> MODEL_CACHE = new HashMap<>();

    public WeaponTESR(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        // ---- 读当前玩家的挥动进度 ----
        LocalPlayer player = Minecraft.getInstance().player;
        float swing = 0f;
        if (player != null) {
            float partialTick = Minecraft.getInstance().getFrameTime();
            swing = player.getAttackAnim(partialTick);   // 0 → 1 → 0 的曲线
        }

        poseStack.pushPose();
        try {
            // ---- 应用攻击动画 ----
//            applyAttackAnim(poseStack, ctx, swing, stack);

            if (ctx == ItemDisplayContext.GUI || ctx == ItemDisplayContext.FIXED) {
                renderIcon(stack, ctx, poseStack, buffer, light, overlay);
            } else {
                renderModel(poseStack, buffer, ctx, stack, light, SCALE_DEFAULT);
            }
        } finally {
            poseStack.popPose();
        }
    }

    /** 自定义攻击动画 */
    private static void applyAttackAnim(PoseStack poseStack, ItemDisplayContext ctx, float swing, ItemStack stack) {
        if (swing <= 0f) return;
        // 第一人称：原版已处理，不重复做
        if (ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            return;
        }

        // 第三人称
        if (ctx != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                && ctx != ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            return;
        }

        Item item = stack.getItem();

        if (item instanceof BH3WeaponBigSword) {
            // 劈砍：抬起 → 劈下，峰值在 swing=0.5
            float f = Mth.sin(swing * (float) Math.PI);
            poseStack.mulPose(Axis.XP.rotationDegrees(-120f * f));
        } else if (item instanceof BH3WeaponLance) {
            // 前刺：快速冲出，慢收
            float f = Mth.sin(Mth.sqrt(swing) * (float) Math.PI);
            poseStack.translate(0f, 0f, -0.6f * f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-45f * f));
        } else if (item instanceof BH3WeaponSickle) {
            // 横扫：匀速扫过
            float f = swing;
            poseStack.mulPose(Axis.YP.rotationDegrees(-90f * f));
            poseStack.translate(0f, 0f, -0.2f * f);
        }
    }

    /**
     * 3D OBJ 渲染
     */
    private void renderModel(PoseStack poseStack, MultiBufferSource buffer, ItemDisplayContext idc, ItemStack stack, int light, float scale) {
        CCModel model = getWeaponModel(stack);
        if (model == null) return;

        poseStack.scale(scale, scale, scale);
        ResourceLocation res = ModUtils.fa("textures/weapon/" + getItemID(stack) + ".png");
        RenderType renderType = BH3RenderTypes.weapon(res);
        CCRenderState cc = CCRenderState.instance();
        cc.reset();
        cc.bind(renderType, buffer, poseStack);
        cc.baseColour = 0xFFFFFFFF;
        cc.brightness = light;
        cc.overlay = 0xFFFF;
        model.render(cc);
    }

    /**
     * 用原版方式渲染 2D 图标（item/generated）
     */
    private static void renderIcon(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel iconModel = getIconModel(stack);
        if (iconModel == null || iconModel == mc.getModelManager().getMissingModel()) {
            return;
        }

        RenderType rt = ItemBlockRenderTypes.getRenderType(stack, false);
        VertexConsumer consumer = buffer.getBuffer(rt);
        mc.getItemRenderer().renderModelLists(iconModel, stack, light, overlay, poseStack, consumer);
    }


    /**
     * 获取崩坏武器id
     */
    private static String getItemID(ItemStack stack) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (key == null) return "";
        String path = key.getPath();
        return path.startsWith("bh3_") ? path.substring(4) : path;
    }

    /**
     * 获取崩坏武器3d模型
     */
    private CCModel getWeaponModel(ItemStack stack) {
        ResourceLocation obj = ModUtils.fa("model/" + getItemID(stack) + ".obj");
        return MODEL_CACHE.computeIfAbsent(obj, loc -> {
            CCModel m = new OBJParser(loc).parse().get("model");
            if (m == null) throw new IllegalStateException("OBJ Model loading error! " + loc);
            return m;
        });
    }

    /**
     * 获取崩坏武器2d模型
     */
    private static BakedModel getIconModel(ItemStack stack) {
        ResourceLocation modelId = ModUtils.fa("bh3_" + getItemID(stack) + "_icon");
        return ICON_CACHE.computeIfAbsent(modelId, k -> {
            ModelResourceLocation loc = new ModelResourceLocation(k, "inventory");
            return Minecraft.getInstance().getModelManager().getModel(loc);
        });
    }

}
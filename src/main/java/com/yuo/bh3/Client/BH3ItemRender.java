package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.bh3.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BH3ItemRender extends BlockEntityWithoutLevelRenderer {

    public BH3ItemRender() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack poseStack, MultiBufferSource source, int i, int j) {
        poseStack.pushPose();
        if (isRender(type)) {
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer(); //使用钻石剑模型
            BakedModel model = renderer.getItemModelShaper().getItemModel(Items.DIAMOND_SWORD);
            VertexConsumer vertexBuilder = source.getBuffer(RenderType.cutout());
            if (model != null) {
                renderer.renderModelLists(model, stack, i, j, poseStack, vertexBuilder);
            }
        }
        poseStack.popPose();
    }

    /**
     * 判断是否替换物品模型
     * @param type 渲染类型
     * @return 结果
     */
    public static boolean isRender(ItemDisplayContext type){
        if (type == ItemDisplayContext.GUI) return !Config.SERVER.isRenderGui.get();
        if (type == ItemDisplayContext.FIXED) return !Config.SERVER.isRenderFixed.get();
        if (type == ItemDisplayContext.GROUND) return !Config.SERVER.isRenderGround.get();
        return false;
    }
}

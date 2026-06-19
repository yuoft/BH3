package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.bh3.Config;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("removal")
public class ItemModel implements BakedModel {
    private final BakedModel objModel;      // 您的OBJ模型
    private final BakedModel diamondModel;  // 钻石剑模型（替代品）

    public ItemModel(BakedModel objModel, BakedModel diamondModel) {
        this.objModel = objModel;
        this.diamondModel = diamondModel;
    }

    private BakedModel getModelForContext(ItemDisplayContext context) {
        return isRender(context) ? diamondModel : objModel;
    }

    /**
     * 判断是否替换物品模型
     * @param type 渲染类型
     * @return 结果
     */
    public static boolean isRender(ItemDisplayContext type){
//        if (type == ItemDisplayContext.GUI) return !Config.SERVER.isRenderGui.get();
        if (type == ItemDisplayContext.FIXED) return !Config.SERVER.isRenderFixed.get();
//        if (type == ItemDisplayContext.GROUND) return !Config.SERVER.isRenderGround.get();
        return false;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction dir, RandomSource rand) {
        return objModel.getQuads(state, dir, rand);
    }

    // 核心：根据上下文选择模型并应用变换
    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        BakedModel selected = getModelForContext(transformType);
        // 如果选中的模型就是自身（防止递归），直接调用其applyTransform
        // 但这里selected不会是this，因为this是包装类，selected是内部模型
        return selected.applyTransform(transformType, poseStack, applyLeftHandTransform);
    }

    // 其他委托方法
    @Override
    public boolean useAmbientOcclusion() { return objModel.useAmbientOcclusion(); }
    @Override
    public boolean isGui3d() { return objModel.isGui3d(); }
    @Override
    public boolean usesBlockLight() { return objModel.usesBlockLight(); }
    @Override
    public boolean isCustomRenderer() { return false; } // 关键：禁用自定义渲染
    @Override
    public TextureAtlasSprite getParticleIcon() { return objModel.getParticleIcon(); }
    @Override
    public ItemOverrides getOverrides() { return objModel.getOverrides(); }
}
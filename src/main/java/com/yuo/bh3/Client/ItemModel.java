package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemModel implements BakedModel {
    private BakedModel bakedModel;
    public ItemModel(BakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(@org.jetbrains.annotations.Nullable BlockState blockState, @org.jetbrains.annotations.Nullable Direction direction, RandomSource randomSource) {
        return this.bakedModel.getQuads(blockState, direction, randomSource);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.bakedModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.bakedModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.bakedModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.bakedModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.bakedModel.getOverrides();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        if (BH3ItemRender.isRender(transformType)) {
            return this;
        }
        return this.bakedModel.applyTransform(transformType, poseStack, applyLeftHandTransform);
    }
}

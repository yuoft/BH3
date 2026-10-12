package com.yuo.bh3.Client.lib;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ISpriteAwareVertexConsumer extends VertexConsumer {
    void sprite(TextureAtlasSprite var1);
}

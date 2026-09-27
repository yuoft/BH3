package com.yuo.bh3.Client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BH3RenderTypes extends RenderStateShard {

    private static final Map<ResourceLocation, RenderType> CACHE = new HashMap<>();

    protected BH3RenderTypes(String name, Runnable setup, Runnable clear) {
        super(name, setup, clear);
    }

    public static RenderType weapon(ResourceLocation texture) {
        return CACHE.computeIfAbsent(texture, tex -> {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER)
                    .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                    .setTextureState(new RenderStateShard.TextureStateShard(tex, false, true))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(true);
            return RenderType.create("bh3_weapon_" + tex, DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.TRIANGLES, 256, true, false, state);
        });
    }
}
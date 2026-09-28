package com.yuo.bh3.Client;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;

import java.util.HashMap;
import java.util.Map;

public final class WeaponTransformHelper {

    public static final Map<String, TransformParams> TRANSFORMS = new HashMap<>();

    static {
        // sword / fld
        TRANSFORMS.put("sword", new TransformParams(
                // 第三人称右手
                -30, -90, 0, 3.5f, 0f, 3f, 1.25f,
                // 第三人称左手
                -30,  90, 0, 3.5f, 0f, 3f, 1.25f,
                // 第一人称右手
                -30, -90, 0, 3f, 0.5f, 0.5f, 0.75f,
                // 第一人称左手
                -30,  90, 0, 3.5f, 0.5f, 0.5f, 0.75f
        ));

        // big_sword
        TRANSFORMS.put("big_sword", new TransformParams(
                -30, -90, 0, 3.5f, 0.5f, 3f, 1.25f,
                -30,  90, 0, 3.5f, 0.5f, 3f, 1.25f,
                -30, -90, 0, 3f, 0.5f, 0.5f, 0.75f,
                -30,  90, 0, 3.5f, 0.5f, 0.5f, 0.75f
        ));

        // bow
        TRANSFORMS.put("bow", new TransformParams(
                -30, -90, 0, 2.5f, 0f, 1f, 0.85f,
                -30,  90, 0, 2.5f, 0f, 1f, 0.85f,
                -30, -90, 0, 2.5f, 0.5f, 0.5f, 0.75f,
                -30,  90, 0, 2.5f, 0.5f, 0.5f, 0.75f
        ));

        // lance
        TRANSFORMS.put("lance", new TransformParams(
                -30, -90, 0, 3.75f, 6f, 4f, 1.25f,
                -30,  90, 0, 3.75f, 6f, 4f, 1.25f,
                -30, -90, 0, 3f, 0.5f, 0.5f, 0.75f,
                -30,  90, 0, 3f, 0.5f, 0.5f, 0.75f
        ));

        // sickle
        TRANSFORMS.put("sickle", new TransformParams(
                -30, -90, 0, -0.25f, 6f, 4f, 1.25f,
                -30,  90, 0, -0.25f, 6f, 4f, 1.25f,
                -30, -90, 0, -0.25f, 0.5f, 0.5f, 0.75f,
                -30,  90, 0, -0.25f, 0.5f, 0.5f, 0.75f
        ));
    }

    private WeaponTransformHelper() {
    }

    /** 查表，没匹配到回退到 sword */
    public static TransformParams get(String id) {
        return TRANSFORMS.getOrDefault(id, TRANSFORMS.get("sword"));
    }

    /**
     * 只生成第三人称 / 第一人称的 display，不生成 GUI / FIXED
     * 左手参数直接读表，不做取反
     */
    public static void applyTransforms(ItemModelBuilder builder, TransformParams p) {
        ModelBuilder<ItemModelBuilder>.TransformsBuilder tb = builder.transforms();

        // 第三人称右手
        tb.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(p.trRotX(), p.trRotY(), p.trRotZ())
                .translation(p.trTx(), p.trTy(), p.trTz())
                .scale(p.trScale()).end();

        // 第三人称左手（独立参数）
        tb.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(p.tlRotX(), p.tlRotY(), p.tlRotZ())
                .translation(p.tlTx(), p.tlTy(), p.tlTz())
                .scale(p.tlScale()).end();

        // 第一人称右手
        tb.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(p.frRotX(), p.frRotY(), p.frRotZ())
                .translation(p.frTx(), p.frTy(), p.frTz())
                .scale(p.frScale()).end();

        // 第一人称左手（独立参数）
        tb.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(p.flRotX(), p.flRotY(), p.flRotZ())
                .translation(p.flTx(), p.flTy(), p.flTz())
                .scale(p.flScale()).end();

        tb.end();
    }

    /**
     * 4 组独立参数：第三人称右/左、第一人称右/左
     */
    public record TransformParams(
            // 第三人称右手
            float trRotX, float trRotY, float trRotZ,
            float trTx, float trTy, float trTz, float trScale,
            // 第三人称左手
            float tlRotX, float tlRotY, float tlRotZ,
            float tlTx, float tlTy, float tlTz, float tlScale,
            // 第一人称右手
            float frRotX, float frRotY, float frRotZ,
            float frTx, float frTy, float frTz, float frScale,
            // 第一人称左手
            float flRotX, float flRotY, float flRotZ,
            float flTx, float flTy, float flTz, float flScale
    ) {}
}
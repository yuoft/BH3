package com.yuo.bh3.Client.effect;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Items.BH3BaseWeapon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.bh3.Items.BH3WeaponBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SlashEffectManager {

    private static final List<SlashEffect> EFFECTS = new ArrayList<>();
    private static final List<LineSlashEffect> LINE_EFFECTS = new ArrayList<>();
    private static final List<SlashEffect> PENDING = new ArrayList<>();
    private static final List<LineSlashEffect> LINE_PENDING = new ArrayList<>();

    private static boolean wasSwinging = false;

    private SlashEffectManager() {}

    public static void spawn(SlashEffect e) {
        PENDING.add(e);
    }
    public static void spawn(LineSlashEffect e) { LINE_PENDING.add(e); }

    // -------- tick 更新 + 检测攻击 --------
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (event.phase == TickEvent.Phase.START) {
            if (player != null) {
                for (SlashEffect e : EFFECTS) e.update();
                for (LineSlashEffect e : LINE_EFFECTS) e.update();
            }
            return;
        }

        // END 阶段：处理队列
        EFFECTS.addAll(PENDING);
        PENDING.clear();
        LINE_EFFECTS.addAll(LINE_PENDING);
        LINE_PENDING.clear();

        EFFECTS.removeIf(e -> e.life <= 0);
        LINE_EFFECTS.removeIf(e -> e.life <= 0);

        // 检测"挥动开始"：swinging 从 false → true
        if (player == null) {
            wasSwinging = false;
            return;
        }
        if (player.swinging && !wasSwinging) {
            ItemStack handItem = player.getMainHandItem();
            if (handItem.getItem() instanceof BH3BaseWeapon && !(handItem.getItem() instanceof BH3WeaponBow)) {
                spawnSlashFor(player);
            }
        }
        wasSwinging = player.swinging;
    }

    private static void spawnSlashFor(LocalPlayer p) {
        Vec3 eye  = p.getEyePosition();
        Vec3 look = p.getLookAngle();

        double x, y, z;
        HitResult mcHit = Minecraft.getInstance().hitResult;
        if (mcHit instanceof EntityHitResult ehr) {
            Entity target = ehr.getEntity();
            x = target.getX();
            y = target.getY() + target.getBbHeight() * 0.5;
            z = target.getZ();
        } else {
            x = eye.x + look.x * 1.2;
            y = eye.y + look.y * 1.2 - 0.3;
            z = eye.z + look.z * 1.2;
        }

        float roll = 30f + p.getRandom().nextFloat() * 120f;

        SlashEffect slash = new SlashEffect().setPos(x, y, z).setFacing(p.getYRot(), p.getXRot(), roll)
                .setShape(1.8f, 0.6f, 120f)
                .setColor(0.6f, 0.6f, 1.0f, 0.9f).setLife(8).setAdditive(true);

        spawn(slash);
    }

    // -------- 世界渲染 --------
    @SubscribeEvent
    public static void onRenderStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        PoseStack pose = event.getPoseStack();
        float pt = event.getPartialTick();

        for (SlashEffect e : EFFECTS) {
            if (e.life > 0) e.render(pose, pt);
        }

        for (LineSlashEffect e : LINE_EFFECTS) {
            if (e.life > 0) e.render(pose, pt);
        }
    }
}
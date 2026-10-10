package com.yuo.bh3.capability;

import com.yuo.bh3.BH3;
import com.yuo.bh3.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HonkaiEnergyHud {

    private static final ResourceLocation GUI_BARS = ModUtils.def("textures/gui/bars.png");
    private static final int BAR_ROW_HEIGHT = 10;
    private static final int BAR_W = 60;
    private static final int BAR_H = 5;
    private static final int BAR_ROW = 5; // 0 = 末影龙粉色

    public static final IGuiOverlay HONKAI_ENERGY_BAR = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.player.isSpectator()) return;

        mc.player.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
            float ratio = Math.max(0f, Math.min(1f, energy.getRatio()));

            // ★ 屏幕左下角
            int x = 10;
            int y = screenHeight - 10;

            // 底槽 + 边框
            int bgV = BAR_ROW * BAR_ROW_HEIGHT;
            guiGraphics.blit(GUI_BARS, x, y, 0, bgV, BAR_W, BAR_H, 256, 256);

            // 彩色填充
            int fillW = (int) (BAR_W * ratio);
            if (fillW > 0) {
                int fillV = BAR_ROW * BAR_ROW_HEIGHT + 5;
                guiGraphics.blit(GUI_BARS, x, y, 0, fillV, fillW, BAR_H, 256, 256);
            }
        });
    };

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("honkai_energy_bar", HONKAI_ENERGY_BAR);
    }
}
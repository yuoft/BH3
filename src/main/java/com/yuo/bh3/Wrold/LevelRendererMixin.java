package com.yuo.bh3.Wrold;

import com.yuo.bh3.Items.BH3BaseWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
    private void bh3$filterSweepParticle(ParticleOptions options,
                                         boolean forceAlwaysRender,
                                         boolean decrease,
                                         double x, double y, double z,
                                         double xSpeed, double ySpeed, double zSpeed,
                                         CallbackInfo ci) {
        if (options.getType() == ParticleTypes.SWEEP_ATTACK) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null
                    && player.getMainHandItem().getItem() instanceof BH3BaseWeapon) {
                ci.cancel();
            }
        }
    }
}
package com.yuo.bh3.Items;

import com.yuo.bh3.Client.BH3ItemRender;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class FLD extends BH3WeaponSword{
    public FLD(WeaponAttributes wa) {
        super(wa);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new BH3ItemRender();
            }
        });
    }
}

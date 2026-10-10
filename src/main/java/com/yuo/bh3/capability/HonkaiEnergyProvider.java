package com.yuo.bh3.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HonkaiEnergyProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<IHonkaiEnergy> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private final HonkaiEnergyImpl impl = new HonkaiEnergyImpl();
    private final LazyOptional<IHonkaiEnergy> optional = LazyOptional.of(() -> impl);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return impl.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        impl.deserializeNBT(nbt);
    }

    /** 便捷获取 */
    public static LazyOptional<IHonkaiEnergy> get(Player player) {
        return player.getCapability(CAP);
    }
}
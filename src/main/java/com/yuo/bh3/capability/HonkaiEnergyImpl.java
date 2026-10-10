package com.yuo.bh3.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class HonkaiEnergyImpl implements IHonkaiEnergy {

    /** 默认上限 */
    public static final float DEFAULT_MAX = 100f;
    /** 每 tick 回复量 */
    public static final float REGEN_PER_TICK = 0.01f;    // 200 秒回满 0.01 * 20 * 100

    private float energy;
    private float maxEnergy = DEFAULT_MAX;

    public HonkaiEnergyImpl() {
        this.energy = DEFAULT_MAX;
    }

    @Override
    public float getEnergy() { return energy; }

    @Override
    public void setEnergy(float value) {
        this.energy = Math.max(0f, Math.min(maxEnergy, value));
    }

    @Override
    public float getMaxEnergy() { return maxEnergy; }

    @Override
    public void setMaxEnergy(float value) {
        this.maxEnergy = Math.max(1f, value);
        if (this.energy > maxEnergy) this.energy = maxEnergy;
    }

    @Override
    public void tick(Player player) {
        // 服务端才回复
        if (player.level().isClientSide) return;

        if (energy < maxEnergy) {
            energy = Math.min(maxEnergy, energy + REGEN_PER_TICK);
        }
    }

    public void copyFrom(IHonkaiEnergy other) {
        this.energy = other.getEnergy();
        this.maxEnergy = other.getMaxEnergy();
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Energy", energy);
        tag.putFloat("MaxEnergy", maxEnergy);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.energy = tag.getFloat("Energy");
        this.maxEnergy = tag.contains("MaxEnergy") ? tag.getFloat("MaxEnergy") : DEFAULT_MAX;
    }
}
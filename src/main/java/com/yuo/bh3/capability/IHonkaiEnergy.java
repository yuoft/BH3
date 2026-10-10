package com.yuo.bh3.capability;

import net.minecraft.world.entity.player.Player;

public interface IHonkaiEnergy {

    float getEnergy();

    void setEnergy(float value);

    float getMaxEnergy();

    void setMaxEnergy(float value);

    /** 返回当前比例 0~1，方便 HUD 用 */
    default float getRatio() {
        float max = getMaxEnergy();
        return max <= 0 ? 0 : getEnergy() / max;
    }

    /** 是否足够消耗 */
    default boolean has(float amount) {
        return getEnergy() >= amount;
    }

    /**
     * 尝试消耗，成功返回 true，失败不动
     */
    default boolean consume(float amount) {
        if (!has(amount)) return false;
        setEnergy(getEnergy() - amount);
        return true;
    }

    /** 回复 / 增加 */
    default void add(float amount) {
        setEnergy(Math.min(getMaxEnergy(), getEnergy() + amount));
    }

    /** 每 tick 调用（服务端） */
    void tick(Player player);
}
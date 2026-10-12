package com.yuo.bh3.Entity;

import com.yuo.bh3.Items.BH3WeaponBigSword;
import com.yuo.bh3.Items.skill.BigSwordSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class BigSwordEntity extends Entity {

    public static final int STATE_CHARGING = 0;
    public static final int STATE_SLASHING = 1;
    private static final int SLASH_LINGER_TICKS = 2;   // 动画结束后多活 2 tick

    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(BigSwordEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_CHARGE_TICKS = SynchedEntityData.defineId(BigSwordEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_SLASH_TICKS = SynchedEntityData.defineId(BigSwordEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(BigSwordEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> DATA_CHARGE_PROGRESS = SynchedEntityData.defineId(BigSwordEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(BigSwordEntity.class, EntityDataSerializers.ITEM_STACK);

    public BigSwordEntity(Level level) {
        super(BH3EntityTypes.BIG_SWORD.get(), level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public BigSwordEntity(Level level, Player owner) {
        super(BH3EntityTypes.BIG_SWORD.get(), level);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.setOwnerUUID(owner.getUUID());
        this.setWeapon(owner.getUseItem());
        this.setState(STATE_CHARGING);
        this.setChargeTicks(0);
        this.setSlashTicks(0);
        this.setChargeProgress(0f);
        this.setPos(owner.getX(), owner.getY(), owner.getZ());
        this.setYRot(owner.getYRot());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("entity.bh3.big_sword", getWeapon().getDisplayName());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_STATE, STATE_CHARGING);
        this.entityData.define(DATA_CHARGE_TICKS, 0);
        this.entityData.define(DATA_SLASH_TICKS, 0);
        this.entityData.define(DATA_OWNER, Optional.empty());
        this.entityData.define(DATA_CHARGE_PROGRESS, 0f);
        this.entityData.define(DATA_WEAPON, ItemStack.EMPTY);
    }

    // ===== getter / setter =====
    public int getState() {
        return this.entityData.get(DATA_STATE);
    }

    public void setState(int state) {
        this.entityData.set(DATA_STATE, state);
    }

    public int getChargeTicks() {
        return this.entityData.get(DATA_CHARGE_TICKS);
    }

    public void setChargeTicks(int t) {
        this.entityData.set(DATA_CHARGE_TICKS, t);
    }

    public int getSlashTicks() {
        return this.entityData.get(DATA_SLASH_TICKS);
    }

    public void setSlashTicks(int t) {
        this.entityData.set(DATA_SLASH_TICKS, t);
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(DATA_OWNER);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(DATA_OWNER, Optional.of(uuid));
    }

    public float getChargeProgress() {
        return this.entityData.get(DATA_CHARGE_PROGRESS);
    }

    public void setChargeProgress(float p) {
        this.entityData.set(DATA_CHARGE_PROGRESS, p);
    }

    public ItemStack getWeapon() {
        return this.entityData.get(DATA_WEAPON);
    }

    public void setWeapon(ItemStack stack) {
        this.entityData.set(DATA_WEAPON, stack);
    }

    public Player getOwner() {
        Optional<UUID> uuid = getOwnerUUID();
        if (uuid.isEmpty()) return null;
        return this.level().getPlayerByUUID(uuid.get());
    }

    @Override
    public void tick() {
        super.tick();

        Level level = this.level();
        if (level.isClientSide) return;

        Player owner = getOwner();
        if (owner == null || !owner.isAlive()) {
            this.discard();
            return;
        }

        // 跟随玩家位置
        this.setPos(owner.getX(), owner.getY(), owner.getZ());
        this.setYRot(owner.getYRot());

        if (getState() == STATE_CHARGING) { //蓄力计时
            // 玩家没在蓄力了 → 立刻劈下
            boolean stillCharging = owner.isUsingItem() && owner.getUseItem().getItem() instanceof BH3WeaponBigSword;

            if (!stillCharging) {
                // ★ 玩家松手
                if (getChargeTicks() < BigSwordSkill.CHARGE_MIN_TICKS) {
                    // 蓄力不足 → 直接消失，不劈下
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.6f, 1.2f);
                    }
                    this.discard();
                } else {
                    // 蓄力足够 → 劈下
                    setState(STATE_SLASHING);
                    setSlashTicks(0);
                }
                return;
            }

            int ticks = getChargeTicks() + 1;
            setChargeTicks(ticks);
            setChargeProgress(Math.min(1f, (float) ticks / BigSwordSkill.CHARGE_MAX_TICKS));

        } else if (getState() == STATE_SLASHING) { //下劈计时
            int ticks = getSlashTicks() + 1;
            setSlashTicks(ticks);
            if (ticks == BigSwordSkill.SLASH_ANIM_TICKS / 2) {   // 动画中点触发伤害
                if (level instanceof ServerLevel serverLevel) {
                    BigSwordSkill.execute(serverLevel, owner, getChargeTicks());
                }
            }
            if (ticks > BigSwordSkill.SLASH_ANIM_TICKS + SLASH_LINGER_TICKS) {
                this.discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
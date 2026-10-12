package com.yuo.bh3.Entity;

import com.yuo.bh3.Items.skill.SickleSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class SickleEntity extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(SickleEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<ItemStack> DATA_WEAPON = SynchedEntityData.defineId(SickleEntity.class, EntityDataSerializers.ITEM_STACK);

    public SickleEntity(Level level) {
        super(BH3EntityTypes.SICKLE.get(), level);
    }

    public SickleEntity(Level level, Player owner) {
        super(BH3EntityTypes.SICKLE.get(), level);
        this.setOwnerUUID(owner.getUUID());
        this.setWeapon(owner.getUseItem());
        this.setPos(owner.getX(), owner.getY() + 0.75f, owner.getZ());
        this.setYRot(owner.getYRot()); //记录初始角度，后续不再变化
    }

    public SickleEntity(EntityType<SickleEntity> sickleEntityEntityType, Level level) {
        super(sickleEntityEntityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        Player owner = getOwner();
        if (owner == null || !owner.isAlive()) {
            this.discard();
            return;
        }
        if (this.level().isClientSide) return;

        if (tickCount < SickleSkill.PULL_DURATION) {
            // 拉取阶段
            SickleSkill.tickPull(owner, tickCount);
        } else if (tickCount == SickleSkill.PULL_DURATION) {
            // 开始横扫
            SickleSkill.startSlash(owner);
        } else if (tickCount <= SickleSkill.PULL_DURATION + SickleSkill.SLASH_DURATION) {
            // 横扫中
            int slashTick = tickCount - SickleSkill.PULL_DURATION;
            if (slashTick == SickleSkill.SLASH_HIT_TICK) {
                SickleSkill.doSlashDamage(owner);
            }
        } else {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_OWNER, Optional.empty());
        this.entityData.define(DATA_WEAPON, ItemStack.EMPTY);
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(DATA_OWNER);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(DATA_OWNER, Optional.of(uuid));
    }

    public ItemStack getWeapon() {
        return this.entityData.get(DATA_WEAPON);
    }

    public void setWeapon(ItemStack stack) {
        this.entityData.set(DATA_WEAPON, stack);
    }

    public Player getOwner() {
        Optional<UUID> uuid = getOwnerUUID();
        return uuid.map(value -> this.level().getPlayerByUUID(value)).orElse(null);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
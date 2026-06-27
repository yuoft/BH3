package com.yuo.bh3.Items;

import net.minecraft.world.item.Tier;

/**
 * 武器属性枚举类
 */
public enum WeaponAttributes {
    // ==================== 镰刀 (BH3WeaponSickle) ====================
    // 基类: BH3WeaponSickle -> super(BH3ItemTiers.BH_30, -1, -2.4f)
    // 总攻击力 = tier.getAttackDamageBonus() + damage = 15.0F + (-1) = 14.0F
    BAX(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),     // 星刃·白矮星
    BAZF(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 彼岸之扉
    BAZFL(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),   // 彼岸之扉·连理
    HSZS(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 恒霜之斯卡蒂
    MFXX(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 冥府携香
    SYZ(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),     // 裁星刃·双鱼座
    TSZS(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 天霜之斯卡蒂
    XSZJ(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 撷生之径·你我如一
    XSZY(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 撷生之谣
    XYZM(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),    // 血渊之眸
    XYZMY(BH3ItemTiers.BH_30, -1, -2.4f, 0, 0),   // 血渊之眸·如一

    // ==================== 太刀 (BH3WeaponSword) ====================
    // 基类: BH3WeaponSword -> super(BH3ItemTiers.BH_31, 0, -2.6f)
    // 总攻击力 = 15.0F + 0 = 15.0F
    AXYW(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 安息鸢尾
    CBZ(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),      // 磁暴·斩
    CNZJ(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 澄凝之境·自性纯一
    CNZY(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 澄凝之钥
    CRY(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),      // 妖刀·赤染樱
    DZQL(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 涤罪七雷·鸣
    DZQLM(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),    // 涤罪七雷·鸣雷见
    FLD(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),      // 仿灵刀·冰昙天
    HYD(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),      // 魂妖刀·血樱寂灭
    TJZJ(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 天殛之境·裁决
    TJZY(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 天殛之钥
    YLD(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),      // 御灵刀·寒狱冰天
    YYQW(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),     // 夜宴蔷薇
    YYQWX(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),    // 夜宴蔷薇·虚之冕
    ZCB(BH3ItemTiers.BH_31, 0, -2.6f, 0, 0),      // 重磁暴·斩

    // ==================== 大剑 (BH3WeaponBigSword) ====================
    // 基类: BH3WeaponBigSword -> super(BH3ItemTiers.BH_32, 2, -3.0f)
    // 总攻击力 = 15.0F + 2 = 17.0F
    JM(BH3ItemTiers.BH_32, 2, -3.0f, 0, 0),       // 劫灭
    JMWJ(BH3ItemTiers.BH_32, 2, -3.0f, 0, 0),     // 劫灭无烬
    NYJ(BH3ItemTiers.BH_32, 2, -3.0f, 0, 0),      // 涅炎剑·史尔特尔
    SYJ(BH3ItemTiers.BH_32, 2, -3.0f, 0, 0),      // 神陨剑·史尔特尔

    // ==================== 骑枪 (BH3WeaponLance) ====================
    // 基类: BH3WeaponLance -> super(BH3ItemTiers.BH_33, 1, -2.8f)
    // 总攻击力 = 15.0F + 1 = 16.0F
    FNMZ(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),     // 凡念梦兆
    FNMZC(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),    // 凡念梦兆·诸相自为
    HYBH(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),     // 黑渊白花
    HYBHC(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),    // 黑渊白花·创灭螺旋
    RMZ(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),      // 贯星枪·人马座
    YJZHLE(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),   // 永寂之赫勒尔
    ZJZHLE(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),   // 湛寂之赫勒尔
    ZXX(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),      // 星枪·主序星
    ZYDATJN(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),  // 子夜的阿塔吉娜
    ZYDHG(BH3ItemTiers.BH_33, 1, -2.8f, 0, 0),    // 子夜的黑光

    // ==================== 弓 (BH3WeaponBow) ====================
    // 基类: BH3WeaponBow -> 继承自 BowItem，无 tier/damage/speed 参数
    // 使用 BH3ItemTiers.BH_34，攻击力使用 tier 的基础值 15.0F
    WSDFH(BH3ItemTiers.BH_34, 0, 0, 0, 0),        // 往世的飞花
    WSDFHA(BH3ItemTiers.BH_34, 0, 0, 0, 0),       // 往世的飞花·爱之诗
    ZWZJ(BH3ItemTiers.BH_34, 0, 0, 0, 0),         // 真我之境·无瑕回归
    ZWZY(BH3ItemTiers.BH_34, 0, 0, 0, 0),         // 真我之钥
    ;

    private final Tier tier;
    private final int damage;
    private final float damageSpeed;
    private final int durability;
    private final int weaponLevel;

    WeaponAttributes(Tier tier, int damage, float damageSpeed, int durability, int weaponLevel) {
        this.tier = tier;
        this.damage = damage;
        this.damageSpeed = damageSpeed;
        this.durability = durability;
        this.weaponLevel = weaponLevel;
    }

    public Tier getTier() {
        return tier;
    }

    public int getDamage() {
        return damage;
    }

    public float getDamageSpeed() {
        return damageSpeed;
    }

    public int getDurability() {
        return durability;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }
}
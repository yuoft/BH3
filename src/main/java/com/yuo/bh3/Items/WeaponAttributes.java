package com.yuo.bh3.Items;

import net.minecraft.world.item.Tier;

public enum WeaponAttributes {
    // ==================== 镰刀 ====================
    BAX(BH3ItemTiers.BH_30, -1, -2.4f, 800, 0),     // 星刃·白矮星
    BAZF(BH3ItemTiers.BH_30, 0, -2.4f, 1000, 0),    // 彼岸之扉
    BAZFL(BH3ItemTiers.BH_30, 1, -2.3f, 1200, 0),   // 彼岸之扉·连理
    HSZS(BH3ItemTiers.BH_30, 0, -2.4f, 1000, 0),    // 恒霜之斯卡蒂
    MFXX(BH3ItemTiers.BH_30, -1, -2.3f, 800, 0),    // 冥府携香
    SYZ(BH3ItemTiers.BH_30, 1, -2.3f, 1100, 0),     // 裁星刃·双鱼座
    TSZS(BH3ItemTiers.BH_30, 0, -2.4f, 1000, 0),    // 天霜之斯卡蒂
    XSZJ(BH3ItemTiers.BH_30, 2, -2.3f, 1300, 0),    // 撷生之径·你我如一
    XSZY(BH3ItemTiers.BH_30, 0, -2.4f, 900, 0),     // 撷生之谣
    XYZM(BH3ItemTiers.BH_30, 1, -2.3f, 1100, 0),    // 血渊之眸
    XYZMY(BH3ItemTiers.BH_30, 3, -2.2f, 1400, 0),   // 血渊之眸·如一

    // ==================== 太刀 ====================
    AXYW(BH3ItemTiers.BH_31, 1, -2.5f, 1200, 0),    // 安息鸢尾
    CBZ(BH3ItemTiers.BH_31, 2, -2.4f, 1400, 0),     // 磁暴·斩
    CNZJ(BH3ItemTiers.BH_31, 2, -2.5f, 1300, 0),    // 澄凝之境·自性纯一
    CNZY(BH3ItemTiers.BH_31, 1, -2.6f, 1100, 0),    // 澄凝之钥
    CRY(BH3ItemTiers.BH_31, 1, -2.6f, 1000, 0),     // 妖刀·赤染樱
    DZQL(BH3ItemTiers.BH_31, 3, -2.4f, 1500, 0),    // 涤罪七雷·鸣
    DZQLM(BH3ItemTiers.BH_31, 5, -2.3f, 1800, 0),   // 涤罪七雷·鸣雷见
    FLD(BH3ItemTiers.BH_31, 1, -2.5f, 1200, 0),     // 仿灵刀·冰昙天
    HYD(BH3ItemTiers.BH_31, 2, -2.4f, 1300, 0),     // 魂妖刀·血樱寂灭
    TJZJ(BH3ItemTiers.BH_31, 4, -2.4f, 1600, 0),    // 天殛之境·裁决
    TJZY(BH3ItemTiers.BH_31, 2, -2.5f, 1300, 0),    // 天殛之钥
    YLD(BH3ItemTiers.BH_31, 1, -2.6f, 1100, 0),     // 御灵刀·寒狱冰天
    YYQW(BH3ItemTiers.BH_31, 1, -2.6f, 1000, 0),    // 夜宴蔷薇
    YYQWX(BH3ItemTiers.BH_31, 2, -2.5f, 1200, 0),   // 夜宴蔷薇·虚之冕
    ZCB(BH3ItemTiers.BH_31, 5, -2.3f, 1800, 0),     // 重磁暴·斩

    // ==================== 大剑 ====================
    JM(BH3ItemTiers.BH_32, 4, -2.8f, 2000, 0),      // 劫灭
    JMWJ(BH3ItemTiers.BH_32, 6, -2.7f, 2200, 0),    // 劫灭无烬
    NYJ(BH3ItemTiers.BH_32, 3, -2.9f, 1600, 0),     // 涅炎剑·史尔特尔
    SYJ(BH3ItemTiers.BH_32, 3, -2.9f, 1600, 0),     // 神陨剑·史尔特尔

    // ==================== 骑枪 ====================
    FNMZ(BH3ItemTiers.BH_33, 1, -2.7f, 1100, 0),    // 凡念梦兆
    FNMZC(BH3ItemTiers.BH_33, 3, -2.6f, 1400, 0),   // 凡念梦兆·诸相自为
    HYBH(BH3ItemTiers.BH_33, 2, -2.7f, 1300, 0),    // 黑渊白花
    HYBHC(BH3ItemTiers.BH_33, 4, -2.6f, 1600, 0),   // 黑渊白花·创灭螺旋
    RMZ(BH3ItemTiers.BH_33, 2, -2.7f, 1300, 0),     // 贯星枪·人马座
    YJZHLE(BH3ItemTiers.BH_33, 2, -2.7f, 1300, 0),  // 永寂之赫勒尔
    ZJZHLE(BH3ItemTiers.BH_33, 3, -2.6f, 1500, 0),  // 湛寂之赫勒尔
    ZXX(BH3ItemTiers.BH_33, 2, -2.7f, 1200, 0),     // 星枪·主序星
    ZYDATJN(BH3ItemTiers.BH_33, 3, -2.6f, 1400, 0), // 子夜的阿塔吉娜
    ZYDHG(BH3ItemTiers.BH_33, 2, -2.7f, 1200, 0),   // 子夜的黑光

    // ==================== 弓（原版属性暂不变） ====================
    // 弓不参与近战数值调整，保持原来的 0,0
    WSDFH(BH3ItemTiers.BH_34, 0, 0, 0, 0),
    WSDFHA(BH3ItemTiers.BH_34, 0, 0, 0, 0),
    ZWZJ(BH3ItemTiers.BH_34, 0, 0, 0, 0),
    ZWZY(BH3ItemTiers.BH_34, 0, 0, 0, 0),
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

    public Tier getTier() { return tier; }
    public int getDamage() { return damage; }
    public float getDamageSpeed() { return damageSpeed; }
    public int getDurability() { return durability; }
    public int getWeaponLevel() { return weaponLevel; }
}
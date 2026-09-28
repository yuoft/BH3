package com.yuo.bh3.Items;

import net.minecraft.world.item.Tier;

public enum WeaponAttributes {
    // ==================== 镰刀 ====================
    BAX(BH3ItemTiers.BH_30, -1, -2.4f, 8),     // 星刃·白矮星
    BAZF(BH3ItemTiers.BH_30, 0, -2.4f, 10),    // 彼岸之扉
    BAZFL(BH3ItemTiers.BH_300, 1, -2.3f, 12),   // 彼岸之扉·连理
    HSZS(BH3ItemTiers.BH_300, 0, -2.4f, 10),    // 恒霜之斯卡蒂
    MFXX(BH3ItemTiers.BH_30, -1, -2.3f, 8),    // 冥府携香
    SYZ(BH3ItemTiers.BH_30, 1, -2.3f, 11),     // 裁星刃·双鱼座
    TSZS(BH3ItemTiers.BH_30, 0, -2.4f, 10),    // 天霜之斯卡蒂
    XSZJ(BH3ItemTiers.BH_300, 2, -2.3f, 13),    // 撷生之径·你我如一
    XSZY(BH3ItemTiers.BH_30, 0, -2.4f, 9),     // 撷生之谣
    XYZM(BH3ItemTiers.BH_30, 1, -2.3f, 11),    // 血渊之眸
    XYZMY(BH3ItemTiers.BH_300, 3, -2.2f, 14),   // 血渊之眸·如一

    // ==================== 太刀 ====================
    AXYW(BH3ItemTiers.BH_31, 1, -2.5f, 12),    // 安息鸢尾
    CBZ(BH3ItemTiers.BH_31, 2, -2.4f, 14),     // 磁暴·斩
    CNZJ(BH3ItemTiers.BH_310, 2, -2.5f, 13),    // 澄凝之境·自性纯一
    CNZY(BH3ItemTiers.BH_31, 1, -2.6f, 11),    // 澄凝之钥
    CRY(BH3ItemTiers.BH_31, 1, -2.6f, 10),     // 妖刀·赤染樱
    DZQL(BH3ItemTiers.BH_31, 3, -2.4f, 15),    // 涤罪七雷·鸣
    DZQLM(BH3ItemTiers.BH_310, 5, -2.3f, 18),   // 涤罪七雷·鸣雷见
    FLD(BH3ItemTiers.BH_31, 1, -2.5f, 12),     // 仿灵刀·冰昙天
    HYD(BH3ItemTiers.BH_310, 2, -2.4f, 13),     // 魂妖刀·血樱寂灭
    TJZJ(BH3ItemTiers.BH_31, 4, -2.4f, 16),    // 天殛之境·裁决
    TJZY(BH3ItemTiers.BH_31, 2, -2.5f, 13),    // 天殛之钥
    YLD(BH3ItemTiers.BH_310, 1, -2.6f, 11),     // 御灵刀·寒狱冰天
    YYQW(BH3ItemTiers.BH_31, 1, -2.6f, 10),    // 夜宴蔷薇
    YYQWX(BH3ItemTiers.BH_310, 2, -2.5f, 12),   // 夜宴蔷薇·虚之冕
    ZCB(BH3ItemTiers.BH_310, 5, -2.3f, 18),     // 重磁暴·斩

    // ==================== 大剑 ====================
    JM(BH3ItemTiers.BH_32, 4, -2.8f, 20),      // 劫灭
    JMWJ(BH3ItemTiers.BH_320, 6, -2.7f, 22),    // 劫灭无烬
    NYJ(BH3ItemTiers.BH_32, 3, -2.9f, 16),     // 涅炎剑·史尔特尔
    SYJ(BH3ItemTiers.BH_320, 3, -2.9f, 16),     // 神陨剑·史尔特尔

    // ==================== 骑枪 ====================
    FNMZ(BH3ItemTiers.BH_33, 1, -2.7f, 11),    // 凡念梦兆
    FNMZC(BH3ItemTiers.BH_330, 3, -2.6f, 14),   // 凡念梦兆·诸相自为
    HYBH(BH3ItemTiers.BH_33, 2, -2.7f, 13),    // 黑渊白花
    HYBHC(BH3ItemTiers.BH_330, 4, -2.6f, 16),   // 黑渊白花·创灭螺旋
    RMZ(BH3ItemTiers.BH_330, 2, -2.7f, 13),     // 贯星枪·人马座
    YJZHLE(BH3ItemTiers.BH_33, 2, -2.7f, 13),  // 永寂之赫勒尔
    ZJZHLE(BH3ItemTiers.BH_330, 3, -2.6f, 15),  // 湛寂之赫勒尔
    ZXX(BH3ItemTiers.BH_33, 2, -2.7f, 12),     // 星枪·主序星
    ZYDATJN(BH3ItemTiers.BH_330, 3, -2.6f, 14), // 子夜的阿塔吉娜
    ZYDHG(BH3ItemTiers.BH_33, 2, -2.7f, 12),   // 子夜的黑光

    // ==================== 弓（原版属性暂不变） ====================
    // 弓不参与近战数值调整，保持原来的 0,0
    WSDFH(BH3ItemTiers.BH_34, 0, -2.1f, 0), //往世的飞花
    WSDFHA(BH3ItemTiers.BH_340, 1, -2.0f, 13), //往世的飞花·爱之诗
    ZWZJ(BH3ItemTiers.BH_340, 1, -2.0f, 14), //真我之境·无瑕回归
    ZWZY(BH3ItemTiers.BH_34, 0, -2.1f, 0), //真我之钥
    ;

    private final Tier tier;
    private final int damage;
    private final float damageSpeed;
    private final int durability;
    private final int weaponLevel;

    /**
     * 武器属性
     * @param tier 基础属性
     * @param damage 额外伤害
     * @param damageSpeed 攻速
     * @param durability 额外耐久 xx
     */
    WeaponAttributes(Tier tier, int damage, float damageSpeed, int durability) {
        this.tier = tier;
        this.damage = damage;
        this.damageSpeed = damageSpeed;
        this.durability = durability;
        this.weaponLevel = tier.getLevel();
    }

    public Tier getTier() { return tier; }
    public int getDamage() { return damage; }
    public float getDamageSpeed() { return damageSpeed; }
    public int getDurability() { return tier.getUses() + durability; }
    public int getWeaponLevel() { return weaponLevel; }
    public float getBowDamage(){
        return tier.getAttackDamageBonus() + damage;
    }
}
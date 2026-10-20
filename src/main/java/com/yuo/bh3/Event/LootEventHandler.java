package com.yuo.bh3.Event;

import com.yuo.bh3.BH3;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootEventHandler {

    /**
     * 包含武器模具掉落的战利品表集合
     */
    private static final Set<ResourceLocation> MOLD_STRUCTURES = Set.of(
            ModUtils.def("chests/simple_dungeon"),
            ModUtils.def("chests/abandoned_mineshaft"),
            ModUtils.def("chests/woodland_mansion"),
            ModUtils.def("chests/stronghold_corridor"),
            ModUtils.def("chests/nether_bridge"),
            ModUtils.def("chests/underwater_ruin_big"),
            ModUtils.def("chests/ancient_city"),
            ModUtils.def("chests/pillager_outpost"),
            ModUtils.def("chests/village/village_fletcher"),
            ModUtils.def("chests/jungle_temple")
    );

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        // 如果当前加载的箱子在列表中
        if (MOLD_STRUCTURES.contains(name)) {
            // 可以在不同结构配置不同的概率。
            // 这里为了统一，设定：出模具的概率大概在 15% 左右
            addRandomMoldLoot(event);
        }
    }

    /**
     * 向战利品表中添加随机模具池
     */
    private static void addRandomMoldLoot(LootTableLoadEvent event) {
        // 每种模具的权重
        int moldWeight = 10;

        // 空物品的权重（权重越高，不出模具的概率越大）
        // 5 种模具总权重 = 50，如果空权重也设为 50，则出模具的总概率为 50%
        // 这里设为 250，意味着单次抽取出模具的概率大概是 50 / (50 + 250) ≈ 16.7%
        int emptyWeight = 250;

        LootPool.Builder pool = LootPool.lootPool()
                .name("bh3_random_mold_pool")
                .setRolls(ConstantValue.exactly(1.0F))
                // 1. 加入“空”的权重，用来稀释掉率
                .add(EmptyLootItem.emptyItem().setWeight(emptyWeight))
                // 2. 加入 5 种模具，它们平分剩余的权重
                .add(LootItem.lootTableItem(BH3Items.weaponMoldSword.get()).setWeight(moldWeight))
                .add(LootItem.lootTableItem(BH3Items.weaponMoldBigsword.get()).setWeight(moldWeight))
                .add(LootItem.lootTableItem(BH3Items.weaponMoldLance.get()).setWeight(moldWeight))
                .add(LootItem.lootTableItem(BH3Items.weaponMoldSickle.get()).setWeight(moldWeight))
                .add(LootItem.lootTableItem(BH3Items.weaponMoldBow.get()).setWeight(moldWeight));

        event.getTable().addPool(pool.build());
    }

    /**
     * 向战利品表中添加物品
     * @param event      事件
     * @param item       要添加的物品
     * @param min        最小数量
     * @param max        最大数量
     * @param weight     权重（越大越容易出，原版普通物品一般是 1~10）
     */
    private static void addLoot(LootTableLoadEvent event, net.minecraft.world.item.Item item, int min, int max, int weight) {
        // 创建一个独立的池子（Pool），不与原版的池子混在一起
        LootPool.Builder pool = LootPool.lootPool()
                .name("bh3_mold_pool") // 池子名字
                .setRolls(ConstantValue.exactly(1.0F)) // 每次抽取 1 次
                .add(LootItem.lootTableItem(item) // 添加物品
                        .setWeight(weight) // 设置权重
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))) // 数量范围
                );

        // 将该池子加到战利品表中
        event.getTable().addPool(pool.build());
    }
}
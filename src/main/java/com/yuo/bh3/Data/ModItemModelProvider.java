package com.yuo.bh3.Data;

import com.yuo.bh3.Items.BH3BaseWeapon;
import com.yuo.bh3.Items.BH3Items;
import com.yuo.bh3.Items.BH3MusicDisc;
import com.yuo.bh3.Items.BH3WeaponBow;
import com.yuo.bh3.ModUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> entry : BH3Items.ITEMS.getEntries()) {
            Item item = entry.get();
            if (item instanceof BlockItem){
                continue;
            }

            if (item instanceof BH3BaseWeapon || item instanceof BH3WeaponBow){
                this.weaponItemModel(getRes(item));
            }else if (item instanceof BH3MusicDisc){
                this.discItemModel(getRes(item));
            }else this.basicItem(item);
        }

    }

    public ResourceLocation getRes(Item item){
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    }

    public void weaponItemModel(ResourceLocation item){
        this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ModUtils.fa("weapon2d/" + getId(item, "bh3_")));
    }

    public void discItemModel(ResourceLocation item){
        this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ModUtils.fa("item/music/" + getId(item, "disc_")));
    }

    private String getId(ResourceLocation item, String str){
        return (item.getPath().startsWith(str) ? item.getPath().substring(str.length()) : item.getPath());
    }
}

package com.yuo.bh3.Data;

import com.yuo.bh3.Client.WeaponTransformHelper;
import com.yuo.bh3.Client.WeaponTransformHelper.TransformParams;
import com.yuo.bh3.Items.*;
import com.yuo.bh3.ModUtils;
import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
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
                String type = "";
                if (item instanceof BH3WeaponSword) type = "sword";
                else if (item instanceof BH3WeaponBigSword) type = "bigsword";
                else if (item instanceof BH3WeaponLance) type = "lance";
                else if (item instanceof BH3WeaponSickle) type = "sickle";
                else type = "bow";
                this.weaponItemModel(getRes(item), type);
                this.weaponIconItemModel(getRes(item));
            }else if (item instanceof BH3MusicDisc){
                this.discItemModel(getRes(item));
            }else this.basicItem(item);
        }

    }

    public ResourceLocation getRes(Item item){
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    }

//    public void weaponItemModel(ResourceLocation item){
//        this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
//                .guiLight(GuiLight.FRONT).transforms()
//                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
//                .rotation(-30, -90, 0) //x：左右 y：前后 z：上下
//                .translation(4f, 0f, 3f)
//                .scale(1f)
//                .end()
//                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
//                .rotation(-30, -90, 0)
//                .translation(4f, 0.5f, 0.5f)
//                .scale(0.85f)
//                .end()
//                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
//                .rotation(-30, 90, 0)
//                .translation(4f, 0f, 3f)
//                .scale(1f)
//                .end()
//                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
//                .rotation(-30, 90, 0)
//                .translation(4f, 0.5f, 0.5f)
//                .scale(0.85f)
//                .end()
//                .end();
//    }

    public void weaponItemModel(ResourceLocation item, String type) {
        ItemModelBuilder builder = this.getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(GuiLight.FRONT);

        TransformParams p = WeaponTransformHelper.TRANSFORMS.getOrDefault(type, WeaponTransformHelper.TRANSFORMS.get("sword"));
        WeaponTransformHelper.applyTransforms(builder, p);
    }

    public void weaponIconItemModel(ResourceLocation item){
        this.getBuilder(item.toString() + "_icon").parent(new ModelFile.UncheckedModelFile("item/generated"))
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

package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public final class IRArmorMaterials {
    public static final Holder<ArmorMaterial> HAZMAT = register("hazmat", Util.make(new EnumMap<>(ArmorItem.Type.class), (p_323378_) -> {
        p_323378_.put(ArmorItem.Type.BOOTS, 2);
        p_323378_.put(ArmorItem.Type.LEGGINGS, 5);
        p_323378_.put(ArmorItem.Type.CHESTPLATE, 6);
        p_323378_.put(ArmorItem.Type.HELMET, 2);
        p_323378_.put(ArmorItem.Type.BODY, 5);
    }), 9, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(IRItems.RUBBER_SHEET.get()));

    public static final Holder<ArmorMaterial> NANO = register("nano", Util.make(new EnumMap<>(ArmorItem.Type.class), (protection) -> {
        protection.put(ArmorItem.Type.HELMET, 1);
        protection.put(ArmorItem.Type.CHESTPLATE, 1);
        protection.put(ArmorItem.Type.LEGGINGS, 1);
        protection.put(ArmorItem.Type.BOOTS, 1);
    }), 15, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, 0.0F, () -> Ingredient.EMPTY);


    private static Holder<ArmorMaterial> register(String p_323589_, EnumMap<ArmorItem.Type, Integer> p_323819_, int p_323636_, Holder<SoundEvent> p_323958_, float p_323937_, float p_323731_, Supplier<Ingredient> p_323970_) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.parse(p_323589_)));
        return register(p_323589_, p_323819_, p_323636_, p_323958_, p_323937_, p_323731_, p_323970_, list);
    }

    private static Holder<ArmorMaterial> register(String p_323865_, EnumMap<ArmorItem.Type, Integer> p_324599_, int p_324319_, Holder<SoundEvent> p_324145_, float p_323494_, float p_324549_, Supplier<Ingredient> p_323845_, List<ArmorMaterial.Layer> p_323990_) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);
        ArmorItem.Type[] armorItemTypes = ArmorItem.Type.values();

        for (ArmorItem.Type armoritem$type : armorItemTypes) {
            enummap.put(armoritem$type, p_324599_.get(armoritem$type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, ResourceLocation.parse(p_323865_), new ArmorMaterial(enummap, p_324319_, p_324145_, p_323845_, p_323990_, p_323494_, p_324549_));
    }
}

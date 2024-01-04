package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum IRArmorMaterials implements ArmorMaterial {
    HAZMAT("hazmat", 5, Util.make(new EnumMap<>(ArmorItem.Type.class), (protection) -> {
        protection.put(ArmorItem.Type.HELMET, 1);
        protection.put(ArmorItem.Type.CHESTPLATE, 1);
        protection.put(ArmorItem.Type.LEGGINGS, 1);
        protection.put(ArmorItem.Type.BOOTS, 1);
    }), 15, 0.0F, 0.0F, () -> Ingredient.of(IRItems.RUBBER_SHEET.get()));

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), p_266653_ -> {
        p_266653_.put(ArmorItem.Type.BOOTS, 13);
        p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
        p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
        p_266653_.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private IRArmorMaterials(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionForType, int enchantmentValue, float toughness, float knockbackResistance, Supplier<Ingredient> ingredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionForType;
        this.enchantmentValue = enchantmentValue;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(ingredientSupplier);
    }
    @Override
    public int getDurabilityForType(ArmorItem.Type p_266807_) {
        return HEALTH_FUNCTION_FOR_TYPE.get(p_266807_) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type p_267168_) {
        return protectionFunctionForType.get(p_267168_);
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return null;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}

package com.indref.industrial_reforged.content.items.armor;

import com.indref.industrial_reforged.registries.IRArmorMaterials;
import net.minecraft.world.item.ArmorItem;

public class HazmatSuiteItem extends ArmorItem {
    public HazmatSuiteItem(Type type, Properties properties) {
        super(IRArmorMaterials.HAZMAT, type, properties.stacksTo(1));
    }
}

package com.indref.industrial_reforged.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public final class IRDataMaps {
    public static final DataMapType<Item, CastingMoldValue> CASTING_MOLDS = DataMapType.builder(
            IndustrialReforged.rl("casting_molds"),
            Registries.ITEM,
            CastingMoldValue.CODEC
    ).synced(
            CastingMoldValue.CODEC,
            false
    ).build();
    public static final DataMapType<Item, TagKey<Item>> MOLD_INGREDIENTS = DataMapType.builder(
            IndustrialReforged.rl("mold_ingredients"),
            Registries.ITEM,
            TagKey.codec(Registries.ITEM)
    ).synced(
            TagKey.codec(Registries.ITEM),
            false
    ).build();
}

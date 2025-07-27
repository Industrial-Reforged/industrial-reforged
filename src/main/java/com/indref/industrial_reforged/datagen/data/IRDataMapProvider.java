package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IRDataMapProvider extends DataMapProvider {
    public IRDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        for (Map.Entry<DeferredItem<?>, CastingMoldValue> item : IRItems.MOLD_ITEMS.entrySet()) {
            CastingMoldValue value = item.getValue();
            moldItem(item.getKey().get(), value.capacity(), value.consumeCast());
        }

        moldIngredient(IRItems.STEEL_MOLD_INGOT, Tags.Items.INGOTS);
        moldIngredient(IRItems.STEEL_MOLD_PLATE, CTags.Items.PLATES);
        moldIngredient(IRItems.STEEL_MOLD_WIRE, CTags.Items.WIRES);
        moldIngredient(IRItems.STEEL_MOLD_ROD, Tags.Items.RODS);

        furnaceFuel(IRItems.COAL_DUST, 1600);
        furnaceFuel(IRItems.TREE_TAP, 200);
        furnaceFuel(IRBlocks.WOODEN_SCAFFOLDING, 500);
    }

    private void moldItem(Item moldItem, int capacity, boolean consumeCast) {
        builder(IRDataMaps.CASTING_MOLDS)
                .add(moldItem.getDefaultInstance().getItemHolder(), new CastingMoldValue(capacity, consumeCast), false);
    }

    private void moldIngredient(ItemLike moldItem, TagKey<Item> ingredient) {
        builder(IRDataMaps.MOLD_INGREDIENTS)
                .add(moldItem.asItem().getDefaultInstance().getItemHolder(), ingredient, false);
    }

    private void furnaceFuel(ItemLike fuelItem, int duration) {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(fuelItem.asItem().getDefaultInstance().getItemHolder(), new FurnaceFuel(duration), false);
    }
}
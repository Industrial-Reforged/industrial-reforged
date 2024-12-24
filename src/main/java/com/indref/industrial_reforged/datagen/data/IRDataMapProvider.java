package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IRDataMapProvider extends DataMapProvider {
    public IRDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        for (Map.Entry<DeferredItem<?>, CastingMoldValue> item : IRItems.MOLD_ITEMS.entrySet()) {
            CastingMoldValue value = item.getValue();
            moldItem(item.getKey().get(), value.capacity(), value.consumeCast());
        }
    }

    private void moldItem(Item moldItem, int capacity, boolean consumeCast) {
        builder(IRDataMaps.CASTING_MOLDS)
                .add(moldItem.getDefaultInstance().getItemHolder(), new CastingMoldValue(capacity, consumeCast), false);
    }
}
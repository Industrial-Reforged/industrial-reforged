package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.registries.IRLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;
import java.util.function.BiConsumer;

public record IRChestLootTableProvider(HolderLookup.Provider registries) implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        for (Map.Entry<ResourceKey<LootTable>, LootTable.Builder> entry : IRLootTables.LOOT_TABLES.entrySet()){
            biConsumer.accept(entry.getKey(), entry.getValue());
        }
    }
}

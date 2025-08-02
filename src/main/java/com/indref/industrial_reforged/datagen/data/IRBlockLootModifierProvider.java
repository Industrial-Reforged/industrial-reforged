package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class IRBlockLootModifierProvider extends GlobalLootModifierProvider {
    public IRBlockLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, IndustrialReforged.MODID);
    }

    @Override
    protected void start() {
        add("dungeon_chest_modifier", new AddTableLootModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(1.0f).build()
        }, IRLootTables.DUNGEON));
    }
}

package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.datagen.assets.IRBlockStateProvider;
import com.indref.industrial_reforged.datagen.assets.IREnUSLangProvider;
import com.indref.industrial_reforged.datagen.assets.IRItemModelProvider;
import com.indref.industrial_reforged.datagen.data.IRBlockLootTableProvider;
import com.indref.industrial_reforged.datagen.data.IRRecipeProvider;
import com.indref.industrial_reforged.datagen.data.IRTagsProvider;
import com.indref.industrial_reforged.datagen.data.IRWorldGenProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGatherer {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new IRItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new IRBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new IREnUSLangProvider(packOutput));

        IRTagsProvider.createTagProviders(generator, packOutput, lookupProvider, existingFileHelper, event.includeServer());
        generator.addProvider(event.includeServer(), new IRRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new IRWorldGenProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(IRBlockLootTableProvider::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
    }
}

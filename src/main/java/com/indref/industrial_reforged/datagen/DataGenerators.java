package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new IRBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new IRItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new IRRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new IRWorldGenProvider(packOutput, lookupProvider));
        IRTagsProvider.BlocksProvider blocks = new IRTagsProvider.BlocksProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeClient(), blocks);
        generator.addProvider(event.includeClient(), new IRTagsProvider.ItemsProvider(packOutput, lookupProvider, blocks.contentsGetter()));
    }
}

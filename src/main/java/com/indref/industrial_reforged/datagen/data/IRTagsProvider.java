package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IRTagsProvider {
    public static void createTagProviders(DataGenerator generator, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper, boolean isServer) {
        BlocksProvider provider = new BlocksProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(isServer, provider);
        generator.addProvider(isServer, new ItemsProvider(packOutput, lookupProvider, provider.contentsGetter()));
        generator.addProvider(isServer, new FluidsProvider(packOutput, lookupProvider, existingFileHelper));
    }

    protected static class ItemsProvider extends ItemTagsProvider {
        public ItemsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
            super(output, lookupProvider, blockTags);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            tag(IRTags.Items.TOOL,
                    IRItems.HAMMER,
                    IRItems.WRENCH,
                    IRItems.TREE_TAP,
                    IRItems.SCANNER,
                    IRItems.THERMOMETER
            );
            tag(IRTags.Items.RUBBER_LOGS,
                    IRBlocks.RUBBER_TREE_LOG,
                    IRBlocks.STRIPPED_RUBBER_TREE_LOG,
                    IRBlocks.RUBBER_TREE_WOOD,
                    IRBlocks.STRIPPED_RUBBER_TREE_WOOD
            );

            plates();
            wires();
            ingots();
            rods();
            storageBlocks();

            tag(ItemTags.DYEABLE, IRItems.TOOLBOX.get());
        }

        private void ingots() {
            tag(Tags.Items.INGOTS,
                    CTags.Items.ALUMINUM_INGOT,
                    CTags.Items.CHROMIUM_INGOT,
                    CTags.Items.IRIDIUM_INGOT,
                    CTags.Items.LEAD_INGOT,
                    CTags.Items.NICKEL_INGOT,
                    CTags.Items.STEEL_INGOT,
                    CTags.Items.TIN_INGOT,
                    CTags.Items.TITANIUM_INGOT,
                    CTags.Items.URANIUM_INGOT
            );
            tag(CTags.Items.ALUMINUM_INGOT, IRItems.ALUMINUM_INGOT);
            tag(CTags.Items.CHROMIUM_INGOT, IRItems.CHROMIUM_INGOT);
            tag(CTags.Items.IRIDIUM_INGOT, IRItems.IRIDIUM_INGOT);
            tag(CTags.Items.LEAD_INGOT, IRItems.LEAD_INGOT);
            tag(CTags.Items.NICKEL_INGOT, IRItems.NICKEL_INGOT);
            tag(CTags.Items.STEEL_INGOT, IRItems.STEEL_INGOT);
            tag(CTags.Items.TIN_INGOT, IRItems.TIN_INGOT);
            tag(CTags.Items.TITANIUM_INGOT, IRItems.TITANIUM_INGOT);
            tag(CTags.Items.URANIUM_INGOT, IRItems.URANIUM_INGOT);
        }

        private void plates() {
            tag(CTags.Items.PLATES,
                    CTags.Items.RUBBER_SHEET
            );
            tag(CTags.Items.RUBBER_SHEET, IRItems.RUBBER_SHEET);
            tag(CTags.Items.IRON_PLATE, IRItems.IRON_PLATE);
            tag(CTags.Items.COPPER_PLATE, IRItems.COPPER_PLATE);
            tag(CTags.Items.TIN_PLATE, IRItems.TIN_PLATE);
            tag(CTags.Items.STEEL_PLATE, IRItems.STEEL_PLATE);
        }

        private void wires() {
            tag(CTags.Items.WIRES,
                    CTags.Items.TIN_WIRE,
                    CTags.Items.COPPER_WIRE,
                    CTags.Items.GOLD_WIRE,
                    CTags.Items.STEEL_WIRE
            );

            tag(CTags.Items.TIN_WIRE, IRItems.TIN_WIRE);
            tag(CTags.Items.COPPER_WIRE, IRItems.COPPER_WIRE);
            tag(CTags.Items.GOLD_WIRE, IRItems.GOLD_WIRE);
            tag(CTags.Items.STEEL_WIRE, IRItems.STEEL_WIRE);
        }

        private void rods() {
            tag(Tags.Items.RODS, IRItems.IRON_ROD, IRItems.STEEL_ROD);
            tag(CTags.Items.IRON_ROD, IRItems.IRON_ROD);
            tag(CTags.Items.STEEL_ROD, IRItems.STEEL_ROD);
        }

        private void storageBlocks() {
            tag(Tags.Items.STORAGE_BLOCKS,
                    IRBlocks.TIN_BLOCK,
                    IRBlocks.NICKEL_BLOCK,
                    IRBlocks.STEEL_BLOCK,
                    IRBlocks.LEAD_BLOCK,
                    IRBlocks.URANIUM_BLOCK,
                    IRBlocks.IRIDIUM_BLOCK,
                    IRBlocks.TITANIUM_BLOCK,
                    IRBlocks.ALUMINUM_BLOCK
            );

            tag(CTags.Items.STORAGE_BLOCKS_TIN, IRBlocks.TIN_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_NICKEL, IRBlocks.NICKEL_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_STEEL, IRBlocks.LEAD_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_LEAD, IRBlocks.TIN_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_URANIUM, IRBlocks.URANIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_IRIDIUM, IRBlocks.IRIDIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_TITANIUM, IRBlocks.TITANIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_ALUMINUM, IRBlocks.ALUMINUM_BLOCK);
        }

        private void tag(TagKey<Item> itemTagKey, ItemLike... items) {
            IntrinsicTagAppender<Item> tag = tag(itemTagKey);
            for (ItemLike item : items) {
                tag.add(item.asItem());
            }
        }

        @SafeVarargs
        private void tag(TagKey<Item> itemTagKey, TagKey<Item>... items) {
            IntrinsicTagAppender<Item> tag = tag(itemTagKey);
            for (TagKey<Item> item : items) {
                tag.addTag(item);
            }
        }
    }

    protected static class BlocksProvider extends BlockTagsProvider {
        public BlocksProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, IndustrialReforged.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.FENCES).add(IRBlocks.IRON_FENCE.get());
        }
    }

    public static class FluidsProvider extends FluidTagsProvider {
        public FluidsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, IndustrialReforged.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
        }
    }
}

package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRFluids;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import com.indref.industrial_reforged.tags.ModdedTags;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
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
            tag(Tags.Items.TOOLS,
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
            rawMaterials();
            ores();

            tag(ItemTags.DYEABLE, IRItems.TOOLBOX.get());

            tag(ItemTags.PLANKS, IRBlocks.RUBBER_TREE_PLANKS.get());
            tag(ItemTags.WOODEN_FENCES, IRBlocks.RUBBER_TREE_FENCE.get());
            tag(ItemTags.FENCE_GATES, IRBlocks.RUBBER_TREE_FENCE_GATE.get());
            tag(ItemTags.WOODEN_BUTTONS, IRBlocks.RUBBER_TREE_BUTTON.get());
            tag(ItemTags.WOODEN_DOORS, IRBlocks.RUBBER_TREE_DOOR.get());
            tag(ItemTags.WOODEN_PRESSURE_PLATES, IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get());
            tag(ItemTags.WOODEN_SLABS, IRBlocks.RUBBER_TREE_SLAB.get());
            tag(ItemTags.WOODEN_TRAPDOORS, IRBlocks.RUBBER_TREE_TRAPDOOR.get());
            tag(ItemTags.LEAVES, IRBlocks.RUBBER_TREE_LEAVES.get());
            tag(ItemTags.LOGS_THAT_BURN,
                    IRBlocks.RUBBER_TREE_LOG.get(),
                    IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(),
                    IRBlocks.RUBBER_TREE_WOOD.get(),
                    IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get(),
                    IRBlocks.RUBBER_TREE_RESIN_HOLE.get()
            );
            tag(Tags.Items.BRICKS, IRBlocks.TERRACOTTA_BRICKS.get(), IRBlocks.BLAST_FURNACE_BRICKS.get(), IRBlocks.REFRACTORY_BRICK.get());
        }

        private void ores() {
            tag(Tags.Items.ORES,
                    IRBlocks.BAUXITE_ORE,
                    IRBlocks.CHROMIUM_ORE,
                    IRBlocks.IRIDIUM_ORE,
                    IRBlocks.LEAD_ORE,
                    IRBlocks.NICKEL_ORE,
                    IRBlocks.TIN_ORE,
                    IRBlocks.URANIUM_ORE
            );

            tag(CTags.Items.ORES_BAUXITE, IRBlocks.BAUXITE_ORE);
            tag(CTags.Items.ORES_CHROMIUM, IRBlocks.CHROMIUM_ORE);
            tag(CTags.Items.ORES_IRIDIUM, IRBlocks.IRIDIUM_ORE);
            tag(CTags.Items.ORES_LEAD, IRBlocks.LEAD_ORE);
            tag(CTags.Items.ORES_NICKEL, IRBlocks.NICKEL_ORE);
            tag(CTags.Items.ORES_TIN, IRBlocks.TIN_ORE);
            tag(CTags.Items.ORES_URANIUM, IRBlocks.URANIUM_ORE);
        }

        private void rawMaterials() {
            tag(Tags.Items.RAW_MATERIALS,
                    IRItems.RAW_BAUXITE,
                    IRItems.RAW_CHROMIUM,
                    IRItems.RAW_IRIDIUM,
                    IRItems.RAW_LEAD,
                    IRItems.RAW_NICKEL,
                    IRItems.RAW_TIN,
                    IRItems.RAW_URANIUM
            );
            tag(CTags.Items.RAW_MATERIALS_BAUXITE, IRItems.RAW_BAUXITE);
            tag(CTags.Items.RAW_MATERIALS_CHROMIUM, IRItems.RAW_CHROMIUM);
            tag(CTags.Items.RAW_MATERIALS_IRIDIUM, IRItems.RAW_IRIDIUM);
            tag(CTags.Items.RAW_MATERIALS_LEAD, IRItems.RAW_LEAD);
            tag(CTags.Items.RAW_MATERIALS_NICKEL, IRItems.RAW_NICKEL);
            tag(CTags.Items.RAW_MATERIALS_TIN, IRItems.RAW_TIN);
            tag(CTags.Items.RAW_MATERIALS_URANIUM, IRItems.RAW_URANIUM);
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
                    CTags.Items.RUBBER_SHEET,
                    CTags.Items.IRON_PLATE,
                    CTags.Items.COPPER_PLATE,
                    CTags.Items.TIN_PLATE,
                    CTags.Items.STEEL_PLATE,
                    CTags.Items.WOODEN_PLATE,
                    CTags.Items.CARBON_PLATE
            );

            tag(CTags.Items.RUBBER_SHEET, IRItems.RUBBER_SHEET);
            tag(CTags.Items.WOODEN_PLATE, IRItems.WOODEN_PLATE);
            tag(CTags.Items.CARBON_PLATE, IRItems.CARBON_PLATE);
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
                    CTags.Items.STEEL_WIRE,
                    Tags.Items.STRINGS
            );

            tag(CTags.Items.TIN_WIRE, IRItems.TIN_WIRE);
            tag(CTags.Items.COPPER_WIRE, IRItems.COPPER_WIRE);
            tag(CTags.Items.GOLD_WIRE, IRItems.GOLD_WIRE);
            tag(CTags.Items.STEEL_WIRE, IRItems.STEEL_WIRE);
            tag(Tags.Items.STRINGS, Items.STRING);
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
                    IRBlocks.ALUMINUM_BLOCK,
                    IRBlocks.RAW_BAUXITE_BLOCK,
                    IRBlocks.RAW_CHROMIUM_BLOCK,
                    IRBlocks.RAW_IRIDIUM_BLOCK,
                    IRBlocks.RAW_LEAD_BLOCK,
                    IRBlocks.RAW_NICKEL_BLOCK,
                    IRBlocks.RAW_TIN_BLOCK,
                    IRBlocks.RAW_URANIUM_BLOCK
            );

            tag(CTags.Items.STORAGE_BLOCKS_TIN, IRBlocks.TIN_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_NICKEL, IRBlocks.NICKEL_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_STEEL, IRBlocks.STEEL_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_LEAD, IRBlocks.LEAD_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_URANIUM, IRBlocks.URANIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_IRIDIUM, IRBlocks.IRIDIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_TITANIUM, IRBlocks.TITANIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_ALUMINUM, IRBlocks.ALUMINUM_BLOCK);

            tag(CTags.Items.STORAGE_BLOCKS_RAW_TIN, IRBlocks.RAW_TIN_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_RAW_NICKEL, IRBlocks.RAW_NICKEL_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_RAW_LEAD, IRBlocks.RAW_LEAD_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_RAW_URANIUM, IRBlocks.RAW_URANIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_RAW_IRIDIUM, IRBlocks.RAW_IRIDIUM_BLOCK);
            tag(CTags.Items.STORAGE_BLOCKS_RAW_BAUXITE, IRBlocks.RAW_BAUXITE_BLOCK);
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
            tag(BlockTags.FENCES, IRBlocks.IRON_FENCE.get());

            // Rubber trees
            tag(BlockTags.PLANKS, IRBlocks.RUBBER_TREE_PLANKS.get());
            tag(BlockTags.WOODEN_FENCES, IRBlocks.RUBBER_TREE_FENCE.get());
            tag(BlockTags.FENCE_GATES, IRBlocks.RUBBER_TREE_FENCE_GATE.get());
            tag(BlockTags.WOODEN_BUTTONS, IRBlocks.RUBBER_TREE_BUTTON.get());
            tag(BlockTags.WOODEN_DOORS, IRBlocks.RUBBER_TREE_DOOR.get());
            tag(BlockTags.WOODEN_PRESSURE_PLATES, IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get());
            tag(BlockTags.WOODEN_SLABS, IRBlocks.RUBBER_TREE_SLAB.get());
            tag(BlockTags.WOODEN_TRAPDOORS, IRBlocks.RUBBER_TREE_TRAPDOOR.get());
            tag(BlockTags.LEAVES, IRBlocks.RUBBER_TREE_LEAVES.get());
            tag(BlockTags.LOGS_THAT_BURN,
                    IRBlocks.RUBBER_TREE_LOG.get(),
                    IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(),
                    IRBlocks.RUBBER_TREE_WOOD.get(),
                    IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get(),
                    IRBlocks.RUBBER_TREE_RESIN_HOLE.get()
            );

            for (DeferredBlock<?> block : IRBlocks.AXE_MINEABLE) {
                tag(BlockTags.MINEABLE_WITH_AXE, block.get());
            }

            for (DeferredBlock<?> block : IRBlocks.PICKAXE_MINEABLE) {
                tag(BlockTags.MINEABLE_WITH_PICKAXE, block.get());
            }

            tag(ModdedTags.Blocks.SUPPORTS_FACADE,
                    IRBlocks.TIN_CABLE.get(),
                    IRBlocks.COPPER_CABLE.get(),
                    IRBlocks.GOLD_CABLE.get(),
                    IRBlocks.STEEL_CABLE.get()
            );

            tag(BlockTags.NEEDS_STONE_TOOL,
                    IRBlocks.BAUXITE_ORE.get(),
                    IRBlocks.TIN_BLOCK.get(),
                    IRBlocks.LEAD_ORE.get(),
                    IRBlocks.DEEPSLATE_BAUXITE_ORE.get(),
                    IRBlocks.DEEPSLATE_TIN_ORE.get(),
                    IRBlocks.DEEPSLATE_LEAD_ORE.get(),
                    IRBlocks.NICKEL_BLOCK.get(),
                    IRBlocks.ALUMINUM_BLOCK.get(),
                    IRBlocks.TIN_BLOCK.get(),
                    IRBlocks.LEAD_BLOCK.get(),
                    IRBlocks.NICKEL_BLOCK.get(),
                    IRBlocks.RAW_BAUXITE_BLOCK.get(),
                    IRBlocks.RAW_TIN_BLOCK.get(),
                    IRBlocks.RAW_LEAD_BLOCK.get(),
                    IRBlocks.RAW_NICKEL_BLOCK.get()
            );

            tag(BlockTags.NEEDS_STONE_TOOL,
                    IRBlocks.BASIC_GENERATOR.get(),
                    IRBlocks.CENTRIFUGE.get()
            );

            tag(BlockTags.NEEDS_IRON_TOOL,
                    IRBlocks.CHROMIUM_ORE.get(),
                    IRBlocks.URANIUM_ORE.get(),
                    IRBlocks.CHROMIUM_BLOCK.get(),
                    IRBlocks.URANIUM_BLOCK.get(),
                    IRBlocks.DEEPSLATE_CHROMIUM_ORE.get(),
                    IRBlocks.DEEPSLATE_URANIUM_ORE.get(),
                    IRBlocks.CHROMIUM_BLOCK.get(),
                    IRBlocks.URANIUM_BLOCK.get(),
                    IRBlocks.STEEL_BLOCK.get(),
                    IRBlocks.RAW_CHROMIUM_BLOCK.get(),
                    IRBlocks.RAW_URANIUM_BLOCK.get()
            );

            tag(BlockTags.NEEDS_DIAMOND_TOOL,
                    IRBlocks.IRIDIUM_ORE.get(),
                    IRBlocks.DEEPSLATE_IRIDIUM_ORE.get(),
                    IRBlocks.TITANIUM_BLOCK.get(),
                    IRBlocks.IRIDIUM_BLOCK.get(),
                    IRBlocks.RAW_IRIDIUM_BLOCK.get()
            );

            tag(Tags.Blocks.STONES, IRBlocks.REFRACTORY_STONE.get());

            tag(BlockTags.CLIMBABLE, IRBlocks.WOODEN_SCAFFOLDING.get());
            tag(BlockTags.FALL_DAMAGE_RESETTING, IRBlocks.WOODEN_SCAFFOLDING.get());
        }

        private void tag(TagKey<Block> itemTagKey, Block... blocks) {
            IntrinsicTagAppender<Block> tag = tag(itemTagKey);
            for (Block block : blocks) {
                tag.add(block);
            }
        }

        @SafeVarargs
        private void tag(TagKey<Block> itemTagKey, TagKey<Block>... blocks) {
            IntrinsicTagAppender<Block> tag = tag(itemTagKey);
            for (TagKey<Block> block : blocks) {
                tag.addTag(block);
            }
        }
    }

    public static class FluidsProvider extends FluidTagsProvider {
        public FluidsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, IndustrialReforged.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(CTags.Fluids.OIL, IRFluids.OIL);
        }

        private void tag(TagKey<Fluid> fluidTagKey, PDLFluid... fluids) {
            IntrinsicTagAppender<Fluid> tag = tag(fluidTagKey);
            for (PDLFluid fluid : fluids) {
                tag.add(fluid.getStillFluid());
            }
        }

        private void tag(TagKey<Fluid> fluidTagKey, Fluid... fluids) {
            IntrinsicTagAppender<Fluid> tag = tag(fluidTagKey);
            for (Fluid fluid : fluids) {
                tag.add(fluid);
            }
        }

        @SafeVarargs
        private void tag(TagKey<Fluid> fluidTagKey, TagKey<Fluid>... fluids) {
            IntrinsicTagAppender<Fluid> tag = tag(fluidTagKey);
            for (TagKey<Fluid> fluid : fluids) {
                tag.addTag(fluid);
            }
        }
    }
}

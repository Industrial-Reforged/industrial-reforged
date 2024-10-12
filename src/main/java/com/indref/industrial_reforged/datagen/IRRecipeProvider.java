package com.indref.industrial_reforged.datagen;

import com.google.common.collect.ImmutableList;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IRRecipeProvider extends RecipeProvider {
    private static final ImmutableList<ItemLike> TIN_SMELTABLES = ImmutableList.of(IRItems.RAW_TIN, IRBlocks.TIN_ORE, IRBlocks.DEEPSLATE_TIN_ORE);
    private static final ImmutableList<ItemLike> NICKEL_SMELTABLES = ImmutableList.of(IRItems.RAW_NICKEL, IRBlocks.NICKEL_ORE, IRBlocks.DEEPSLATE_NICKEL_ORE);
    private static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(IRItems.RAW_LEAD, IRBlocks.LEAD_ORE, IRBlocks.DEEPSLATE_LEAD_ORE);
    private static final ImmutableList<ItemLike> URANIUM_SMELTABLES = ImmutableList.of(IRItems.RAW_URANIUM, IRBlocks.URANIUM_ORE, IRBlocks.DEEPSLATE_URANIUM_ORE);
    private static final ImmutableList<ItemLike> CHROMIUM_SMELTABLES = ImmutableList.of(IRItems.RAW_CHROMIUM, IRBlocks.CHROMIUM_ORE, IRBlocks.DEEPSLATE_CHROMIUM_ORE);

    private RecipeOutput output;

    public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookUpProvider) {
        super(output, lookUpProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        this.output = output;
        craftingRecipes();
        smeltingRecipes();
    }

    private void craftingRecipes() {
        rubberWoodRecipes();
        toolRecipes();
        cableRecipes();

        // Compacting recipes
        rawOreToBlockRecipes();
        ingotToBlockRecipes();
    }

    private void smeltingRecipes() {
        ingotSmeltingRecipes();
    }

    private void toolRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.HAMMER.get())
                .pattern(" # ")
                .pattern(" S#")
                .pattern("S  ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.WRENCH.get())
                .pattern(" # ")
                .pattern(" ##")
                .pattern("#  ")
                .define('#', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.TREE_TAP.get())
                .pattern(" S ")
                .pattern("###")
                .pattern("#  ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.THERMOMETER.get())
                .pattern(" ##")
                .pattern("#R#")
                .pattern("R# ")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('#', Tags.Items.GLASS_PANES)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);

        // TODO: Other tool recipes
    }

    private void rawOreToBlockRecipes() {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_BAUXITE.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_BAUXITE_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_CHROMIUM.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_CHROMIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_IRIDIUM.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_IRIDIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_LEAD.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_LEAD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_NICKEL.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_NICKEL_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_TIN.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_TIN_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_URANIUM.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_URANIUM_BLOCK.get());
    }

    private void ingotToBlockRecipes() {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.STEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.STEEL_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.ALUMINUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.ALUMINUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.TITANIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.TITANIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.CHROMIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.CHROMIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.IRIDIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.IRIDIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.LEAD_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.LEAD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.NICKEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.NICKEL_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.TIN_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.TIN_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.URANIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.URANIUM_BLOCK.get());
    }

    private void cableRecipes() {
        cable(CTags.Items.TIN_WIRE, IRBlocks.TIN_CABLE.get());
        cable(CTags.Items.COPPER_WIRE, IRBlocks.COPPER_CABLE.get());
        cable(CTags.Items.GOLD_WIRE, IRBlocks.GOLD_CABLE.get());
        cable(CTags.Items.STEEL_WIRE, IRBlocks.STEEL_CABLE.get());
    }

    private void rubberWoodRecipes() {
        planksFromLog(output, IRBlocks.RUBBER_TREE_PLANKS, IRTags.Items.RUBBER_LOGS, 4);
        slab(this.output, RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_SLAB, IRBlocks.RUBBER_TREE_PLANKS);
        pressurePlate(output, IRBlocks.RUBBER_TREE_PRESSURE_PLATE, IRBlocks.RUBBER_TREE_PLANKS);
        woodFromLogs(output, IRBlocks.RUBBER_TREE_WOOD, IRBlocks.RUBBER_TREE_LOG);
        woodFromLogs(output, IRBlocks.STRIPPED_RUBBER_TREE_WOOD, IRBlocks.STRIPPED_RUBBER_TREE_WOOD);
        stairBuilder(IRBlocks.RUBBER_TREE_STAIRS, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        buttonBuilder(IRBlocks.RUBBER_TREE_BUTTON, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        fenceBuilder(IRBlocks.RUBBER_TREE_FENCE, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        fenceGateBuilder(IRBlocks.RUBBER_TREE_FENCE_GATE, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        trapdoorBuilder(IRBlocks.RUBBER_TREE_TRAPDOOR, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        doorBuilder(IRBlocks.RUBBER_TREE_DOOR, Ingredient.of(IRBlocks.RUBBER_TREE_DOOR))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
    }

    private void ingotSmeltingRecipes() {
        oreSmelting(output, TIN_SMELTABLES, RecipeCategory.MISC, IRItems.TIN_INGOT, 0.7f, 200, "tin_ingot");
        oreSmelting(output, NICKEL_SMELTABLES, RecipeCategory.MISC, IRItems.NICKEL_INGOT, 0.7f, 200, "nickel_ingot");
        oreSmelting(output, LEAD_SMELTABLES, RecipeCategory.MISC, IRItems.LEAD_INGOT, 0.7f, 200, "lead_ingot");
        oreSmelting(output, CHROMIUM_SMELTABLES, RecipeCategory.MISC, IRItems.CHROMIUM_INGOT, 1f, 200, "chromium_ingot");
        oreSmelting(output, URANIUM_SMELTABLES, RecipeCategory.MISC, IRItems.URANIUM_INGOT, 1f, 200, "uranium_ingot");

        oreBlasting(output, TIN_SMELTABLES, RecipeCategory.MISC, IRItems.TIN_INGOT, 0.7f, 100, "tin_ingot");
        oreBlasting(output, NICKEL_SMELTABLES, RecipeCategory.MISC, IRItems.NICKEL_INGOT, 0.7f, 100, "nickel_ingot");
        oreBlasting(output, LEAD_SMELTABLES, RecipeCategory.MISC, IRItems.LEAD_INGOT, 0.7f, 100, "lead_ingot");
        oreBlasting(output, CHROMIUM_SMELTABLES, RecipeCategory.MISC, IRItems.CHROMIUM_INGOT, 1f, 100, "chromium_ingot");
        oreBlasting(output, URANIUM_SMELTABLES, RecipeCategory.MISC, IRItems.URANIUM_INGOT, 1f, 100, "uranium_ingot");
    }

    // -- HELPER METHODS --
    private void cable(TagKey<Item> wireMaterial, ItemLike cable) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, cable)
                .requires(CTags.Items.RUBBER_SHEET)
                .requires(wireMaterial)
                .unlockedBy("has_rubber_sheet", has(CTags.Items.RUBBER_SHEET))
                .save(output);
    }

    protected static void nineBlockStorageRecipes(
            RecipeOutput recipeOutput, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed
    ) {
        nineBlockStorageRecipes(
                recipeOutput, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), null, getSimpleRecipeName(unpacked), null
        );
    }

    protected static void nineBlockStorageRecipes(
            RecipeOutput recipeOutput,
            RecipeCategory unpackedCategory,
            ItemLike unpacked,
            RecipeCategory packedCategory,
            ItemLike packed,
            String packedName,
            @Nullable String packedGroup,
            String unpackedName,
            @Nullable String unpackedGroup
    ) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9)
                .requires(packed)
                .group(unpackedGroup)
                .unlockedBy(getHasName(packed), has(packed))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('#', unpacked)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group(packedGroup)
                .unlockedBy(getHasName(unpacked), has(unpacked))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, packedName));
    }
}

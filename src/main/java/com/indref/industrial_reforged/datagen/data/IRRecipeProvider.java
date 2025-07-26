package com.indref.industrial_reforged.datagen.data;

import com.google.common.collect.ImmutableList;
import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.fluids.MoltenMetalFluid;
import com.indref.industrial_reforged.content.recipes.*;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRFluids;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IRRecipeProvider extends RecipeProvider {
    private static final ImmutableList<ItemLike> TIN_SMELTABLES = ImmutableList.of(IRItems.RAW_TIN, IRBlocks.TIN_ORE, IRBlocks.DEEPSLATE_TIN_ORE);
    private static final ImmutableList<ItemLike> NICKEL_SMELTABLES = ImmutableList.of(IRItems.RAW_NICKEL, IRBlocks.NICKEL_ORE, IRBlocks.DEEPSLATE_NICKEL_ORE);
    private static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(IRItems.RAW_LEAD, IRBlocks.LEAD_ORE, IRBlocks.DEEPSLATE_LEAD_ORE);
    private static final ImmutableList<ItemLike> URANIUM_SMELTABLES = ImmutableList.of(IRItems.RAW_URANIUM, IRBlocks.URANIUM_ORE, IRBlocks.DEEPSLATE_URANIUM_ORE);
    private static final ImmutableList<ItemLike> CHROMIUM_SMELTABLES = ImmutableList.of(IRItems.RAW_CHROMIUM, IRBlocks.CHROMIUM_ORE, IRBlocks.DEEPSLATE_CHROMIUM_ORE);

    private RecipeOutput output;

    public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
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
        machineCraftingRecipes();
        multiblockRecipes();
        armorRecipes();

        storageItemRecipes();

        upgradeRecipes();
        miscRecipes();

        blastFurnaceRecipes();
        crucibleSmeltingRecipes();
        castingRecipes();
        moldCastingRecipes();
        centrifugeRecipes();

        // Compacting recipes
        rawOreToBlockRecipes();
        ingotToBlockRecipes();
    }

    private void multiblockRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.CERAMIC_CASTING_BASIN.get())
                .pattern("# #")
                .pattern("###")
                .define('#', IRBlocks.TERRACOTTA_BRICKS.get())
                .unlockedBy("has_terracotta_brick", has(IRBlocks.TERRACOTTA_BRICKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BLAST_FURNACE_CASTING_BASIN.get())
                .pattern("# #")
                .pattern("###")
                .define('#', IRBlocks.BLAST_FURNACE_BRICKS.get())
                .unlockedBy("has_blast_furnace_brick", has(IRBlocks.BLAST_FURNACE_BRICKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BLAST_FURNACE_FAUCET.get())
                .pattern("# #")
                .pattern(" # ")
                .define('#', IRBlocks.BLAST_FURNACE_BRICKS.get())
                .unlockedBy("has_blast_furnace_brick", has(IRBlocks.BLAST_FURNACE_BRICKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BLAST_FURNACE_HATCH.get(), 2)
                .pattern("#P#")
                .pattern("PFP")
                .pattern("#P#")
                .define('#', IRBlocks.BLAST_FURNACE_BRICKS.get())
                .define('P', CTags.Items.IRON_PLATE)
                .define('F', Blocks.BLAST_FURNACE)
                .unlockedBy("has_iron_plate", has(CTags.Items.IRON_PLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.SMALL_FIREBOX_HATCH.get(), 2)
                .pattern("#P#")
                .pattern("PFP")
                .pattern("#P#")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('P', CTags.Items.IRON_PLATE)
                .define('F', Blocks.FURNACE)
                .unlockedBy("has_iron_plate", has(CTags.Items.IRON_PLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.IRON_FENCE.get(), 3)
                .pattern("#R#")
                .pattern("#R#")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('R', CTags.Items.IRON_ROD)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
    }

    private void storageItemRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_BATTERY.get())
                .pattern(" W ")
                .pattern("#R#")
                .pattern("#R#")
                .define('#', CTags.Items.LEAD_INGOT)
                .define('W', IRItems.TIN_WIRE.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_tin_wire", has(IRItems.TIN_WIRE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.FLUID_CELL.get(), 6)
                .pattern(" # ")
                .pattern("#P#")
                .pattern(" # ")
                .define('#', CTags.Items.TIN_INGOT)
                .define('P', Tags.Items.GLASS_PANES)
                .unlockedBy("has_tin_ingot", has(CTags.Items.TIN_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.BLAST_FURNACE_BRICKS, 2)
                .pattern("SS")
                .pattern("SS")
                .define('S', IRItems.SANDY_BRICK)
                .unlockedBy("has_sandy_brick", has(IRItems.SANDY_BRICK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.TERRACOTTA_BRICKS, 4)
                .pattern("BB")
                .pattern("BB")
                .define('B', IRItems.TERRACOTTA_BRICK)
                .unlockedBy("has_terracotta_brick", has(IRItems.TERRACOTTA_BRICK))
                .save(output);

        slab(output, RecipeCategory.BUILDING_BLOCKS, IRBlocks.TERRACOTTA_BRICK_SLAB, IRBlocks.TERRACOTTA_BRICKS);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.REFRACTORY_BRICK, 4)
                .pattern("SB")
                .pattern("BS")
                .define('S', IRItems.SANDY_BRICK)
                .define('B', IRItems.RAW_BAUXITE)
                .unlockedBy("has_terracotta", has(Items.TERRACOTTA))
                .save(output);
    }

    private void upgradeRecipes() {
        // TODO: Custom recipe for this

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.OVERCLOCKER_UPGRADE)
                .pattern(" W ")
                .pattern("ACA")
                .pattern(" W ")
                .define('W', DataComponentIngredient.of(false, DataComponentPredicate.builder()
                        .expect(IRDataComponents.FLUID.get(), SimpleFluidContent.copyOf(new FluidStack(Fluids.WATER, 4000)))
                        .build(), IRItems.FLUID_CELL.get()))
                .define('A', CTags.Items.ALUMINUM_PLATE)
                .define('C', IRItems.BASIC_CIRCUIT.get())
                .unlockedBy("has_item", has(IRItems.BASIC_CIRCUIT))
                .save(output);
    }

    private void miscRecipes() {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.CLAY_MOLD_BLANK.get(), 2)
                .requires(Items.CLAY_BALL, 3)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.SANDY_BRICK, 3)
                .requires(Items.CLAY_BALL, 2)
                .requires(Tags.Items.SANDS)
                .unlockedBy("has_clay_ball", has(Items.CLAY_BALL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.TERRACOTTA_BRICK, 5)
                .requires(Items.CLAY_BALL, 2)
                .requires(Items.BRICK, 2)
                .unlockedBy("has_clay_ball", has(Items.CLAY_BALL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ANTENNA.get())
                .pattern("# R")
                .pattern("#S ")
                .pattern(" ##")
                .define('#', CTags.Items.STEEL_PLATE)
                .define('S', IRItems.STEEL_ROD.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_battery", has(IRItems.ADVANCED_BATTERY.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.IRON_ROD.get(), 4)
                .pattern("#")
                .pattern("#")
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.STEEL_ROD.get(), 4)
                .pattern("#")
                .pattern("#")
                .define('#', CTags.Items.STEEL_INGOT)
                .unlockedBy("has_steel_ingot", has(CTags.Items.STEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ELECTRIC_MOTOR.get(), 2)
                .pattern(" SR")
                .pattern("C#S")
                .pattern("#C ")
                .define('#', IRItems.COPPER_WIRE.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', IRItems.STEEL_ROD.get())
                .define('S', CTags.Items.STEEL_INGOT)
                .unlockedBy("has_copper_wire", has(IRItems.COPPER_WIRE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_CIRCUIT.get())
                .pattern("###")
                .pattern("RBR")
                .pattern("###")
                .define('#', IRBlocks.COPPER_CABLE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('B', IRItems.CIRCUIT_BOARD)
                .unlockedBy("has_copper_cable", has(IRBlocks.COPPER_CABLE))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.CIRCUIT_BOARD.get())
                .requires(IRItems.PLANT_MASS, 3)
                .requires(IRItems.RUBBER, 2)
                .unlockedBy("has_plant_ball", has(IRItems.PLANT_MASS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.WOODEN_SCAFFOLDING.get(), 6)
                .pattern("PPP")
                .pattern("# #")
                .pattern("# #")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_wooden_rods", has(Tags.Items.RODS_WOODEN))
                .save(output);

        plantBallRecipes();

//        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, IRBlocks.REFRACTORY_BRICK, IRBlocks.REFRACTORY_STONE);
//        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, IRBlocks.REFRACTORY_STONE, IRBlocks.REFRACTORY_BRICK);
    }

    private void plantBallRecipes() {
        ShapelessRecipeBuilder plantBall0 = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.PLANT_BALL.get(), 2);
        requires(plantBall0, ItemTags.LEAVES, 9);
        plantBall0.unlockedBy("has_leaves", has(ItemTags.LEAVES))
                .save(output, IndustrialReforged.rl("plant_ball_from_leaves"));

        ShapelessRecipeBuilder plantBall1 = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.PLANT_BALL.get(), 2);
        requires(plantBall1, ItemTags.SAPLINGS, 5);
        plantBall1.unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
                .save(output, IndustrialReforged.rl("plant_ball_from_sapling"));

        ShapelessRecipeBuilder plantBall2 = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.PLANT_BALL.get(), 4);
        requires(plantBall2, Tags.Items.CROPS_SUGAR_CANE, 5);
        plantBall2.unlockedBy("has_sugar_cane", has(Tags.Items.CROPS_SUGAR_CANE))
                .save(output, IndustrialReforged.rl("plant_ball_from_sugar_cane"));
    }

    private void requires(ShapelessRecipeBuilder builder, TagKey<Item> tag, int count) {
        for (int i = 0; i < count; i++) {
            builder.requires(tag);
        }
    }

    private void smeltingRecipes() {
        ingotSmeltingRecipes();

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IRItems.STICKY_RESIN), RecipeCategory.MISC, IRItems.RUBBER, 0.7f, 200)
                .unlockedBy("has_sticky_resin", has(IRItems.STICKY_RESIN))
                .save(output);

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
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.THERMOMETER.get())
//                .pattern(" ##")
//                .pattern("#R#")
//                .pattern("R# ")
//                .define('R', Tags.Items.DUSTS_REDSTONE)
//                .define('#', Tags.Items.GLASS_PANES)
//                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
//                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.TOOLBOX.get())
                .pattern(" R ")
                .pattern("###")
                .define('R', IRItems.IRON_ROD)
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.BASIC_DRILL.get())
                .pattern(" # ")
                .pattern("#M#")
                .pattern("CBC")
                .define('#', CTags.Items.STEEL_INGOT)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('M', IRItems.ELECTRIC_MOTOR.get())
                .define('B', IRItems.BASIC_BATTERY.get())
                .unlockedBy("has_motor", has(IRItems.ELECTRIC_MOTOR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ADVANCED_DRILL.get())
                .pattern(" # ")
                .pattern("#D#")
                .pattern("CBC")
                .define('#', Tags.Items.GEMS_DIAMOND)
                .define('D', IRItems.BASIC_DRILL.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', IRItems.ADVANCED_BATTERY.get())
                .unlockedBy("has_battery", has(IRItems.ADVANCED_BATTERY.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.BASIC_CHAINSAW.get())
                .pattern(" ##")
                .pattern("CM#")
                .pattern("BC ")
                .define('#', CTags.Items.STEEL_INGOT)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('M', IRItems.ELECTRIC_MOTOR.get())
                .define('B', IRItems.BASIC_BATTERY.get())
                .unlockedBy("has_motor", has(IRItems.ELECTRIC_MOTOR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ADVANCED_CHAINSAW.get())
                .pattern(" ##")
                .pattern("CD#")
                .pattern("BC ")
                .define('#', Tags.Items.GEMS_DIAMOND)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('D', IRItems.BASIC_CHAINSAW.get())
                .define('B', IRItems.ADVANCED_BATTERY.get())
                .unlockedBy("has_motor", has(IRItems.ELECTRIC_MOTOR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, IRItems.ELECTRIC_HOE)
                .requires(IRItems.BASIC_BATTERY)
                .requires(ItemTags.HOES)
                .unlockedBy("has_battery", has(IRItems.BASIC_BATTERY))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, IRItems.ELECTRIC_TREE_TAP)
                .requires(IRItems.BASIC_BATTERY)
                .requires(IRItems.TREE_TAP)
                .unlockedBy("has_battery", has(IRItems.BASIC_BATTERY))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ROCK_CUTTER.get())
                .pattern("D# ")
                .pattern(" D#")
                .pattern(" CB")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('#', CTags.Items.STEEL_INGOT)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', IRItems.BASIC_BATTERY.get())
                .unlockedBy("has_battery", has(IRItems.ADVANCED_BATTERY.get()))
                .save(output);

        //ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.SCANNER.get())
        //        .pattern("A  ")
        //        .pattern("#G#")
        //        .pattern("#C#")
        //        .define('A', IRItems.ANTENNA)
        //        .define('G', Tags.Items.GLASS_PANES)
        //        .define('C', IRItems.BASIC_CIRCUIT)
        //        .define('#', CTags.Items.STEEL_PLATE)
        //        .unlockedBy("has_circuit", has(IRItems.BASIC_CIRCUIT.get()))
        //        .save(output);

    }

    private void armorRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.HAZMAT_HELMET.get())
                .pattern("WWW")
                .pattern("WGW")
                .pattern("LBL")
                .define('W', Items.ORANGE_WOOL)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('L', CTags.Items.LEAD_INGOT)
                .define('B', Items.IRON_BARS)
                .unlockedBy("has_lead_ingot", has(CTags.Items.LEAD_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.HAZMAT_CHESTPLATE.get())
                .pattern("W W")
                .pattern("RWR")
                .pattern("LRL")
                .define('W', Items.ORANGE_WOOL)
                .define('L', CTags.Items.LEAD_INGOT)
                .define('R', IRItems.RUBBER)
                .unlockedBy("has_rubber", has(IRItems.RUBBER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.HAZMAT_LEGGINGS.get())
                .pattern("WWW")
                .pattern("W W")
                .pattern("R R")
                .define('W', Items.ORANGE_WOOL)
                .define('R', IRItems.RUBBER)
                .unlockedBy("has_rubber", has(IRItems.RUBBER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.HAZMAT_BOOTS.get())
                .pattern("R R")
                .pattern("L L")
                .define('L', CTags.Items.LEAD_INGOT)
                .define('R', IRItems.RUBBER)
                .unlockedBy("has_rubber", has(IRItems.RUBBER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.JETPACK.get())
                .pattern("PCP")
                .pattern("PBP")
                .pattern("R R")
                .define('P', CTags.Items.STEEL_PLATE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', IRItems.BASIC_CIRCUIT)
                .define('B', IRItems.BASIC_BATTERY)
                .unlockedBy("has_circuit", has(IRItems.BASIC_CIRCUIT.get()))
                .save(output);

    }

    private void crucibleSmeltingRecipes() {
        int moltenIngot = 111;
        int moltenWire = moltenIngot / 3;
        int moltenBlock = moltenIngot * 9 + 1;
        int moltenRawMaterial = moltenIngot * 5 / 3;
        int moltenRawBlock = moltenBlock * 5 / 3;
        int twoMoltenIngots = moltenIngot * 2;

        // Copper
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, Tags.Items.INGOTS_COPPER, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, CTags.Items.COPPER_PLATE, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, CTags.Items.DUSTS_COPPER, moltenIngot);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, CTags.Items.COPPER_WIRE, moltenWire);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, Tags.Items.STORAGE_BLOCKS_COPPER, moltenBlock);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, Tags.Items.STORAGE_BLOCKS_RAW_COPPER, moltenRawBlock);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, Tags.Items.ORES_COPPER, twoMoltenIngots);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_COPPER, Tags.Items.RAW_MATERIALS_COPPER, moltenRawMaterial);

        // Steel
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_STEEL, CTags.Items.STEEL_INGOT, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_STEEL, CTags.Items.STEEL_PLATE, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_STEEL, CTags.Items.STEEL_WIRE, moltenWire);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_STEEL, CTags.Items.STEEL_ROD, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_STEEL, CTags.Items.DUSTS_STEEL, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_STEEL, CTags.Items.STORAGE_BLOCKS_STEEL, moltenBlock);

        // Iron
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_IRON, Tags.Items.INGOTS_IRON, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_IRON, CTags.Items.IRON_PLATE, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_IRON, CTags.Items.IRON_ROD, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_IRON, Tags.Items.STORAGE_BLOCKS_IRON, moltenBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_IRON, Tags.Items.RAW_MATERIALS_IRON, moltenRawMaterial);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_IRON, Tags.Items.STORAGE_BLOCKS_RAW_IRON, moltenRawBlock);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_NICKEL, CTags.Items.NICKEL_INGOT, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_NICKEL, CTags.Items.STORAGE_BLOCKS_NICKEL, moltenBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_NICKEL, CTags.Items.STORAGE_BLOCKS_RAW_NICKEL, moltenRawBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_NICKEL, CTags.Items.RAW_MATERIALS_NICKEL, moltenRawMaterial);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_GOLD, Tags.Items.INGOTS_GOLD, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_GOLD, Tags.Items.STORAGE_BLOCKS_GOLD, moltenBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_GOLD, Tags.Items.STORAGE_BLOCKS_RAW_GOLD, moltenRawBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_GOLD, CTags.Items.GOLD_WIRE, moltenWire);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_GOLD, Tags.Items.RAW_MATERIALS_GOLD, moltenRawMaterial);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_TIN, CTags.Items.TIN_INGOT, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_TIN, CTags.Items.TIN_PLATE, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_TIN, CTags.Items.TIN_WIRE, moltenWire);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_TIN, CTags.Items.STORAGE_BLOCKS_TIN, moltenBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_TIN, CTags.Items.STORAGE_BLOCKS_RAW_TIN, moltenRawBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_TIN, CTags.Items.RAW_MATERIALS_TIN, moltenRawMaterial);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_LEAD, CTags.Items.LEAD_INGOT, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_LEAD, CTags.Items.STORAGE_BLOCKS_LEAD, moltenBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_LEAD, CTags.Items.STORAGE_BLOCKS_RAW_LEAD, moltenRawBlock);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_LEAD, CTags.Items.RAW_MATERIALS_LEAD, moltenRawMaterial);

        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_ALUMINUM, CTags.Items.ALUMINUM_INGOT, moltenIngot);
        itemCrucibleSmeltingRecipe(IRFluids.MOLTEN_ALUMINUM, CTags.Items.STORAGE_BLOCKS_ALUMINUM, moltenBlock);
    }

    private void itemCrucibleSmeltingRecipe(MoltenMetalFluid fluid, TagKey<Item> item, int fluidAmount) {
        irRecipe(new CrucibleSmeltingRecipe(IngredientWithCount.of(item),
                fluid.toStack(fluidAmount),
                200,
                200
        ));
    }

    private void moldCastingRecipes() {
        moldCastingRecipe(Tags.Items.INGOTS, IRFluids.MOLTEN_STEEL, IRItems.STEEL_MOLD_INGOT);
        moldCastingRecipe(CTags.Items.PLATES, IRFluids.MOLTEN_STEEL, IRItems.STEEL_MOLD_PLATE);
        moldCastingRecipe(Tags.Items.RODS, IRFluids.MOLTEN_STEEL, IRItems.STEEL_MOLD_ROD);
        moldCastingRecipe(CTags.Items.WIRES, IRFluids.MOLTEN_STEEL, IRItems.STEEL_MOLD_WIRE);
    }

    private void castingRecipes() {
        ingotCastingRecipe(IRFluids.MOLTEN_ALUMINUM, IRItems.ALUMINUM_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_COPPER, Items.COPPER_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_GOLD, Items.GOLD_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_IRON, Items.IRON_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_NICKEL, IRItems.NICKEL_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_STEEL, IRItems.STEEL_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_TIN, IRItems.TIN_INGOT);
        ingotCastingRecipe(IRFluids.MOLTEN_LEAD, IRItems.LEAD_INGOT);

        wireCastingRecipe(IRFluids.MOLTEN_TIN, IRItems.TIN_WIRE);
        wireCastingRecipe(IRFluids.MOLTEN_COPPER, IRItems.COPPER_WIRE);
        wireCastingRecipe(IRFluids.MOLTEN_GOLD, IRItems.GOLD_WIRE);

        rodCastingRecipe(IRFluids.MOLTEN_STEEL, IRItems.STEEL_ROD);
        rodCastingRecipe(IRFluids.MOLTEN_IRON, IRItems.IRON_ROD);

        plateCastingRecipe(IRFluids.MOLTEN_ALUMINUM, IRItems.ALUMINUM_PLATE);
        plateCastingRecipe(IRFluids.MOLTEN_IRON, IRItems.IRON_PLATE);
        plateCastingRecipe(IRFluids.MOLTEN_COPPER, IRItems.COPPER_PLATE);
        plateCastingRecipe(IRFluids.MOLTEN_STEEL, IRItems.STEEL_PLATE);
        plateCastingRecipe(IRFluids.MOLTEN_TIN, IRItems.TIN_PLATE);
    }

    private void moldCastingRecipe(TagKey<Item> moldIngredientTag, PDLFluid fluid, ItemLike resultMoldItem) {
        irRecipe(new BasinMoldCastingRecipe(
                Ingredient.of(moldIngredientTag),
                FluidIngredientWithAmount.of(fluid.toStack(333)),
                new ItemStack(resultMoldItem.asItem()),
                200
        ));
    }

    private void rodCastingRecipe(PDLFluid fluid, ItemLike resultIngotItem) {
        irRecipe(new BasinCastingRecipe(
                Ingredient.of(IRTags.Items.MOLDS_ROD),
                FluidIngredientWithAmount.of(fluid.toStack(111)),
                new ItemStack(resultIngotItem.asItem(), 4),
                200
        ));
    }

    private void wireCastingRecipe(PDLFluid fluid, ItemLike resultIngotItem) {
        irRecipe(new BasinCastingRecipe(
                Ingredient.of(IRTags.Items.MOLDS_WIRE),
                FluidIngredientWithAmount.of(fluid.toStack(37)),
                new ItemStack(resultIngotItem.asItem(), 3),
                200
        ));
    }

    private void ingotCastingRecipe(PDLFluid fluid, ItemLike resultIngotItem) {
        irRecipe(new BasinCastingRecipe(
                Ingredient.of(IRTags.Items.MOLDS_INGOT),
                FluidIngredientWithAmount.of(fluid.toStack(111)),
                resultIngotItem.asItem().getDefaultInstance(),
                200
        ));
    }

    private void plateCastingRecipe(PDLFluid fluid, ItemLike resultPlateItem) {
        irRecipe(new BasinCastingRecipe(
                Ingredient.of(IRTags.Items.MOLDS_PLATE),
                FluidIngredientWithAmount.of(fluid.toStack(111)),
                resultPlateItem.asItem().getDefaultInstance(),
                200
        ));
    }

    private void centrifugeRecipes() {
        centrifugeRecipe(IngredientWithCount.of(IRItems.STICKY_RESIN), 200, 1,
                asStack(IRItems.RUBBER, 3));
        centrifugeRecipe(IngredientWithCount.of(IRBlocks.RUBBER_TREE_LOG, 16), IRFluids.METHANE.toStack(333), 200, 100,
                asStack(IRItems.RUBBER, 6), asStack(IRItems.COAL_DUST, 2));
        centrifugeRecipe(IngredientWithCount.of(Tags.Items.FOODS_RAW_MEAT, 3), IRFluids.METHANE.toStack(666), 200, 100,
                asStack(IRItems.COAL_DUST), asStack(Items.BONE_MEAL));
        centrifugeRecipe(IngredientWithCount.of(Items.CLAY), new FluidStack(Fluids.WATER, 1000), 200, 100,
                asStack(Items.DIRT, 3), asStack(Items.CLAY_BALL, 3));
        centrifugeRecipe(IngredientWithCount.of(IRItems.PLANT_BALL, 2), IRFluids.BIO_MASS.toStack(333), 200, 100,
                asStack(IRItems.PLANT_MASS, 3), asStack(Items.VINE, 2), asStack(Items.WHEAT_SEEDS, 4));
        centrifugeRecipe(IngredientWithCount.of(Items.SOUL_SAND, 16), IRFluids.OIL.toStack(666), 200, 100,
                asStack(Items.SAND, 10), asStack(IRItems.COAL_DUST, 3), asStack(Items.GOLD_NUGGET, 2));
    }

    private ItemStack asStack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    private ItemStack asStack(ItemLike item) {
        return asStack(item, 1);
    }

    private void centrifugeRecipe(IngredientWithCount ingredient, int duration, int energy, ItemStack... items) {
        irRecipe(new CentrifugeRecipe(ingredient, List.of(items), FluidStack.EMPTY, duration, energy));
    }

    private void centrifugeRecipe(IngredientWithCount ingredient, FluidStack resultFluid, int duration, int energy, ItemStack... items) {
        irRecipe(new CentrifugeRecipe(ingredient, List.of(items), resultFluid, duration, energy));
    }

    private void blastFurnaceRecipes() {
        int moltenIngot = 111;
        int moltenWire = moltenIngot / 3;
        int moltenBlock = moltenIngot * 9 + 1;
        int moltenIngotAndTwoThirds = moltenIngot * 5 / 3;
        int moltenBlockAndTwoThirds = moltenBlock * 5 / 3;
        int twoMoltenIngots = moltenIngot * 2;

        blastFurnaceRecipe(new FluidStack(IRFluids.MOLTEN_STEEL.getStillFluid(), twoMoltenIngots), 200, 180,
                IngredientWithCount.of(Tags.Items.INGOTS_IRON, 2), IngredientWithCount.of(ItemTags.COALS));
        blastFurnaceRecipe(new FluidStack(IRFluids.MOLTEN_ALUMINUM.getStillFluid(), moltenIngot), 200, 180,
                IngredientWithCount.of(IRItems.RAW_BAUXITE));
        blastFurnaceRecipe(new FluidStack(IRFluids.MOLTEN_ALUMINUM.getStillFluid(), twoMoltenIngots), 200, 180,
                IngredientWithCount.of(CTags.Items.ORES_BAUXITE));
        blastFurnaceRecipe(new FluidStack(IRFluids.MOLTEN_ALUMINUM.getStillFluid(), moltenBlockAndTwoThirds), 200 * 9, 180,
                IngredientWithCount.of(CTags.Items.STORAGE_BLOCKS_RAW_BAUXITE));
    }

    private void blastFurnaceRecipe(FluidStack resultFluid, int duration, int heat, IngredientWithCount... ingredients) {
        irRecipe(new BlastFurnaceRecipe(List.of(ingredients), resultFluid, duration, heat));
    }

    private void machineCraftingRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BASIC_MACHINE_FRAME.get(), 2)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .define('#', CTags.Items.ALUMINUM_INGOT)
                .unlockedBy("has_aluminum_ingot", has(CTags.Items.ALUMINUM_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.BASIC_GENERATOR.getBlock())
                .pattern("B")
                .pattern("M")
                .pattern("F")
                .define('B', IRItems.BASIC_BATTERY.get())
                .define('M', IRBlocks.BASIC_MACHINE_FRAME.get())
                .define('F', Blocks.FURNACE)
                .unlockedBy("has_machine_frame", has(IRBlocks.BASIC_MACHINE_FRAME.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRMachines.CENTRIFUGE.getBlock())
                .pattern("###")
                .pattern("RBR")
                .pattern("#M#")
                .define('#', CTags.Items.IRON_PLATE)
                .define('R', IRItems.IRON_ROD)
                .define('B', IRBlocks.BASIC_MACHINE_FRAME)
                .define('M', IRItems.ELECTRIC_MOTOR)
                .unlockedBy("has_machine_frame", has(IRBlocks.BASIC_MACHINE_FRAME.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BATTERY_BOX.get())
                .pattern("#C#")
                .pattern("BBB")
                .pattern("###")
                .define('#', ItemTags.PLANKS)
                .define('B', IRItems.BASIC_BATTERY)
                .define('C', IRBlocks.COPPER_CABLE)
                .unlockedBy("has_battery", has(IRItems.BASIC_BATTERY.get()))
                .save(output);

        // "MANUAL" Machines

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.CRAFTING_STATION.get())
                .pattern("WWW")
                .pattern("#R#")
                .pattern("#C#")
                .define('W', Items.RED_WOOL)
                .define('#', ItemTags.PLANKS)
                .define('R', Blocks.CRAFTING_TABLE)
                .define('C', Blocks.CHEST)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.DRAIN.get())
                .pattern("PBP")
                .pattern("PUP")
                .pattern("###")
                .define('P', ItemTags.PLANKS)
                .define('#', Tags.Items.COBBLESTONES)
                .define('U', Items.BUCKET)
                .define('B', Blocks.IRON_BARS)
                .unlockedBy("has_cobblestone", has(Tags.Items.COBBLESTONES))
                .save(output);

        // MISC

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.COIL.get())
                .pattern("###")
                .pattern("#R#")
                .pattern("###")
                .define('#', IRItems.COPPER_WIRE.get())
                .define('R', IRItems.IRON_ROD)
                .unlockedBy("has_copper_wire", has(IRItems.COPPER_WIRE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.COIL.get())
                .pattern("###")
                .pattern("#R#")
                .pattern("###")
                .define('#', Tags.Items.INGOTS_COPPER)
                .define('R', IRItems.IRON_ROD)
                .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                .save(output, IndustrialReforged.rl("coil_from_ingots"));
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
        doorBuilder(IRBlocks.RUBBER_TREE_DOOR, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
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
                .requires(CTags.Items.RUBBER)
                .requires(wireMaterial)
                .unlockedBy("has_rubber", has(CTags.Items.RUBBER))
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

    private void irRecipe(Recipe<?> recipe) {
        IRRecipeBuilder.of(recipe).save(output);
    }
}

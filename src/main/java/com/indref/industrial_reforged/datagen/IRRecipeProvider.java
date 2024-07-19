package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class IRRecipeProvider extends RecipeProvider {

    public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookUpProvider) {
        super(output, lookUpProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        rubberWoodRecipes(output);
    }

    private static void rubberWoodRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_PLANKS.get(), 4)
                .requires(IRBlocks.RUBBER_TREE_LOG.get())
                .group("planks")
                .unlockedBy("has_log", has(IRBlocks.RUBBER_TREE_LOG.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_SLAB.get(), 6)
                .pattern("###")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .group("slabs")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .group("stairs")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, IRBlocks.RUBBER_TREE_BUTTON.get())
                .requires(IRBlocks.RUBBER_TREE_PLANKS.get())
                .group("buttons")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .group("pressure_plates")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', IRBlocks.RUBBER_TREE_LOG.get())
                .group("barks")
                .unlockedBy("has_log", has(IRBlocks.RUBBER_TREE_LOG.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', IRBlocks.STRIPPED_RUBBER_TREE_LOG.get())
                .group("barks")
                .unlockedBy("has_stripped_log", has(IRBlocks.STRIPPED_RUBBER_TREE_LOG.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_FENCE.get(), 3)
                .pattern("#S#")
                .pattern("#S#")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .group("fences")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_FENCE_GATE.get())
                .pattern("S#S")
                .pattern("S#S")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .group("fence_gates")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_TRAPDOOR.get(), 2)
                .pattern("###")
                .pattern("###")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .group("trapdoors")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', IRBlocks.RUBBER_TREE_PLANKS.get())
                .group("doors")
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS.get()))
                .save(output);
    }
}

package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;

import java.util.concurrent.CompletableFuture;

public class IRRecipeProvider extends RecipeProvider {

	public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_PLANKS.get(), 4)
			.requires(IRBlocks.RUBBER_TREE_LOG.get()).group("planks").unlockedBy("has_log", has(IRBlocks.RUBBER_TREE_LOG.get())).save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_WOOD.get(), 3).define('#', IRBlocks.RUBBER_TREE_LOG.get())
			.pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(IRBlocks.RUBBER_TREE_LOG.get())).save(output);
	}
}

package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.content.IRBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;

import java.util.function.Consumer;

public class IRRecipeProvider extends RecipeProvider {

	public IRRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_PLANKS.get(), 4)
			.requires(IRBlocks.RUBBER_TREE_LOG.get()).group("planks").unlockedBy("has_log", has(IRBlocks.RUBBER_TREE_LOG.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_WOOD.get(), 3).define('#', IRBlocks.RUBBER_TREE_LOG.get())
			.pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(IRBlocks.RUBBER_TREE_LOG.get())).save(consumer);
	}
}

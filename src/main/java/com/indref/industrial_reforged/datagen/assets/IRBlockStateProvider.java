package com.indref.industrial_reforged.datagen.assets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class IRBlockStateProvider extends BlockStateProvider {
	public IRBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, IndustrialReforged.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		treeStatesAndModels();

		metalStorageBlocks();
	}

	private void metalStorageBlocks() {
		for (DeferredBlock<Block> block : IRBlocks.METAL_STORAGE_BLOCKS) {
			simpleBlock(block.get());
		}

		for (DeferredBlock<Block> block : IRBlocks.RAW_STORAGE_BLOCKS) {
			simpleBlock(block.get());
		}
	}

	private void treeStatesAndModels() {
        logBlock(IRBlocks.RUBBER_TREE_LOG.get(), "tree");
		logBlock(IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(), "tree");
		woodBlock(IRBlocks.RUBBER_TREE_WOOD.get(), IRBlocks.RUBBER_TREE_LOG.get(), "tree");
		woodBlock(IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get(), IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(), "tree");
		simpleBlockParentItem(IRBlocks.RUBBER_TREE_PLANKS.get(), "cube_all", "all", "tree");
		simpleBlockParentItem(IRBlocks.RUBBER_TREE_LEAVES.get(), "leaves", "all", "tree");
		simpleBlock(IRBlocks.RUBBER_TREE_SAPLING.get(), "cross", "cross", "tree");
		buttonBlock(IRBlocks.RUBBER_TREE_BUTTON.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		inventoryModel(IRBlocks.RUBBER_TREE_BUTTON.get(), "button_inventory", blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		doorBlock(IRBlocks.RUBBER_TREE_DOOR.get(),
				blockTexture(IRBlocks.RUBBER_TREE_DOOR.get(), "tree", "_bottom"), blockTexture(IRBlocks.RUBBER_TREE_DOOR.get(), "tree", "_top"));
		fenceBlock(IRBlocks.RUBBER_TREE_FENCE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		inventoryModel(IRBlocks.RUBBER_TREE_FENCE.get(), "fence_inventory", blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		fenceGateBlock(IRBlocks.RUBBER_TREE_FENCE_GATE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		pressurePlateBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		trapdoorBlock(IRBlocks.RUBBER_TREE_TRAPDOOR.get(), blockTexture(IRBlocks.RUBBER_TREE_TRAPDOOR.get(), "tree"), false);
		slabBlock(IRBlocks.RUBBER_TREE_SLAB.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "block/rubber_tree_planks"),
			blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
		stairsBlock(IRBlocks.RUBBER_TREE_STAIRS.get(),
				blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
	}

	private void logBlock(RotatedPillarBlock block, String textureFolder) {
		ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
		axisBlock(block, blockTexture(block, textureFolder), blockTexture(block, textureFolder, "_top"));
		simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
	}

	private void woodBlock(RotatedPillarBlock block, Block blockTexture, String textureFolder) {
		ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
		ResourceLocation texture = blockTexture(blockTexture, textureFolder);
		axisBlock(block, models().cubeColumn(name.getPath(), texture, texture),
			models().cubeColumnHorizontal(name.getPath(), texture, texture));
		simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
	}

	private void inventoryModel(Block block, String parentModel, ResourceLocation texture) {
		ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
		models().getBuilder(name.getPath() + "_inventory").parent(models().getExistingFile(ResourceLocation.parse(parentModel))).texture("texture", texture);
	}

	private ResourceLocation blockTexture(Block block, String textureFolder) {
		return blockTexture(block, textureFolder, "");
	}

	private ResourceLocation blockTexture(Block block, String textureFolder, String suffix) {
		ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
		if (textureFolder == null || textureFolder.trim().isEmpty())
			return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
		return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + textureFolder + "/" + name.getPath() + suffix);
	}

	private ModelFile cubeAll(Block block, String textureFolder) {
		ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
		return models().singleTexture(name.getPath(), mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_all"), "all", blockTexture(block, textureFolder));
	}

	public void simpleBlock(Block block, String textureFolder) {
		simpleBlock(block, cubeAll(block, textureFolder));
	}

	private void simpleBlock(Block block, String modelName, String textureKey, String textureFolder) {
		simpleBlock(block, models().singleTexture(
			BuiltInRegistries.BLOCK.getKey(block).getPath(), ResourceLocation.parse(ModelProvider.BLOCK_FOLDER + "/" + modelName),
			textureKey, blockTexture(block, textureFolder)));
	}

	private void simpleBlockParentItem(Block block, String modelName, String textureKey, String textureFolder) {
		simpleBlockWithItem(block, models()
			.withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), ModelProvider.BLOCK_FOLDER + "/" + modelName)
			.texture(textureKey, blockTexture(block, textureFolder)));
	}
}

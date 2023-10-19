package com.indref.industrial_reforged.content.datagen;

import java.util.Objects;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.IRBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class IRItemModelProvider extends ItemModelProvider {

	public IRItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, IndustrialReforged.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		basicItemBlock(IRBlocks.RUBBER_TREE_SAPLING.get().asItem(), "tree");
		parentItemBlock(IRBlocks.RUBBER_TREE_BUTTON.get().asItem(), "_inventory");
		basicItem(IRBlocks.RUBBER_TREE_DOOR.get().asItem(), "tree");
		parentItemBlock(IRBlocks.RUBBER_TREE_FENCE.get().asItem(), "_inventory");
		parentItemBlock(IRBlocks.RUBBER_TREE_FENCE_GATE.get().asItem());
		parentItemBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get().asItem());
		parentItemBlock(IRBlocks.RUBBER_TREE_SLAB.get().asItem());
		parentItemBlock(IRBlocks.RUBBER_TREE_STAIRS.get().asItem());
		parentItemBlock(IRBlocks.RUBBER_TREE_TRAPDOOR.get().asItem(), "_bottom");
	}

	public ItemModelBuilder basicItem(Item item, String textureFolder) {
		String folder = textureFolder + "/";
		if (textureFolder == null || textureFolder.trim().isEmpty())
			folder = "";
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
		return getBuilder(name.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", new ResourceLocation(name.getNamespace(), "item/" + folder + name.getPath()));
	}

	public ItemModelBuilder basicItemBlock(Item item) {
		return basicItemBlock(item, "");
	}

	public ItemModelBuilder basicItemBlock(Item item, String textureFolder) {
		String folder = textureFolder + "/";
		if (textureFolder == null || textureFolder.trim().isEmpty())
			folder = "";
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
		return getBuilder(name.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", new ResourceLocation(name.getNamespace(), "block/" + folder + name.getPath()));
	}

	public ItemModelBuilder parentItemBlock(Item item) {
		return parentItemBlock(item, "");
	}

	public ItemModelBuilder parentItemBlock(Item item, String suffix) {
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
		return getBuilder(name.toString())
			.parent(new ModelFile.UncheckedModelFile(new ResourceLocation(name.getNamespace(), "block/" + name.getPath() + suffix)));
	}
}

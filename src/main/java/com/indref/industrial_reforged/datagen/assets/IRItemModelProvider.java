package com.indref.industrial_reforged.datagen.assets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.items.storage.BatteryItem;
import com.indref.industrial_reforged.content.items.tools.ThermometerItem;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRFluids;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IRItemModelProvider extends ItemModelProvider {

    public IRItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IndustrialReforged.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(IRBlocks.RUBBER_TREE_DOOR.get().asItem());
        basicItemBlock(IRBlocks.RUBBER_TREE_SAPLING.asItem(), "tree");
        parentItemBlock(IRBlocks.RUBBER_TREE_BUTTON.get().asItem(), "_inventory");
        parentItemBlock(IRBlocks.RUBBER_TREE_FENCE.get().asItem(), "_inventory");
        parentItemBlock(IRBlocks.RUBBER_TREE_TRAPDOOR.get().asItem(), "_bottom");

        bucket(IRFluids.MOLTEN_STEEL_SOURCE.get());
        bucket(IRFluids.MOLTEN_ALUMINUM_SOURCE.get());
        basicItem(IRItems.BASIC_CIRCUIT);
        basicItem(IRItems.ADVANCED_CIRCUIT);
        basicItem(IRItems.ULTIMATE_CIRCUIT);
        basicItem(IRItems.ANTENNA);
        basicItem(IRItems.BLUEPRINT);
        basicItem(IRItems.CLAY_MOLD_BLANK);
        basicItem(IRItems.CLAY_MOLD_INGOT);
        basicItem(IRItems.CLAY_MOLD_WIRE);
        basicItem(IRItems.FERTILIZER);
        basicItem(IRItems.OIL_BUCKET);
        basicItem(IRItems.PLANT_BALL);
        basicItem(IRItems.RUBBER);
        basicItem(IRItems.RUBBER_SHEET);
        basicItem(IRItems.SANDY_BRICK);
        basicItem(IRItems.STICKY_RESIN);
        basicItem(IRItems.CIRCUIT_BOARD);

        basicItem(IRItems.SCANNER);
        basicItem(IRItems.ADVANCED_DRILL);
        basicItem(IRItems.ELECTRIC_DRILL);
        basicItem(IRItems.NANO_SABER);
        basicItem(IRItems.ROCK_CUTTER);

        basicItem(IRItems.HAMMER);
        basicItem(IRItems.TREE_TAP);
        basicItem(IRItems.WRENCH);
        toolbox(IRItems.TOOLBOX);

        basicItem(IRItems.ELECTRIC_MOTOR);

 		basicItem(IRItems.TIN_WIRE);
        basicItem(IRItems.COPPER_WIRE);
        basicItem(IRItems.GOLD_WIRE);
        basicItem(IRItems.STEEL_WIRE);

        basicItem(IRItems.HAZMAT_HELMET);
        basicItem(IRItems.HAZMAT_CHESTPLATE);
        basicItem(IRItems.HAZMAT_LEGGINGS);
        basicItem(IRItems.HAZMAT_BOOTS);

        battery(IRItems.BASIC_BATTERY, 6);
        battery(IRItems.ADVANCED_BATTERY, 8);
        battery(IRItems.ULTIMATE_BATTERY, 9);

        fluidCell(IRItems.FLUID_CELL);

        thermometer(IRItems.THERMOMETER);

        basicItem(IRItems.ALUMINUM_INGOT);
        basicItem(IRItems.IRIDIUM_INGOT);
        basicItem(IRItems.CHROMIUM_INGOT);
        basicItem(IRItems.LEAD_INGOT);
        basicItem(IRItems.NICKEL_INGOT);
        basicItem(IRItems.STEEL_INGOT);
        basicItem(IRItems.TIN_INGOT);
        basicItem(IRItems.TITANIUM_INGOT);
        basicItem(IRItems.URANIUM_INGOT);

        basicItem(IRItems.RAW_BAUXITE);
        basicItem(IRItems.RAW_IRIDIUM);
        basicItem(IRItems.RAW_CHROMIUM);
        basicItem(IRItems.RAW_LEAD);
        basicItem(IRItems.RAW_NICKEL);
        basicItem(IRItems.RAW_TIN);
        basicItem(IRItems.RAW_URANIUM);

        basicItem(IRItems.COPPER_DUST);
        basicItem(IRItems.STEEL_DUST);

        basicItem(IRItems.COPPER_PLATE);
        basicItem(IRItems.IRON_PLATE);
        basicItem(IRItems.STEEL_PLATE);
        basicItem(IRItems.TIN_PLATE);

        basicItem(IRItems.IRON_ROD);
        basicItem(IRItems.STEEL_ROD);

        basicItem(IRItems.URANIUM_FUEL_ROD);

        cable(IRBlocks.TIN_CABLE);
        cable(IRBlocks.COPPER_CABLE);
        cable(IRBlocks.GOLD_CABLE);
        cable(IRBlocks.STEEL_CABLE);

        blockItems();
    }

    private void toolbox(ItemLike item) {
        withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", itemTexture(item))
                .texture("layer1", extend(itemTexture(item), "_overlay"));
    }

    private void fluidCell(ItemLike item) {
        withExistingParent(name(item), ResourceLocation.fromNamespaceAndPath("neoforge", "item/default"))
                .texture("base", itemTexture(item))
                .texture("fluid", extend(itemTexture(item), "_overlay"))
                .texture("particle", extend(itemTexture(item), "_overlay"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .applyTint(true)
                .flipGas(true)
                .fluid(Fluids.EMPTY);
    }

    private void thermometer(ItemLike item) {
        ItemModelBuilder builder = getBuilder(name(item))
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", extend(itemTexture(item), "_0"));

        for (int i = 0; i < 7; i++) {
            builder.override()
                    .model(basicItem(item, "_" + i))
                    .predicate(modLoc(ThermometerItem.DISPLAY_TEMPERATURE_KEY), i)
                    .end();
        }
    }

    private void battery(ItemLike item, int stages) {
        ItemModelBuilder builder = withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", extend(itemTexture(item), "_0"));

        for (int i = 0; i < stages; i++) {
            builder.override()
                    .model(basicItem(item, "_" + i))
                    .predicate(modLoc(BatteryItem.ENERGY_STAGE_KEY), i)
                    .end();
        }
    }

    private void bucket(Fluid f) {
        withExistingParent(key(f.getBucket()).getPath(), ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket_drip"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(f);
    }

    private void cable(ItemLike item) {
        withExistingParent(name(item), modLoc("item/cable_inventory"))
                .texture("texture", blockTexture(item));
    }

    private void blockItems() {
        for (DeferredItem<BlockItem> blockItem : IRItems.BLOCK_ITEMS) {
            parentItemBlock(blockItem.get());
        }
    }

    public ItemModelBuilder basicItem(ItemLike itemLike) {
        return basicItem(itemLike, "");
    }

    public ItemModelBuilder basicItem(ItemLike item, String suffix) {
        ResourceLocation location = key(item);
        return getBuilder(location + suffix)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath() + suffix));
    }

    private ItemModelBuilder basicItemBlock(Item item, String textureFolder) {
        String folder = textureFolder + "/";
        if (textureFolder == null || textureFolder.trim().isEmpty())
            folder = "";
        ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return getBuilder(name.toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + folder + name.getPath()));
    }

    private ItemModelBuilder parentItemBlock(Item item) {
        return parentItemBlock(item, "");
    }

    private ItemModelBuilder parentItemBlock(Item item, String suffix) {
        ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return getBuilder(name.toString())
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath() + suffix)));
    }

    private ResourceLocation inDir(ResourceLocation rl, String directory) {
        StringBuilder path = new StringBuilder();
        String[] dirs = rl.getPath().split("/");
        for (int i = 0; i < dirs.length; i++) {
            if (i == dirs.length - 1) {
                path.append(directory).append("/");
            }
            path.append(dirs[i]).append(i != dirs.length - 1 ? "/" : "");
        }
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), path.toString());
    }

    private static @NotNull ResourceLocation key(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private static @NotNull String namespace(ItemLike item) {
        return key(item).getNamespace();
    }

    private static @NotNull ResourceLocation loc(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public String name(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

    public ResourceLocation itemTexture(ItemLike item) {
        ResourceLocation name = key(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + name.getPath());
    }

    public ResourceLocation blockTexture(ItemLike item) {
        ResourceLocation name = key(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }

}

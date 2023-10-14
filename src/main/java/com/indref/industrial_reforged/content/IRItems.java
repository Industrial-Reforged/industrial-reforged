package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.items.EnergyTestItem;
import com.indref.industrial_reforged.api.items.ToolItem;
import com.indref.industrial_reforged.content.items.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IRItems {
    /**
     * Variable used for registering and storing all items under the "indref" mod-id
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialReforged.MODID);

    public static final RegistryObject<Item> WRENCH = registerItem("wrench",
            () -> new WrenchItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HAMMER = registerItem("hammer",
            () -> new ToolItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TOOLBOX = registerItem("toolbox",
            () -> new ToolboxItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCANNER = registerItem("scanner",
            () -> new ScannerItem(new Item.Properties()));
    public static final RegistryObject<Item> ENERGY_TEST_ITEM = registerItem("energy_test",
            () -> new EnergyTestItem(new Item.Properties()));
    public static final RegistryObject<Item> FLUID_CELL = registerItem("fluid_cell",
            () -> new FluidCellItem(new Item.Properties().stacksTo(16), 1000));
    public static final RegistryObject<Item> SEED_POUCH = registerItem("seed_pouch",
            () -> new SeedPouchItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RUBBER_SHEET = registerItem("rubber_sheet",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds",
            () -> new ItemNameBlockItem(IRBlocks.CORN_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAZMAT_BOOTS = registerItem("hazmat_boots",
            () -> new HazmatSuiteItem(ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    /**
     * Registers a new item
     *
     * @param name the item name referred to as id in-game
     * @param item the item you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the item-registry-object built from the parameters
     */
    private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

}

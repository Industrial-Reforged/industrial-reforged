package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.items.armor.HazmatSuiteItem;
import com.indref.industrial_reforged.registries.items.misc.FertilizerItem;
import com.indref.industrial_reforged.registries.items.misc.MiningPipeBlockItem;
import com.indref.industrial_reforged.registries.items.reactor.UraniumFuelRod;
import com.indref.industrial_reforged.registries.items.storage.FluidCellItem;
import com.indref.industrial_reforged.registries.items.storage.LunchBagItem;
import com.indref.industrial_reforged.registries.items.storage.ToolboxItem;
import com.indref.industrial_reforged.registries.items.tools.*;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, IndustrialReforged.MODID);
    //tools
    public static final Supplier<Item> WRENCH = registerItem("wrench",
            () -> new WrenchItem(new Item.Properties()));
    public static final Supplier<Item> HAMMER = registerItem("hammer",
            () -> new HammerItem(new Item.Properties()));
    public static final Supplier<Item> TREE_TAP = registerItem("tree_tap",
            () -> new TreeTapItem(new Item.Properties()));
    public static final Supplier<Item> NANO_SABER = registerItem("nano_saber",
            () -> new NanoSaberItem(new Item.Properties(), EnergyTiers.HIGH));
    public static final Supplier<Item> SCANNER = registerItem("scanner",
            () -> new ScannerItem(new Item.Properties()));
    public static final Supplier<Item> ELECTRIC_WRENCH = registerItem("electric_wrench",
            () -> new ElectricWrenchItem(new Item.Properties()));
    public static final Supplier<Item> ELECTRIC_HOE = registerItem("electric_hoe",
            () -> new ElectricHoeItem(Tiers.IRON, 0, 0, new Item.Properties()));
    public static final Supplier<Item> ELECTRIC_TREE_TAP = registerItem("electric_tree_tap",
            () -> new ElectricTreeTapItem(new Item.Properties()));
    public static final Supplier<Item> ELECTRIC_DRILL = registerItem("electric_drill",
            () -> new ElectricTreeTapItem(new Item.Properties()));
    public static final Supplier<Item> ADVANCED_DRILL = registerItem("advanced_drill",
            () -> new ElectricTreeTapItem(new Item.Properties()));
    public static final Supplier<Item> TAPE_MEASURE = registerItem("tape_measure",
            () -> new TapeMeasureItem(new Item.Properties()));
    public static final Supplier<Item> BLUEPRINT = registerItem("blueprint",
            () -> new Item(new Item.Properties()));

    // Item storages
    public static final Supplier<Item> LUNCH_BAG = registerItem("lunch_bag",
            () -> new LunchBagItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> FLUID_CELL = registerItem("fluid_cell",
            () -> new FluidCellItem(new Item.Properties().stacksTo(16), 1000));
    public static final Supplier<Item> TOOLBOX = registerItem("toolbox",
            () -> new ToolboxItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> URANIUM_FUEL_ROD = registerItem("uranium_fuel_rod",
            () -> new UraniumFuelRod(new Item.Properties().stacksTo(1)));

    // armor
    public static final Supplier<Item> HAZMAT_BOOTS = registerItem("hazmat_boots",
            () -> new HazmatSuiteItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final Supplier<Item> HAZMAT_LEGGINGS = registerItem("hazmat_leggings",
            () -> new HazmatSuiteItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> HAZMAT_CHESTPLATE = registerItem("hazmat_chestplate",
            () -> new HazmatSuiteItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> HAZMAT_HELMET = registerItem("hazmat_helmet",
            () -> new HazmatSuiteItem(ArmorItem.Type.HELMET, new Item.Properties()));

    //misc
    public static final Supplier<Item> RUBBER_SHEET = registerItem("rubber_sheet",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STICKY_RESIN = registerItem("sticky_resin",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FERTILIZER = registerItem("fertilizer",
            () -> new FertilizerItem(new Item.Properties()));

    // Needs to be registered manual for custom placement
    public static final Supplier<Item> MINING_PIPE = registerItem("mining_pipe",
            () -> new MiningPipeBlockItem(new Item.Properties()));

    //ores
    public static final Supplier<Item> RAW_BAUXITE = registerItem("raw_bauxite",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_CHROMIUM = registerItem("raw_chromium",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_IRIDIUM = registerItem("raw_iridium",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_LEAD = registerItem("raw_lead",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_NICKEL = registerItem("raw_nickel",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_TIN = registerItem("raw_tin",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_URANIUM = registerItem("raw_uranium",
            () -> new Item(new Item.Properties()));

    // ingots
    public static final Supplier<Item> ALUMINUM_INGOT = registerItem("aluminum_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TITANIUM_INGOT = registerItem("titanium_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHROMIUM_INGOT = registerItem("chromium_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> IRIDIUM_INGOT = registerItem("iridium_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LEAD_INGOT = registerItem("lead_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NICKEL_INGOT = registerItem("nickel_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TIN_INGOT = registerItem("tin_ingot",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> URANIUM_INGOT = registerItem("uranium_ingot",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SOAP_WATER_BUCKET = registerItem("soap_water_bucket",
            () -> new BucketItem(IRFluids.SOURCE_SOAP_WATER,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    /**
     * Registers a new item
     *
     * @param name the item name referred to as id in-game
     * @param item the item you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the item-registry-object built from the parameters
     */
    private static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

}

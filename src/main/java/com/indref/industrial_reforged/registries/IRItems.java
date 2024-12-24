package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.items.armor.HazmatSuiteItem;
import com.indref.industrial_reforged.content.items.misc.BlueprintItem;
import com.indref.industrial_reforged.content.items.misc.FertilizerItem;
import com.indref.industrial_reforged.content.items.reactor.UraniumFuelRodItem;
import com.indref.industrial_reforged.content.items.storage.BatteryItem;
import com.indref.industrial_reforged.content.items.storage.FluidCellItem;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.content.items.tools.*;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IRItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IndustrialReforged.MODID);
    public static final List<DeferredItem<BlockItem>> BLOCK_ITEMS = new ArrayList<>();
    public static final List<DeferredItem<?>> TAB_ITEMS = new ArrayList<>();
    public static final Map<DeferredItem<?>, CastingMoldValue> MOLD_ITEMS = new HashMap<>();

    // Tools
    public static final DeferredItem<WrenchItem> WRENCH = toolItem("wrench", WrenchItem::new);
    public static final DeferredItem<TreeTapItem> TREE_TAP = toolItem("tree_tap", TreeTapItem::new);
    public static final DeferredItem<HammerItem> HAMMER = toolItem("hammer", HammerItem::new);
    public static final DeferredItem<ThermometerItem> THERMOMETER = toolItem("thermometer", ThermometerItem::new);
    public static final DeferredItem<NanoSaberItem> NANO_SABER = registerItem("nano_saber",
            () -> new NanoSaberItem(new Item.Properties(), EnergyTiers.HIGH));
    public static final DeferredItem<ScannerItem> SCANNER = registerItem("scanner",
            () -> new ScannerItem(new Item.Properties(), EnergyTiers.MEDIUM));
//    public static final DeferredItem<ElectricWrenchItem> ELECTRIC_WRENCH = registerItem("electric_wrench",
//            () -> new ElectricWrenchItem(new Item.Properties(), EnergyTiers.LOW));
//    public static final DeferredItem<ElectricHoeItem> ELECTRIC_HOE = registerItem("electric_hoe",
//            () -> new ElectricHoeItem(EnergyTiers.LOW, Tiers.IRON, 0, 0, new Item.Properties()));
//    public static final DeferredItem<ElectricTreeTapItem> ELECTRIC_TREE_TAP = registerItem("electric_tree_tap",
//            () -> new ElectricTreeTapItem(new Item.Properties(), EnergyTiers.LOW));
    public static final DeferredItem<RockCutterItem> ROCK_CUTTER = registerItem("rock_cutter",
            () -> new RockCutterItem(1, -2.8F, EnergyTiers.LOW, Tiers.IRON, new Item.Properties()));
    public static final DeferredItem<ElectricDrillItem> ELECTRIC_DRILL = registerItem("electric_drill",
            () -> new ElectricDrillItem(1, -2.8F, 54, EnergyTiers.LOW, Tiers.GOLD, new Item.Properties()));
    public static final DeferredItem<ElectricDrillItem> ADVANCED_DRILL = registerItem("advanced_drill",
            () -> new ElectricDrillItem(1, -2.8F, 96, EnergyTiers.HIGH, Tiers.DIAMOND, new Item.Properties()));
//    public static final DeferredItem<TapeMeasureItem> TAPE_MEASURE = registerItem("tape_measure",
//            () -> new TapeMeasureItem(new Item.Properties()));
    public static final DeferredItem<BlueprintItem> BLUEPRINT = registerItem("blueprint",
            () -> new BlueprintItem(new Item.Properties()));

    // Item storages
    public static final DeferredItem<FluidCellItem> FLUID_CELL = registerItem("fluid_cell",
            () -> new FluidCellItem(new Item.Properties().stacksTo(16), 1000));
    public static final DeferredItem<Item> ANTENNA = registerStandardItem("antenna");
    public static final DeferredItem<Item> ELECTRIC_MOTOR = registerItem("electric_motor",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CIRCUIT_BOARD = registerItem("circuit_board",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BASIC_CIRCUIT = registerStandardItem("basic_circuit");
    public static final DeferredItem<Item> ADVANCED_CIRCUIT = registerStandardItem("advanced_circuit");
    public static final DeferredItem<Item> ULTIMATE_CIRCUIT = registerStandardItem("ultimate_circuit");
    public static final DeferredItem<Item> PLANT_BALL = registerStandardItem("plant_ball");
    public static final DeferredItem<BatteryItem> BASIC_BATTERY = registerItem("basic_battery",
            () -> new BatteryItem(new Item.Properties(), EnergyTiers.LOW, 6));
    public static final DeferredItem<BatteryItem> ADVANCED_BATTERY = registerItem("advanced_battery",
            () -> new BatteryItem(new Item.Properties(), EnergyTiers.HIGH, 8));
    public static final DeferredItem<BatteryItem> ULTIMATE_BATTERY = registerItem("ultimate_battery",
            () -> new BatteryItem(new Item.Properties(), EnergyTiers.INSANE, 9));
    public static final DyedItemColor EMPTY_COLOR = new DyedItemColor(FastColor.ARGB32.color(255, 255, 255), false);
    public static final DeferredItem<ToolboxItem> TOOLBOX = registerItem("toolbox",
            () -> new ToolboxItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.DYED_COLOR, EMPTY_COLOR)));
    public static final DeferredItem<UraniumFuelRodItem> URANIUM_FUEL_ROD = registerItem("uranium_fuel_rod",
            () -> new UraniumFuelRodItem(new Item.Properties().stacksTo(1)));

    // armor
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_BOOTS = registerItem("hazmat_boots",
            () -> new HazmatSuiteItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_LEGGINGS = registerItem("hazmat_leggings",
            () -> new HazmatSuiteItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_CHESTPLATE = registerItem("hazmat_chestplate",
            () -> new HazmatSuiteItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_HELMET = registerItem("hazmat_helmet",
            () -> new HazmatSuiteItem(ArmorItem.Type.HELMET, new Item.Properties()));
//    public static final DeferredItem<JetpackItem> JETPACK = registerItem("jetpack",
//            () -> new JetpackItem(ArmorMaterials.IRON, new Item.Properties()));

    //misc
    public static final DeferredItem<Item> RUBBER_SHEET = registerStandardItem("rubber_sheet");
    public static final DeferredItem<Item> RUBBER = registerStandardItem("rubber");
    public static final DeferredItem<Item> SANDY_BRICK = registerStandardItem("sandy_brick");
    public static final DeferredItem<Item> STICKY_RESIN = registerStandardItem("sticky_resin");
    public static final DeferredItem<FertilizerItem> FERTILIZER = registerItem("fertilizer",
            () -> new FertilizerItem(new Item.Properties()));
    public static final DeferredItem<Item> CLAY_MOLD_BLANK = registerStandardItem("clay_mold");
    public static final DeferredItem<Item> CLAY_MOLD_INGOT = moldItem("ingot", 111, true);
    public static final DeferredItem<Item> CLAY_MOLD_WIRE = moldItem("wire", 37, true);

    //ores
    public static final DeferredItem<Item> RAW_BAUXITE = registerStandardItem("raw_bauxite");
    public static final DeferredItem<Item> RAW_CHROMIUM = registerStandardItem("raw_chromium");
    public static final DeferredItem<Item> RAW_IRIDIUM = registerStandardItem("raw_iridium");
    public static final DeferredItem<Item> RAW_LEAD = registerStandardItem("raw_lead");
    public static final DeferredItem<Item> RAW_NICKEL = registerStandardItem("raw_nickel");
    public static final DeferredItem<Item> RAW_TIN = registerStandardItem("raw_tin");
    public static final DeferredItem<Item> RAW_URANIUM = registerStandardItem("raw_uranium");

    // ingots
    public static final DeferredItem<Item> ALUMINUM_INGOT = registerStandardItem("aluminum_ingot");
    public static final DeferredItem<Item> TITANIUM_INGOT = registerStandardItem("titanium_ingot");
    public static final DeferredItem<Item> CHROMIUM_INGOT = registerStandardItem("chromium_ingot");
    public static final DeferredItem<Item> IRIDIUM_INGOT = registerStandardItem("iridium_ingot");
    public static final DeferredItem<Item> LEAD_INGOT = registerStandardItem("lead_ingot");
    public static final DeferredItem<Item> NICKEL_INGOT = registerStandardItem("nickel_ingot");
    public static final DeferredItem<Item> TIN_INGOT = registerStandardItem("tin_ingot");
    public static final DeferredItem<Item> URANIUM_INGOT = registerStandardItem("uranium_ingot");
    public static final DeferredItem<Item> STEEL_INGOT = registerStandardItem("steel_ingot");

    // Plates
    public static final DeferredItem<Item> IRON_PLATE = registerStandardItem("iron_plate");
    public static final DeferredItem<Item> COPPER_PLATE = registerStandardItem("copper_plate");
    public static final DeferredItem<Item> STEEL_PLATE = registerStandardItem("steel_plate");
    public static final DeferredItem<Item> TIN_PLATE = registerStandardItem("tin_plate");

    // Rods
    public static final DeferredItem<Item> IRON_ROD = registerStandardItem("iron_rod");
    public static final DeferredItem<Item> STEEL_ROD = registerStandardItem("steel_rod");

    // Wires
    public static final DeferredItem<Item> TIN_WIRE = registerStandardItem("tin_wire");
    public static final DeferredItem<Item> COPPER_WIRE = registerStandardItem("copper_wire");
    public static final DeferredItem<Item> GOLD_WIRE = registerStandardItem("gold_wire");
    public static final DeferredItem<Item> STEEL_WIRE = registerStandardItem("steel_wire");

    // Wires
    public static final DeferredItem<Item> STEEL_DUST = registerStandardItem("steel_dust");
    public static final DeferredItem<Item> COPPER_DUST = registerStandardItem("copper_dust");

    static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item) {
        DeferredItem<T> deferredItem = ITEMS.register(name, item);
        TAB_ITEMS.add(deferredItem);
        return deferredItem;
    }

    private static DeferredItem<Item> registerStandardItem(String name) {
        DeferredItem<Item> deferredItem = ITEMS.registerSimpleItem(name);
        TAB_ITEMS.add(deferredItem);
        return deferredItem;
    }

    private static DeferredItem<Item> moldItem(String moldType, int capacity, boolean consumeCast) {
        DeferredItem<Item> item = registerStandardItem("clay_mold_" + moldType);
        MOLD_ITEMS.put(item, new CastingMoldValue(capacity, consumeCast));
        return item;
    }

    private static <T extends Item> DeferredItem<T> toolItem(String name, Function<Item.Properties, T> itemConstructor) {
        return registerItem(name, () -> itemConstructor.apply(new Item.Properties().stacksTo(1)));
    }

}

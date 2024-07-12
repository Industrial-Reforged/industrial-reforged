package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.items.armor.HazmatSuiteItem;
import com.indref.industrial_reforged.registries.items.armor.JetpackItem;
import com.indref.industrial_reforged.registries.items.misc.BlueprintItem;
import com.indref.industrial_reforged.registries.items.misc.FertilizerItem;
import com.indref.industrial_reforged.registries.items.misc.MiningPipeBlockItem;
import com.indref.industrial_reforged.registries.items.misc.MoldItem;
import com.indref.industrial_reforged.registries.items.reactor.UraniumFuelRodItem;
import com.indref.industrial_reforged.registries.items.storage.BatteryItem;
import com.indref.industrial_reforged.registries.items.storage.FluidCellItem;
import com.indref.industrial_reforged.registries.items.storage.ToolboxItem;
import com.indref.industrial_reforged.registries.items.tools.*;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public final class IRItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IndustrialReforged.MODID);

    // Tools
    public static final DeferredItem<WrenchItem> WRENCH = registerItem("wrench",
            () -> new WrenchItem(new Item.Properties()));
    public static final DeferredItem<HammerItem> HAMMER = registerItem("hammer",
            () -> new HammerItem(new Item.Properties()));
    public static final DeferredItem<ThermometerItem> THERMOMETER = registerItem("thermometer",
            () -> new ThermometerItem(new Item.Properties()));
    public static final DeferredItem<TreeTapItem> TREE_TAP = registerItem("tree_tap",
            () -> new TreeTapItem(new Item.Properties()));
    public static final DeferredItem<NanoSaberItem> NANO_SABER = registerItem("nano_saber",
            () -> new NanoSaberItem(new Item.Properties(), EnergyTiers.HIGH));
    public static final DeferredItem<ScannerItem> SCANNER = registerItem("scanner",
            () -> new ScannerItem(new Item.Properties(), EnergyTiers.MEDIUM));
    public static final DeferredItem<ElectricWrenchItem> ELECTRIC_WRENCH = registerItem("electric_wrench",
            () -> new ElectricWrenchItem(new Item.Properties(), EnergyTiers.LOW));
    public static final DeferredItem<ElectricHoeItem> ELECTRIC_HOE = registerItem("electric_hoe",
            () -> new ElectricHoeItem(EnergyTiers.LOW, Tiers.IRON, 0, 0, new Item.Properties()));
    public static final DeferredItem<RockCutterItem> ROCK_CUTTER = registerItem("rock_cutter",
            () -> new RockCutterItem(1, -2.8F, 54, EnergyTiers.LOW, Tiers.IRON, new Item.Properties()));
    public static final DeferredItem<ElectricTreeTapItem> ELECTRIC_TREE_TAP = registerItem("electric_tree_tap",
            () -> new ElectricTreeTapItem(new Item.Properties(), EnergyTiers.LOW));
    public static final DeferredItem<ElectricDrillItem> ELECTRIC_DRILL = registerItem("electric_drill",
            () -> new ElectricDrillItem(1, -2.8F, 54, EnergyTiers.LOW, Tiers.IRON, new Item.Properties()));
    public static final DeferredItem<ElectricDrillItem> ADVANCED_DRILL = registerItem("advanced_drill",
            () -> new ElectricDrillItem(1, -2.8F, 96, EnergyTiers.HIGH, Tiers.DIAMOND, new Item.Properties()));
    public static final DeferredItem<TapeMeasureItem> TAPE_MEASURE = registerItem("tape_measure",
            () -> new TapeMeasureItem(new Item.Properties()));
    public static final DeferredItem<BlueprintItem> BLUEPRINT = registerItem("blueprint",
            () -> new BlueprintItem(new Item.Properties()));

    // Item storages
    //public static final Supplier<Item> LUNCH_BAG = registerItem("lunch_bag",
    //        () -> new LunchBagItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<FluidCellItem> FLUID_CELL = registerItem("fluid_cell",
            () -> new FluidCellItem(new Item.Properties().stacksTo(16), 1000));
    public static final DeferredItem<Item> ANTENNA = registerStandardItem("antenna");
    public static final DeferredItem<Item> PLANT_BALL = registerStandardItem("plant_ball");
    public static final DeferredItem<BatteryItem> BASIC_BATTERY = registerItem("basic_battery",
            () -> new BatteryItem(new Item.Properties(), EnergyTiers.LOW));
    public static final DeferredItem<BatteryItem> ADVANCED_BATTERY = registerItem("advanced_battery",
            () -> new BatteryItem(new Item.Properties(), EnergyTiers.HIGH));
    public static final DeferredItem<BatteryItem> ULTIMATE_BATTERY = registerItem("ultimate_battery",
            () -> new BatteryItem(new Item.Properties(), EnergyTiers.INSANE));
    public static final DeferredItem<ToolboxItem> TOOLBOX = registerItem("toolbox",
            () -> new ToolboxItem(new Item.Properties().stacksTo(1)));
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
    public static final DeferredItem<JetpackItem> JETPACK = registerItem("jetpack",
            () -> new JetpackItem(ArmorMaterials.IRON, new Item.Properties()));

    //misc
    public static final DeferredItem<Item> RUBBER_SHEET = registerStandardItem("rubber_sheet");
    public static final DeferredItem<Item> BASIC_CIRCUIT = registerStandardItem("basic_circuit");
    public static final DeferredItem<Item> ADVANCED_CIRCUIT = registerStandardItem("advanced_circuit");
    public static final DeferredItem<Item> ULTIMATE_CIRCUIT = registerStandardItem("ultimate_circuit");
    public static final DeferredItem<Item> BIOMASS = registerStandardItem("biomass");
    public static final DeferredItem<Item> STICKY_RESIN = registerStandardItem("sticky_resin");
    public static final DeferredItem<FertilizerItem> FERTILIZER = registerItem("fertilizer",
            () -> new FertilizerItem(new Item.Properties()));
    public static final DeferredItem<MoldItem> CLAY_MOLD_BLANK = registerItem("clay_mold", MoldItem::new);
    public static final DeferredItem<MoldItem> CLAY_MOLD_INGOT = registerItem("clay_mold_ingot", MoldItem::new);

    // Needs to be registered manual for custom placement
    public static final DeferredItem<Item> MINING_PIPE = registerItem("mining_pipe",
            () -> new MiningPipeBlockItem(new Item.Properties()));

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

    // Wires
    public static final DeferredItem<Item> COPPER_WIRE = registerStandardItem("copper_wire");

    public static final DeferredItem<BucketItem> SOAP_WATER_BUCKET = registerItem("soap_water_bucket",
            () -> new BucketItem(IRFluids.SOURCE_SOAP_WATER.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<BucketItem> MOLTEN_STEEL_BUCKET = registerItem("molten_steel_bucket",
            () -> new BucketItem(IRFluids.MOLTEN_STEEL_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)) {
                @Override
                public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
                    tooltip.add(Component.literal("Only for testing").withStyle(ChatFormatting.RED));
                }
            });

    /**
     * Registers a new item
     *
     * @param name the item name referred to as id in-game
     * @param item the item you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the item-registry-object built from the parameters
     */
    private static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private static DeferredItem<Item> registerStandardItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

}

package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.UpgradeItem;
import com.indref.industrial_reforged.content.items.GuideItem;
import com.indref.industrial_reforged.content.items.SimpleUpgradeItem;
import com.indref.industrial_reforged.content.items.armor.HazmatSuiteItem;
import com.indref.industrial_reforged.content.items.CastingScrapsItem;
import com.indref.industrial_reforged.content.items.FertilizerItem;
import com.indref.industrial_reforged.content.items.armor.JetpackItem;
import com.indref.industrial_reforged.content.items.storage.BatteryItem;
import com.indref.industrial_reforged.content.items.storage.FluidCellItem;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.content.items.tools.*;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.util.SingleFluidStack;
import com.indref.industrial_reforged.util.tabs.ItemTabOrdering;
import com.indref.industrial_reforged.util.tabs.TabOrdering;
import com.indref.industrial_reforged.util.tabs.TabPosition;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.indref.industrial_reforged.IRFeatureFlags.WIP_FLAG;

public final class IRItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IndustrialReforged.MODID);
    public static final List<Supplier<BlockItem>> BLOCK_ITEMS = new ArrayList<>();
    public static final Map<TabOrdering, Map<Integer, DeferredItem<?>>> TAB_ITEMS = new HashMap<>();
    public static final Map<DeferredItem<?>, CastingMoldValue> MOLD_ITEMS = new LinkedHashMap<>();

    // primitive tools
    public static final DeferredItem<WrenchItem> WRENCH = primitiveToolItem("wrench", WrenchItem::new);
    public static final DeferredItem<TreeTapItem> TREE_TAP = primitiveToolItem("tree_tap", TreeTapItem::new);
    public static final DeferredItem<HammerItem> HAMMER = primitiveToolItem("hammer", HammerItem::new);
    public static final DeferredItem<ToolboxItem> TOOLBOX = toolBoxItem("toolbox",
            () -> new ToolboxItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.DYED_COLOR, new DyedItemColor(FastColor.ARGB32.color(255, 255, 255), true))));

    // electric tools
    // low
    public static final DeferredItem<ElectricTreeTapItem> ELECTRIC_TREE_TAP = electricToolItem("electric_tree_tap",
            () -> new ElectricTreeTapItem(new Item.Properties(), IREnergyTiers.LOW, () -> IRConfig.electricTreeTapEnergyUsage, () -> IRConfig.electricTreeTapCapacity));
    public static final DeferredItem<ElectricHoeItem> ELECTRIC_HOE = electricToolItem("electric_hoe",
            () -> new ElectricHoeItem(new Item.Properties(), Tiers.IRON, 1, -2.8F, IREnergyTiers.LOW, () -> IRConfig.electricHoeEnergyUsage, () -> IRConfig.electricHoeCapacity));
    public static final DeferredItem<ElectricDrillItem> BASIC_DRILL = electricToolItem("basic_drill",
            () -> new ElectricDrillItem(new Item.Properties(), -2.8F, 1, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.basicDrillEnergyUsage, () -> IRConfig.basicDrillCapacity));
    public static final DeferredItem<ElectricChainsawItem> BASIC_CHAINSAW = electricToolItem("basic_chainsaw",
            () -> new ElectricChainsawItem(new Item.Properties(), -2.8F, 5, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.basicChainsawEnergyUsage, () -> IRConfig.basicChainsawCapacity));
    public static final DeferredItem<RockCutterItem> ROCK_CUTTER = electricToolItem("rock_cutter",
            () -> new RockCutterItem(new Item.Properties(), -2.8F, 1, Tiers.IRON, IREnergyTiers.LOW, () -> IRConfig.rockCutterEnergyUsage, () -> IRConfig.rockCutterCapacity));
    public static final DeferredItem<ElectricDrillItem> ADVANCED_DRILL = electricToolItem("advanced_drill",
            () -> new ElectricDrillItem(new Item.Properties().requiredFeatures(WIP_FLAG), -2.8F, 1, Tiers.DIAMOND, IREnergyTiers.HIGH, () -> IRConfig.advancedDrillEnergyUsage, () -> IRConfig.advancedDrillCapacity));
    public static final DeferredItem<ElectricChainsawItem> ADVANCED_CHAINSAW = electricToolItem("advanced_chainsaw",
            () -> new ElectricChainsawItem(new Item.Properties().requiredFeatures(WIP_FLAG), 7, -2.8F, Tiers.DIAMOND, IREnergyTiers.HIGH, () -> IRConfig.advancedChainsawEnergyUsage, () -> IRConfig.advancedChainsawCapacity));
    // medium
    //public static final DeferredItem<ScannerItem> SCANNER = electricToolItem("scanner",
    //        () -> new ScannerItem(new Item.Properties(), IREnergyTiers.MEDIUM, () -> IRConfig.scannerEnergyUsage, () -> IRConfig.scannerCapacity));
    // high
    public static final DeferredItem<NanoSaberItem> NANO_SABER = electricToolItem("nano_saber",
            () -> new NanoSaberItem(new Item.Properties().requiredFeatures(WIP_FLAG), Tiers.DIAMOND, -1, -3F, IREnergyTiers.HIGH, () -> IRConfig.nanoSaberEnergyUsage, () -> IRConfig.nanoSaberCapacity));

    // Item storages
    public static final DeferredItem<BatteryItem> BASIC_BATTERY = batteryItem("basic_battery",
            () -> new BatteryItem(new Item.Properties(), IREnergyTiers.LOW, () -> IRConfig.basicBatteryCapacity, 6));
    public static final DeferredItem<BatteryItem> ADVANCED_BATTERY = batteryItem("advanced_battery",
            () -> new BatteryItem(new Item.Properties().requiredFeatures(WIP_FLAG), IREnergyTiers.HIGH, () -> IRConfig.advancedBatteryCapacity, 8));
    public static final DeferredItem<BatteryItem> ULTIMATE_BATTERY = batteryItem("ultimate_battery",
            () -> new BatteryItem(new Item.Properties().requiredFeatures(WIP_FLAG), IREnergyTiers.INSANE, () -> IRConfig.ultimateBatteryCapacity, 9));
    public static final DeferredItem<FluidCellItem> FLUID_CELL = fluidCellItem("fluid_cell",
            () -> new FluidCellItem(new Item.Properties().stacksTo(16), () -> IRConfig.fluidCellCapacity));

    public static final DeferredItem<Item> ANTENNA = electricComponent("antenna");
    public static final DeferredItem<Item> ELECTRIC_MOTOR = electricComponent("electric_motor");
    public static final DeferredItem<Item> CIRCUIT_BOARD = registerStandardItem("circuit_board", ItemTabOrdering.CIRCUITS.withPosition(0));
    public static final DeferredItem<Item> BASIC_CIRCUIT = circuitItem("basic_circuit");
    public static final DeferredItem<Item> ADVANCED_CIRCUIT = circuitItem("advanced_circuit", true);
    public static final DeferredItem<Item> ULTIMATE_CIRCUIT = circuitItem("ultimate_circuit", true);

    // Upgrades
    public static final DeferredItem<SimpleUpgradeItem> OVERCLOCKER_UPGRADE = upgradeItem("overclocker_upgrade", () -> new SimpleUpgradeItem(new Item.Properties(), IRUpgrades.OVERCLOCKER_UPGRADE));

    // Guide book
    public static final DeferredItem<GuideItem> GUIDE = guideItem("guide", GuideItem::new);;

    // armor
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_BOOTS = registerItem("hazmat_boots",
            () -> new HazmatSuiteItem(ArmorItem.Type.BOOTS, new Item.Properties()), calculateTabPosition(ItemTabOrdering.REGULAR_ARMOR));
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_LEGGINGS = registerItem("hazmat_leggings",
            () -> new HazmatSuiteItem(ArmorItem.Type.LEGGINGS, new Item.Properties().requiredFeatures(WIP_FLAG)), calculateTabPosition(ItemTabOrdering.REGULAR_ARMOR));
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_CHESTPLATE = registerItem("hazmat_chestplate",
            () -> new HazmatSuiteItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().requiredFeatures(WIP_FLAG)), calculateTabPosition(ItemTabOrdering.REGULAR_ARMOR));
    public static final DeferredItem<HazmatSuiteItem> HAZMAT_HELMET = registerItem("hazmat_helmet",
            () -> new HazmatSuiteItem(ArmorItem.Type.HELMET, new Item.Properties().requiredFeatures(WIP_FLAG)), calculateTabPosition(ItemTabOrdering.REGULAR_ARMOR));
    public static final DeferredItem<JetpackItem> JETPACK = registerItem("jetpack",
            () -> new JetpackItem(new Item.Properties(), IRArmorMaterials.JETPACK, IREnergyTiers.MEDIUM), calculateTabPosition(ItemTabOrdering.BASIC_ELECTRIC_ARMOR));

    // primitive components
    public static final DeferredItem<Item> STICKY_RESIN = primitiveComponent("sticky_resin");
    public static final DeferredItem<Item> RUBBER = primitiveComponent("rubber");
    public static final DeferredItem<Item> PLANT_BALL = primitiveComponent("plant_ball");
    public static final DeferredItem<Item> PLANT_MASS = primitiveComponent("plant_mass");
    public static final DeferredItem<Item> SANDY_BRICK = primitiveComponent("sandy_brick");
    public static final DeferredItem<Item> TERRACOTTA_BRICK = primitiveComponent("terracotta_brick");

    // misc items
    public static final DeferredItem<FertilizerItem> FERTILIZER = miscItem("fertilizer", FertilizerItem::new);

    // casting molds
    // clay
    public static final DeferredItem<Item> CLAY_MOLD_BLANK = registerStandardItem("clay_mold", ItemTabOrdering.CASTING_MOLDS.withPosition(0));
    public static final DeferredItem<Item> CLAY_MOLD_INGOT = clayMoldItem("ingot", 111, true);
    public static final DeferredItem<Item> CLAY_MOLD_PLATE = clayMoldItem("plate", 111, true);
    public static final DeferredItem<Item> CLAY_MOLD_WIRE = clayMoldItem("wire", 37, true);
    public static final DeferredItem<Item> CLAY_MOLD_ROD = clayMoldItem("rod", 111, true);
    // steel
    public static final DeferredItem<Item> STEEL_MOLD_INGOT = steelMoldItem("ingot", 111, false);
    public static final DeferredItem<Item> STEEL_MOLD_PLATE = steelMoldItem("plate", 111, false);
    public static final DeferredItem<Item> STEEL_MOLD_WIRE = steelMoldItem("wire", 37, false);
    public static final DeferredItem<Item> STEEL_MOLD_ROD = steelMoldItem("rod", 111, false);

    public static final DeferredItem<Item> CASTING_SCRAPS = registerItem("casting_scraps",
            () -> new CastingScrapsItem(new Item.Properties().component(IRDataComponents.SINGLE_FLUID, SingleFluidStack.EMPTY)), ItemTabOrdering.noPosition());

    //ores
    public static final DeferredItem<Item> RAW_BAUXITE = rawOreItem("bauxite");
    public static final DeferredItem<Item> RAW_CHROMIUM = rawOreItem("chromium");
    public static final DeferredItem<Item> RAW_IRIDIUM = rawOreItem("iridium");
    public static final DeferredItem<Item> RAW_LEAD = rawOreItem("lead");
    public static final DeferredItem<Item> RAW_NICKEL = rawOreItem("nickel");
    public static final DeferredItem<Item> RAW_TIN = rawOreItem("tin");
    public static final DeferredItem<Item> RAW_URANIUM = rawOreItem("uranium");

    // ingots
    public static final DeferredItem<Item> ALUMINUM_INGOT = ingotItem("aluminum");
    public static final DeferredItem<Item> TITANIUM_INGOT = ingotItem("titanium");
    public static final DeferredItem<Item> CHROMIUM_INGOT = ingotItem("chromium");
    public static final DeferredItem<Item> IRIDIUM_INGOT = ingotItem("iridium");
    public static final DeferredItem<Item> LEAD_INGOT = ingotItem("lead");
    public static final DeferredItem<Item> NICKEL_INGOT = ingotItem("nickel");
    public static final DeferredItem<Item> TIN_INGOT = ingotItem("tin");
    public static final DeferredItem<Item> URANIUM_INGOT = ingotItem("uranium");
    public static final DeferredItem<Item> STEEL_INGOT = ingotItem("steel");

    // Plates
    public static final DeferredItem<Item> ALUMINUM_PLATE = plateItem("aluminum");
    public static final DeferredItem<Item> IRON_PLATE = plateItem("iron");
    public static final DeferredItem<Item> COPPER_PLATE = plateItem("copper");
    public static final DeferredItem<Item> STEEL_PLATE = plateItem("steel");
    public static final DeferredItem<Item> TIN_PLATE = plateItem("tin");
    public static final DeferredItem<Item> CARBON_PLATE = plateItem("carbon");

    // Dusts
    public static final DeferredItem<Item> COAL_DUST = dustItem("coal");
    public static final DeferredItem<Item> STEEL_DUST = dustItem("steel");
    public static final DeferredItem<Item> COPPER_DUST = dustItem("copper");

    // Rods
    public static final DeferredItem<Item> IRON_ROD = rodItem("iron");
    public static final DeferredItem<Item> STEEL_ROD = rodItem("steel");

    // Wires
    public static final DeferredItem<Item> TIN_WIRE = wireItem("tin");
    public static final DeferredItem<Item> COPPER_WIRE = wireItem("copper");
    public static final DeferredItem<Item> GOLD_WIRE = wireItem("gold");
    public static final DeferredItem<Item> STEEL_WIRE = wireItem("steel");

    static DeferredItem<Item> primitiveComponent(String name) {
        return registerStandardItem(name, calculateTabPosition(ItemTabOrdering.PRIMITIVE_COMPONENTS));
    }

    static DeferredItem<Item> electricComponent(String name) {
        return registerStandardItem(name, calculateTabPosition(ItemTabOrdering.ELECTRIC_COMPONENTS));
    }

    static DeferredItem<Item> circuitItem(String name) {
        return registerStandardItem(name, calculateTabPosition(ItemTabOrdering.CIRCUITS));
    }

    static DeferredItem<Item> circuitItem(String name, boolean wip) {
        return registerStandardItem(name, calculateTabPosition(ItemTabOrdering.CIRCUITS), wip);
    }

    static <T extends Item & UpgradeItem> DeferredItem<T> upgradeItem(String name, Supplier<T> itemSupplier) {
        return registerItem(name, itemSupplier, calculateTabPosition(ItemTabOrdering.UPGRADES));
    }

    static <T extends Item> DeferredItem<T> guideItem(String name, Function<Item.Properties, T> itemConstructor) {
        boolean guideMeLoaded = ModList.get().isLoaded("guideme");
        return registerItem(name, () -> itemConstructor.apply(new Item.Properties()), guideMeLoaded ? calculateTabPosition(ItemTabOrdering.BASIC_ELECTRIC_TOOLS) : ItemTabOrdering.noPosition());
    }

    static <T extends Item> DeferredItem<T> miscItem(String name, Function<Item.Properties, T> itemConstructor) {
        return registerItem(name, () -> itemConstructor.apply(new Item.Properties()), calculateTabPosition(ItemTabOrdering.MISC_ITEMS));
    }

    static DeferredItem<Item> rawOreItem(String postfix) {
        return registerStandardItem("raw_" + postfix, calculateTabPosition(ItemTabOrdering.RAW_ORES));
    }

    static DeferredItem<Item> ingotItem(String prefix) {
        return registerStandardItem(prefix + "_ingot", calculateTabPosition(ItemTabOrdering.INGOTS));
    }

    static DeferredItem<Item> plateItem(String prefix) {
        return registerStandardItem(prefix + "_plate", calculateTabPosition(ItemTabOrdering.PLATES));
    }

    static DeferredItem<Item> dustItem(String prefix) {
        return registerStandardItem(prefix + "_dust", calculateTabPosition(ItemTabOrdering.DUSTS));
    }

    static DeferredItem<Item> wireItem(String prefix) {
        return registerStandardItem(prefix + "_wire", calculateTabPosition(ItemTabOrdering.WIRES));
    }

    static DeferredItem<Item> rodItem(String prefix) {
        return registerStandardItem(prefix + "_rod", calculateTabPosition(ItemTabOrdering.RODS));
    }

    static DeferredItem<Item> clayMoldItem(String moldType, int capacity, boolean consumeCast) {
        DeferredItem<Item> item = registerStandardItem("clay_mold_" + moldType, calculateTabPosition(ItemTabOrdering.CASTING_MOLDS));
        MOLD_ITEMS.put(item, new CastingMoldValue(capacity, consumeCast));
        return item;
    }

    static DeferredItem<Item> steelMoldItem(String moldType, int capacity, boolean consumeCast) {
        DeferredItem<Item> item = registerStandardItem("steel_mold_" + moldType, calculateTabPosition(ItemTabOrdering.CASTING_MOLDS));
        MOLD_ITEMS.put(item, new CastingMoldValue(capacity, consumeCast));
        return item;
    }

    static <T extends Item> DeferredItem<T> primitiveToolItem(String name, Function<Item.Properties, T> itemConstructor) {
        return registerItem(name, () -> itemConstructor.apply(new Item.Properties().stacksTo(1)), calculateTabPosition(ItemTabOrdering.PRIMITIVE_TOOLS));
    }

    static DeferredItem<ToolboxItem> toolBoxItem(String name, Supplier<ToolboxItem> itemSupplier) {
        return registerItem(name, itemSupplier, calculateTabPosition(ItemTabOrdering.TOOL_BOX));
    }

    static <T extends Item> DeferredItem<T> electricToolItem(String name, Supplier<T> itemSupplier) {
        return registerItem(name, itemSupplier, calculateTabPosition(ItemTabOrdering.BASIC_ELECTRIC_TOOLS));
    }

    static DeferredItem<BatteryItem> batteryItem(String name, Supplier<BatteryItem> itemSupplier) {
        return registerItem(name, itemSupplier, calculateTabPosition(ItemTabOrdering.BATTERIES));
    }

    static DeferredItem<FluidCellItem> fluidCellItem(String name, Supplier<FluidCellItem> itemSupplier) {
        return registerItem(name, itemSupplier, calculateTabPosition(ItemTabOrdering.FLUID_CELLS));
    }

    static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item, TabPosition position) {
        DeferredItem<T> deferredItem = ITEMS.register(name, item);
        putTabItem(position, deferredItem);
        return deferredItem;
    }

    static DeferredItem<Item> registerStandardItem(String name, TabPosition position, boolean wip) {
        DeferredItem<Item> deferredItem = ITEMS.register(name, () -> new Item(wip ? new Item.Properties().requiredFeatures(WIP_FLAG) : new Item.Properties()));
        putTabItem(position, deferredItem);
        return deferredItem;
    }

    static DeferredItem<Item> registerStandardItem(String name, TabPosition position) {
        DeferredItem<Item> deferredItem = ITEMS.registerSimpleItem(name);
        putTabItem(position, deferredItem);
        return deferredItem;
    }

    // helper methods

    private static void putTabItem(TabPosition position, DeferredItem<?> deferredItem) {
        TAB_ITEMS.computeIfAbsent(position.orderingCategory(), k -> new Int2ObjectOpenHashMap<>())
                .put(position.categoryPosition(), deferredItem);
    }

    private static TabPosition calculateTabPosition(ItemTabOrdering tabOrdering) {
        return tabOrdering.withPosition(TAB_ITEMS.getOrDefault(tabOrdering, Collections.emptyMap()).size());
    }

}

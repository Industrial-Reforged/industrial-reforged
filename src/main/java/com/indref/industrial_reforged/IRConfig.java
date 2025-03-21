package com.indref.industrial_reforged;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class IRConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Block Energy Capacity
    private static final ModConfigSpec.IntValue BASIC_GENERATOR_ENERGY_CAPACITY =
            blockEnergyCapacity("basic_generator", "Basic Generator", 4_000);
    private static final ModConfigSpec.IntValue CENTRIFUGE_ENERGY_CAPACITY =
            blockEnergyCapacity("centrifuge", "Centrifuge", 4_000);

    // Block Fluid Capacity
    private static final ModConfigSpec.IntValue CRUCIBLE_FLUID_CAPACITY =
            blockFluidCapacity("crucible", "Crucible", 9_000);
    private static final ModConfigSpec.IntValue BLAST_FURNACE_FLUID_CAPACITY =
            blockFluidCapacity("blast_furnace", "Blast Furnace", 9_000);
    private static final ModConfigSpec.IntValue DRAIN_FLUID_CAPACITY =
            blockFluidCapacity("drain", "Drain", 1_000);
    private static final ModConfigSpec.IntValue CENTRIFUGE_FLUID_CAPACITY =
            blockFluidCapacity("centrifuge", "Centrifuge", 1_000);

    // Item Energy Capacity
    // LOW
    private static final ModConfigSpec.IntValue BASIC_DRILL_ENERGY_CAPACITY =
            itemEnergyCapacity("basic_drill", "Basic Drill", 4_000);
    private static final ModConfigSpec.IntValue BASIC_CHAINSAW_ENERGY_CAPACITY =
            itemEnergyCapacity("basic_chainsaw", "Basic Chainsaw", 4_000);
    private static final ModConfigSpec.IntValue BASIC_BATTERY_ENERGY_CAPACITY =
            itemEnergyCapacity("basic_battery", "Basic Battery", 4_000);
    private static final ModConfigSpec.IntValue ELECTRIC_HOE_ENERGY_CAPACITY =
            itemEnergyCapacity("electric_hoe", "Electric Hoe", 4_000);
    private static final ModConfigSpec.IntValue ELECTRIC_TREE_TAP_ENERGY_CAPACITY =
            itemEnergyCapacity("electric_tree_tap", "Electric Tree Tap", 4_000);
    private static final ModConfigSpec.IntValue ROCK_CUTTER_ENERGY_CAPACITY =
            itemEnergyCapacity("rock_cutter", "Rock Cutter", 4_000);

    // MEDIUM
    private static final ModConfigSpec.IntValue SCANNER_ENERGY_CAPACITY =
            itemEnergyCapacity("scanner", "Scanner", 4_000);

    // HIGH
    private static final ModConfigSpec.IntValue NANO_SABER_ENERGY_CAPACITY =
            itemEnergyCapacity("nano_saber", "Nano Saber", 4_000);
    private static final ModConfigSpec.IntValue ADVANCED_DRILL_ENERGY_CAPACITY =
            itemEnergyCapacity("advanced_drill", "Advanced Drill", 4_000);
    private static final ModConfigSpec.IntValue ADVANCED_CHAINSAW_ENERGY_CAPACITY =
            itemEnergyCapacity("advanced_chainsaw", "Advanced Chainsaw", 4_000);
    private static final ModConfigSpec.IntValue ADVANCED_BATTERY_ENERGY_CAPACITY =
            itemEnergyCapacity("advanced_battery", "Advanced Battery", 4_000);

    // INSANE
    private static final ModConfigSpec.IntValue ULTIMATE_BATTERY_ENERGY_CAPACITY =
            itemEnergyCapacity("ultimate_battery", "Ultimate Battery", 4_000);

    // Item Fluid Capacity
    private static final ModConfigSpec.IntValue FLUID_CELL_FLUID_CAPACITY =
            itemFluidCapacity("fluid_cell", "Fluid Cell", 4_000);

    // Block Heat Production
    private static final ModConfigSpec.DoubleValue SMALL_FIREBOX_PRODUCTION =
            heatProduction("small_firebox", "Small Firebox", 0.85);

    private static final ModConfigSpec.DoubleValue FIREBOX_PRODUCTION =
            heatProduction("firebox", "Firebox", 0.85);

    // Block Heat Decay
    private static final ModConfigSpec.DoubleValue SMALL_FIREBOX_DECAY =
            heatDecay("small_firebox", "Small Firebox", 0.22);
    private static final ModConfigSpec.DoubleValue FIREBOX_DECAY =
            heatDecay("firebox", "Firebox", 0.22);
    private static final ModConfigSpec.DoubleValue BLAST_FURNACE_DECAY =
            heatDecay("blast_furnace", "Blast Furnace", 0.22);
    private static final ModConfigSpec.DoubleValue CRUCIBLE_DECAY =
            heatDecay("crucible", "Crucible", 0.22);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int basicGeneratorEnergyCapacity;
    public static int centrifugeEnergyCapacity;

    public static int crucibleFluidCapacity;
    public static int blastFurnaceFluidCapacity;
    public static int drainFluidCapacity;
    public static int centrifugeFluidCapacity;

    public static int basicDrillCapacity;
    public static int basicChainsawCapacity;
    public static int basicBatteryCapacity;
    public static int electricHoeCapacity;
    public static int electricTreeTapCapacity;
    public static int rockCutterCapacity;
    public static int scannerCapacity;
    public static int nanoSaberCapacity;
    public static int advancedDrillCapacity;
    public static int advancedChainsawCapacity;
    public static int advancedBatteryCapacity;
    public static int ultimateBatteryCapacity;

    public static int fluidCellCapacity;

    public static float smallFireboxHeatProduction;
    public static float fireboxHeatProduction;

    public static float smallFireboxHeatDecay;
    public static float fireboxHeatDecay;
    public static float crucibleHeatDecay;
    public static float blastFurnaceHeatDecay;

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        basicGeneratorEnergyCapacity = BASIC_GENERATOR_ENERGY_CAPACITY.getAsInt();
        centrifugeEnergyCapacity = CENTRIFUGE_ENERGY_CAPACITY.getAsInt();

        crucibleFluidCapacity = CRUCIBLE_FLUID_CAPACITY.getAsInt();
        blastFurnaceFluidCapacity = BLAST_FURNACE_FLUID_CAPACITY.getAsInt();
        drainFluidCapacity = DRAIN_FLUID_CAPACITY.getAsInt();
        centrifugeFluidCapacity = CENTRIFUGE_FLUID_CAPACITY.getAsInt();

        basicDrillCapacity = BASIC_DRILL_ENERGY_CAPACITY.getAsInt();
        basicChainsawCapacity = BASIC_CHAINSAW_ENERGY_CAPACITY.getAsInt();
        basicBatteryCapacity = BASIC_BATTERY_ENERGY_CAPACITY.getAsInt();
        electricHoeCapacity = ELECTRIC_HOE_ENERGY_CAPACITY.getAsInt();
        electricTreeTapCapacity = ELECTRIC_TREE_TAP_ENERGY_CAPACITY.getAsInt();
        rockCutterCapacity = ROCK_CUTTER_ENERGY_CAPACITY.getAsInt();
        scannerCapacity = SCANNER_ENERGY_CAPACITY.getAsInt();
        nanoSaberCapacity = NANO_SABER_ENERGY_CAPACITY.getAsInt();
        advancedDrillCapacity = ADVANCED_DRILL_ENERGY_CAPACITY.getAsInt();
        advancedChainsawCapacity = ADVANCED_CHAINSAW_ENERGY_CAPACITY.getAsInt();
        advancedBatteryCapacity = ADVANCED_BATTERY_ENERGY_CAPACITY.getAsInt();
        ultimateBatteryCapacity = ULTIMATE_BATTERY_ENERGY_CAPACITY.getAsInt();

        fluidCellCapacity = FLUID_CELL_FLUID_CAPACITY.getAsInt();

        smallFireboxHeatProduction = (float) SMALL_FIREBOX_PRODUCTION.getAsDouble();
        fireboxHeatProduction = (float) FIREBOX_PRODUCTION.getAsDouble();

        smallFireboxHeatDecay = (float) SMALL_FIREBOX_DECAY.getAsDouble();
        fireboxHeatDecay = (float) FIREBOX_DECAY.getAsDouble();
        crucibleHeatDecay = (float) CRUCIBLE_DECAY.getAsDouble();
        blastFurnaceHeatDecay = (float) BLAST_FURNACE_DECAY.getAsDouble();
    }

    private static ModConfigSpec.DoubleValue heatProduction(String blockId, String name, double heatProduction) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks.heat." + blockId + "_production", heatProduction, 0.0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.DoubleValue heatDecay(String blockId, String name, double heatDecay) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks.heat." + blockId + "_decay", heatDecay, 0.0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue blockEnergyCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks.energy." + blockId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue blockFluidCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks.fluid." + blockId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue blockHeatCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks.heat." + blockId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemEnergyCapacity(String itemId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("items.energy." + itemId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemFluidCapacity(String itemId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("items.fluid." + itemId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemHeatCapacity(String itemId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("items.heat." + itemId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

}

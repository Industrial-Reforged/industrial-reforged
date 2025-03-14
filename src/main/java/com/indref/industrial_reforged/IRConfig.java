package com.indref.industrial_reforged;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class IRConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue BASIC_GENERATOR_CAPACITY =
            blockCapacity("basic_generator", "Basic Generator", 4_000);
    private static final ModConfigSpec.IntValue CENTRIFUGE_CAPACITY =
            blockCapacity("centrifuge", "Centrifuge", 4_000);

    private static final ModConfigSpec.IntValue CRUCIBLE_CAPACITY =
            blockCapacity("crucible", "Crucible", 9_000);
    private static final ModConfigSpec.IntValue BLAST_FURNACE_CAPACITY =
            blockCapacity("blast_furnace", "Blast Furnace", 9_000);
    private static final ModConfigSpec.IntValue DRAIN_CAPACITY =
            blockCapacity("drain", "Drain", 1_000);

    private static final ModConfigSpec.IntValue BASIC_DRILL_CAPACITY =
            itemCapacity("basic_drill", "Basic Drill", 4_000);
    private static final ModConfigSpec.IntValue BASIC_CHAINSAW_CAPACITY =
            itemCapacity("basic_chainsaw", "Basic Chainsaw", 4_000);
    private static final ModConfigSpec.IntValue BASIC_BATTERY_CAPACITY =
            itemCapacity("basic_battery", "Basic Battery", 4_000);
    private static final ModConfigSpec.IntValue ELECTRIC_HOE_CAPACITY =
            itemCapacity("electric_hoe", "Electric Hoe", 4_000);
    private static final ModConfigSpec.IntValue ELECTRIC_TREE_TAP_CAPACITY =
            itemCapacity("electric_tree_tap", "Electric Tree Tap", 4_000);
    private static final ModConfigSpec.IntValue ROCK_CUTTER_CAPACITY =
            itemCapacity("rock_cutter", "Rock Cutter", 4_000);

    private static final ModConfigSpec.IntValue SCANNER_CAPACITY =
            itemCapacity("scanner", "Scanner", 4_000);

    private static final ModConfigSpec.IntValue NANO_SABER_CAPACITY =
            itemCapacity("nano_saber", "Nano Saber", 4_000);
    private static final ModConfigSpec.IntValue ADVANCED_DRILL_CAPACITY =
            itemCapacity("advanced_drill", "Advanced Drill", 4_000);
    private static final ModConfigSpec.IntValue ADVANCED_CHAINSAW_CAPACITY =
            itemCapacity("advanced_chainsaw", "Advanced Chainsaw", 4_000);
    private static final ModConfigSpec.IntValue ADVANCED_BATTERY_CAPACITY =
            itemCapacity("advanced_battery", "Advanced Battery", 4_000);

    private static final ModConfigSpec.IntValue ULTIMATE_BATTERY_CAPACITY =
            itemCapacity("ultimate_battery", "Ultimate Battery", 4_000);

    private static final ModConfigSpec.IntValue FLUID_CELL_CAPACITY =
            itemCapacity("fluid_cell", "Fluid Cell", 4_000);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int basicGeneratorEnergyCapacity;
    public static int centrifugeEnergyCapacity;

    public static int crucibleFluidCapacity;
    public static int blastFurnaceFluidCapacity;
    public static int drainFluidCapacity;

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

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        basicGeneratorEnergyCapacity = BASIC_GENERATOR_CAPACITY.getAsInt();
        centrifugeEnergyCapacity = CENTRIFUGE_CAPACITY.getAsInt();

        crucibleFluidCapacity = CRUCIBLE_CAPACITY.getAsInt();
        blastFurnaceFluidCapacity = BLAST_FURNACE_CAPACITY.getAsInt();
        drainFluidCapacity = DRAIN_CAPACITY.getAsInt();

        basicDrillCapacity = BASIC_DRILL_CAPACITY.getAsInt();
        basicChainsawCapacity = BASIC_CHAINSAW_CAPACITY.getAsInt();
        basicBatteryCapacity = BASIC_BATTERY_CAPACITY.getAsInt();
        electricHoeCapacity = ELECTRIC_HOE_CAPACITY.getAsInt();
        electricTreeTapCapacity = ELECTRIC_TREE_TAP_CAPACITY.getAsInt();
        rockCutterCapacity = ROCK_CUTTER_CAPACITY.getAsInt();
        scannerCapacity = SCANNER_CAPACITY.getAsInt();
        nanoSaberCapacity = NANO_SABER_CAPACITY.getAsInt();
        advancedDrillCapacity = ADVANCED_DRILL_CAPACITY.getAsInt();
        advancedChainsawCapacity = ADVANCED_CHAINSAW_CAPACITY.getAsInt();
        advancedBatteryCapacity = ADVANCED_BATTERY_CAPACITY.getAsInt();
        ultimateBatteryCapacity = ULTIMATE_BATTERY_CAPACITY.getAsInt();

        fluidCellCapacity = FLUID_CELL_CAPACITY.getAsInt();
    }

    private static ModConfigSpec.IntValue blockCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks." + blockId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemCapacity(String itemId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("ITEMS." + itemId + "_capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

}

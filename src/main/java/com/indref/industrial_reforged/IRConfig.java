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
    private static final ModConfigSpec.IntValue BATTERY_BOX_ENERGY_CAPACITY =
            blockEnergyCapacity("battery_box", "Battery Box", 4_000);

    private static final ModConfigSpec.IntValue BASIC_GENERATOR_ENERGY_PRODUCTION =
            blockEnergyProduction("basic_generator", "Basic Generator", 4_000);

    // Block Fluid Capacity
    private static final ModConfigSpec.IntValue CRUCIBLE_FLUID_CAPACITY =
            blockFluidCapacity("crucible", "Crucible", 9_000);
    private static final ModConfigSpec.IntValue BLAST_FURNACE_FLUID_CAPACITY =
            blockFluidCapacity("blast_furnace", "Blast Furnace", 9_000);
    private static final ModConfigSpec.IntValue DRAIN_FLUID_CAPACITY =
            blockFluidCapacity("drain", "Drain", 1_000);
    private static final ModConfigSpec.IntValue CENTRIFUGE_FLUID_CAPACITY =
            blockFluidCapacity("centrifuge", "Centrifuge", 4_000);

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
            itemEnergyCapacity("scanner", "Scanner", 16_000);
    private static final ModConfigSpec.IntValue JETPACK_ENERGY_CAPACITY =
            itemEnergyCapacity("jetpack", "Jetpack", 16_000);

    // HIGH
    private static final ModConfigSpec.IntValue NANO_SABER_ENERGY_CAPACITY =
            itemEnergyCapacity("nano_saber", "Nano Saber", 32_000);
    private static final ModConfigSpec.IntValue ADVANCED_DRILL_ENERGY_CAPACITY =
            itemEnergyCapacity("advanced_drill", "Advanced Drill", 32_000);
    private static final ModConfigSpec.IntValue ADVANCED_CHAINSAW_ENERGY_CAPACITY =
            itemEnergyCapacity("advanced_chainsaw", "Advanced Chainsaw", 32_000);
    private static final ModConfigSpec.IntValue ADVANCED_BATTERY_ENERGY_CAPACITY =
            itemEnergyCapacity("advanced_battery", "Advanced Battery", 32_000);

    // INSANE
    private static final ModConfigSpec.IntValue ULTIMATE_BATTERY_ENERGY_CAPACITY =
            itemEnergyCapacity("ultimate_battery", "Ultimate Battery", 128_000);

    // Item Energy Usage
    // LOW
    private static final ModConfigSpec.IntValue BASIC_DRILL_ENERGY_USAGE =
            itemEnergyUsage("basic_drill", "Basic Drill", 16);
    private static final ModConfigSpec.IntValue BASIC_CHAINSAW_ENERGY_USAGE =
            itemEnergyUsage("basic_chainsaw", "Basic Chainsaw", 16);
    private static final ModConfigSpec.IntValue ELECTRIC_HOE_ENERGY_USAGE =
            itemEnergyUsage("electric_hoe", "Electric Hoe", 16);
    private static final ModConfigSpec.IntValue ELECTRIC_TREE_TAP_ENERGY_USAGE =
            itemEnergyUsage("electric_tree_tap", "Electric Tree Tap", 16);
    private static final ModConfigSpec.IntValue ROCK_CUTTER_ENERGY_USAGE =
            itemEnergyUsage("rock_cutter", "Rock Cutter", 16);

    // MEDIUM
    private static final ModConfigSpec.IntValue SCANNER_ENERGY_USAGE =
            itemEnergyUsage("scanner", "Scanner", 32);
    private static final ModConfigSpec.IntValue JETPACK_ENERGY_USAGE =
            itemEnergyUsage("jetpack", "Jetpack", 32);

    // HIGH
    private static final ModConfigSpec.IntValue NANO_SABER_ENERGY_USAGE =
            itemEnergyUsage("nano_saber", "Nano Saber", 64);
    private static final ModConfigSpec.IntValue ADVANCED_DRILL_ENERGY_USAGE =
            itemEnergyUsage("advanced_drill", "Advanced Drill", 64);
    private static final ModConfigSpec.IntValue ADVANCED_CHAINSAW_ENERGY_USAGE =
            itemEnergyUsage("advanced_chainsaw", "Advanced Chainsaw", 64);

    // Item Fluid Capacity
    private static final ModConfigSpec.IntValue FLUID_CELL_FLUID_CAPACITY =
            itemFluidCapacity("fluid_cell", "Fluid Cell", 4_000);

    // Block Heat Capacity
    private static final ModConfigSpec.DoubleValue SMALL_FIREBOX_HEAT_CAPACITY =
            blockHeatCapacity("small_firebox", "Small Firebox", 1_800);
    private static final ModConfigSpec.DoubleValue FIREBOX_HEAT_CAPACITY =
            blockHeatCapacity("firebox", "Firebox", 2_400);
    private static final ModConfigSpec.DoubleValue BLAST_FURNACE_HEAT_CAPACITY =
            blockHeatCapacity("blast_furnace", "Blast Furnace", 1_800);
    private static final ModConfigSpec.DoubleValue CRUCIBLE_HEAT_CAPACITY =
            blockHeatCapacity("crucible", "Crucible", 2_400);

    // Block Heat Production
    private static final ModConfigSpec.DoubleValue SMALL_FIREBOX_PRODUCTION =
            heatProduction("small_firebox", "Small Firebox", 2.45);

    private static final ModConfigSpec.DoubleValue FIREBOX_PRODUCTION =
            heatProduction("firebox", "Firebox", 0.85);

    // Block Heat Decay
    private static final ModConfigSpec.DoubleValue SMALL_FIREBOX_DECAY =
            heatDecay("small_firebox", "Small Firebox", 0.42);
    private static final ModConfigSpec.DoubleValue FIREBOX_DECAY =
            heatDecay("firebox", "Firebox", 0.42);
    private static final ModConfigSpec.DoubleValue BLAST_FURNACE_DECAY =
            heatDecay("blast_furnace", "Blast Furnace", 0.42);
    private static final ModConfigSpec.DoubleValue CRUCIBLE_DECAY =
            heatDecay("crucible", "Crucible", 0.42);

    private static final ModConfigSpec.DoubleValue NANO_SABER_ATTACK_SPEED = BUILDER
            .comment("The Nano Saber's attack speed when activated")
            .defineInRange("items.nano_saber_attack_speed", 1.2, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue NANO_SABER_ATTACK_DAMAGE = BUILDER
            .comment("The Nano Saber's attack damage when activated")
            .defineInRange("items.nano_saber_attack_damage", 19, 0, Integer.MAX_VALUE);

    // Item Animations
    private static final ModConfigSpec.BooleanValue CHAINSAW_ITEM_ANIMATION =
            itemAnimation("chainsaw", "Chainsaw", true);
    private static final ModConfigSpec.BooleanValue DRILL_ITEM_ANIMATION =
            itemAnimation("drill", "Drill", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int basicGeneratorEnergyCapacity;
    public static int centrifugeEnergyCapacity;
    public static int batteryBoxEnergyCapacity;

    public static int basicGeneratorEnergyProduction;

    public static int crucibleFluidCapacity;
    public static int blastFurnaceFluidCapacity;
    public static int drainFluidCapacity;
    public static int centrifugeFluidCapacity;

    public static float smallFireboxHeatCapacity;
    public static float fireboxHeatCapacity;
    public static float crucibleHeatCapacity;
    public static float blastFurnaceHeatCapacity;

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
    public static int jetpackCapacity;

    public static int basicDrillEnergyUsage;
    public static int basicChainsawEnergyUsage;
    public static int electricHoeEnergyUsage;
    public static int electricTreeTapEnergyUsage;
    public static int rockCutterEnergyUsage;
    public static int scannerEnergyUsage;
    public static int nanoSaberEnergyUsage;
    public static int advancedDrillEnergyUsage;
    public static int advancedChainsawEnergyUsage;
    public static int jetpackEnergyUsage;

    public static int fluidCellCapacity;

    public static float smallFireboxHeatProduction;
    public static float fireboxHeatProduction;

    public static float smallFireboxHeatDecay;
    public static float fireboxHeatDecay;
    public static float crucibleHeatDecay;
    public static float blastFurnaceHeatDecay;

    public static boolean drillItemAnimation;
    public static boolean chainsawItemAnimation;

    public static float nanoSaberAttackSpeed;
    public static int nanoSaberAttackDamage;

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;

        basicGeneratorEnergyCapacity = BASIC_GENERATOR_ENERGY_CAPACITY.getAsInt();
        centrifugeEnergyCapacity = CENTRIFUGE_ENERGY_CAPACITY.getAsInt();
        batteryBoxEnergyCapacity = BATTERY_BOX_ENERGY_CAPACITY.getAsInt();

        basicGeneratorEnergyProduction = BASIC_GENERATOR_ENERGY_PRODUCTION.getAsInt();

        crucibleFluidCapacity = CRUCIBLE_FLUID_CAPACITY.getAsInt();
        blastFurnaceFluidCapacity = BLAST_FURNACE_FLUID_CAPACITY.getAsInt();
        drainFluidCapacity = DRAIN_FLUID_CAPACITY.getAsInt();
        centrifugeFluidCapacity = CENTRIFUGE_FLUID_CAPACITY.getAsInt();

        smallFireboxHeatCapacity = (float) SMALL_FIREBOX_HEAT_CAPACITY.getAsDouble();
        fireboxHeatCapacity = (float) FIREBOX_HEAT_CAPACITY.getAsDouble();
        crucibleHeatCapacity = (float) CRUCIBLE_HEAT_CAPACITY.getAsDouble();
        blastFurnaceHeatCapacity = (float) BLAST_FURNACE_HEAT_CAPACITY.getAsDouble();

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
        jetpackCapacity = JETPACK_ENERGY_CAPACITY.getAsInt();

        basicDrillEnergyUsage = BASIC_DRILL_ENERGY_USAGE.getAsInt();
        basicChainsawEnergyUsage = BASIC_CHAINSAW_ENERGY_USAGE.getAsInt();
        electricHoeEnergyUsage = ELECTRIC_HOE_ENERGY_USAGE.getAsInt();
        electricTreeTapEnergyUsage = ELECTRIC_TREE_TAP_ENERGY_USAGE.getAsInt();
        rockCutterEnergyUsage = ROCK_CUTTER_ENERGY_USAGE.getAsInt();
        scannerEnergyUsage = SCANNER_ENERGY_USAGE.getAsInt();
        nanoSaberEnergyUsage = NANO_SABER_ENERGY_USAGE.getAsInt();
        advancedDrillEnergyUsage = ADVANCED_DRILL_ENERGY_USAGE.getAsInt();
        advancedChainsawEnergyUsage = ADVANCED_CHAINSAW_ENERGY_USAGE.getAsInt();
        jetpackEnergyUsage = JETPACK_ENERGY_USAGE.getAsInt();

        fluidCellCapacity = FLUID_CELL_FLUID_CAPACITY.getAsInt();

        smallFireboxHeatProduction = (float) SMALL_FIREBOX_PRODUCTION.getAsDouble();
        fireboxHeatProduction = (float) FIREBOX_PRODUCTION.getAsDouble();

        smallFireboxHeatDecay = (float) SMALL_FIREBOX_DECAY.getAsDouble();
        fireboxHeatDecay = (float) FIREBOX_DECAY.getAsDouble();
        crucibleHeatDecay = (float) CRUCIBLE_DECAY.getAsDouble();
        blastFurnaceHeatDecay = (float) BLAST_FURNACE_DECAY.getAsDouble();

        chainsawItemAnimation = CHAINSAW_ITEM_ANIMATION.getAsBoolean();
        drillItemAnimation = DRILL_ITEM_ANIMATION.getAsBoolean();

        nanoSaberAttackDamage = NANO_SABER_ATTACK_DAMAGE.getAsInt();
        nanoSaberAttackSpeed = (float) NANO_SABER_ATTACK_SPEED.getAsDouble();
    }

    private static ModConfigSpec.DoubleValue heatProduction(String blockId, String name, double heatProduction) {
        return BUILDER
                .comment("The " + name + "'s heat production")
                .defineInRange("blocks.heat.production." + blockId, heatProduction, 0.0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.DoubleValue heatDecay(String blockId, String name, double heatDecay) {
        return BUILDER
                .comment("The " + name + "'s heat decay")
                .defineInRange("blocks.heat.decay." + blockId, heatDecay, 0.0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue blockEnergyCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s energy capacity")
                .defineInRange("blocks.energy.capacity." + blockId, defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue blockEnergyProduction(String blockId, String name, int defaultEnergyProduction) {
        return BUILDER
                .comment("The " + name + "'s energy production per tick")
                .defineInRange("blocks.energy.production." + blockId, defaultEnergyProduction, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue blockFluidCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s fluid capacity")
                .defineInRange("blocks.fluid.capacity." + blockId, defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.DoubleValue blockHeatCapacity(String blockId, String name, double defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s heat capacity")
                .defineInRange("blocks.heat.capacity" + blockId, defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemEnergyUsage(String itemId, String name, int defaultUsage) {
        return BUILDER
                .comment("The " + name + "'s energy usage")
                .defineInRange("items.energy.usage." + itemId, defaultUsage, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemEnergyCapacity(String itemId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s energy capacity")
                .defineInRange("items.energy.capacity." + itemId, defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.IntValue itemFluidCapacity(String itemId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s fluid capacity")
                .defineInRange("items.fluid.capacity." + itemId, defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.DoubleValue itemHeatCapacity(String itemId, String name, double defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s heat capacity")
                .defineInRange("items.heat.capacity." + itemId, defaultCapacity, 0, Integer.MAX_VALUE);
    }

    private static ModConfigSpec.BooleanValue itemAnimation(String itemId, String name, boolean defaultValue) {
        return BUILDER
                .comment("Whether to run " + name + "'s animation")
                .define("items.animation." + itemId + "_animation", defaultValue);
    }

}

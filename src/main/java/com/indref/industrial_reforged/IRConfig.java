package com.indref.industrial_reforged;

import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class IRConfig {
    private static final String BLOCKS_ENERGY_CAPACITY = "blocks.energy.capacity";

    @ConfigValue(key = "basic_generator", name = "Basic Generator Energy Capacity", comment = "The Energy Capacity of the Basic Generator", category = BLOCKS_ENERGY_CAPACITY)
    public static int basicGeneratorEnergyCapacity = 4000;
    @ConfigValue(key = "centrifuge", name = "Centrifuge Energy Capacity", comment = "The Energy Capacity of the Centrifuge", category = BLOCKS_ENERGY_CAPACITY)
    public static int centrifugeEnergyCapacity = 4000;
    @ConfigValue(key = "battery_box", name = "Battery Box Energy Capacity", comment = "The Energy Capacity of the Battery Box", category = BLOCKS_ENERGY_CAPACITY)
    public static int batteryBoxEnergyCapacity = 4000;

    private static final String BLOCKS_ENERGY_PRODUCTION = "blocks.energy.production";

    @ConfigValue(key = "basic_generator", name = "Basic Generator Energy Production", comment = "The amount of Energy produced by the Basic Generator", category = BLOCKS_ENERGY_PRODUCTION)
    public static int basicGeneratorEnergyProduction = 4000;

    private static final String BLOCKS_FLUID_CAPACITY = "blocks.fluid.capacity";

    @ConfigValue(key = "crucible", name = "Crucible Fluid Capacity", comment = "The Fluid Capacity of the Crucible", category = BLOCKS_FLUID_CAPACITY)
    public static int crucibleFluidCapacity = 9000;
    @ConfigValue(key = "blast_furnace", name = "Blast Furnace Fluid Capacity", comment = "The Fluid Capacity of the Blast Furnace", category = BLOCKS_FLUID_CAPACITY)
    public static int blastFurnaceFluidCapacity = 9000;
    @ConfigValue(key = "drain", name = "Drain Fluid Capacity", comment = "The Fluid Capacity of the Drain", category = BLOCKS_FLUID_CAPACITY)
    public static int drainFluidCapacity = 1000;
    @ConfigValue(key = "centrifuge", name = "Centrifuge Fluid Capacity", comment = "The Fluid Capacity of the Centrifuge", category = BLOCKS_FLUID_CAPACITY)
    public static int centrifugeFluidCapacity = 400;

    private static final String BLOCKS_HEAT_CAPACITY = "blocks.heat.capacity";

    @ConfigValue(key = "small_firebox", name = "Small Firebox Heat Capacity", comment = "The Heat Capacity of the Small Firebox", category = BLOCKS_HEAT_CAPACITY)
    public static float smallFireboxHeatCapacity = 1800;
    @ConfigValue(key = "firebox", name = "Firebox Heat Capacity", comment = "The Heat Capacity of the Firebox", category = BLOCKS_HEAT_CAPACITY)
    public static float fireboxHeatCapacity = 2400;
    @ConfigValue(key = "crucible", name = "Crucible Heat Capacity", comment = "The Heat Capacity of the Crucible", category = BLOCKS_HEAT_CAPACITY)
    public static float crucibleHeatCapacity = 1800;
    @ConfigValue(key = "blast_furnace", name = "Blast Furnace Heat Capacity", comment = "The Heat Capacity of the Blast Furnace", category = BLOCKS_HEAT_CAPACITY)
    public static float blastFurnaceHeatCapacity = 2400;

    private static final String ITEMS_ENERGY_CAPACITY = "items.energy.capacity";

    @ConfigValue(key = "basic_drill", name = "Basic Drill Energy Capacity", comment = "The Energy Capacity of the Basic Drill", category = ITEMS_ENERGY_CAPACITY)
    public static int basicDrillCapacity = 4000;
    @ConfigValue(key = "basic_chainsaw", name = "Basic Chainsaw Energy Capacity", comment = "The Energy Capacity of the Basic Chainsaw", category = ITEMS_ENERGY_CAPACITY)
    public static int basicChainsawCapacity = 4000;
    @ConfigValue(key = "basic_battery", name = "Basic Battery Energy Capacity", comment = "The Energy Capacity of the Basic Battery", category = ITEMS_ENERGY_CAPACITY)
    public static int basicBatteryCapacity = 4000;
    @ConfigValue(key = "electric_hoe", name = "Electric Hoe Energy Capacity", comment = "The Energy Capacity of the Electric Hoe", category = ITEMS_ENERGY_CAPACITY)
    public static int electricHoeCapacity = 4000;
    @ConfigValue(key = "electric_tree_tap", name = "Electric Tree Tap Energy Capacity", comment = "The Energy Capacity of the Electric Tree Tap", category = ITEMS_ENERGY_CAPACITY)
    public static int electricTreeTapCapacity = 4000;
    @ConfigValue(key = "rock_cutter", name = "Rock Cutter Energy Capacity", comment = "The Energy Capacity of the Rock Cutter", category = ITEMS_ENERGY_CAPACITY)
    public static int rockCutterCapacity = 4000;

    @ConfigValue(key = "scanner", name = "Scanner Energy Capacity", comment = "The Energy Capacity of the Rock Cutter", category = ITEMS_ENERGY_CAPACITY)
    public static int scannerCapacity = 16_000;
    @ConfigValue(key = "jetpack", name = "Jetpack Energy Capacity", comment = "The Energy Capacity of the Jetpack", category = ITEMS_ENERGY_CAPACITY)
    public static int jetpackCapacity = 16_000;

    @ConfigValue(key = "nano_saber", name = "Nano Saber Energy Capacity", comment = "The Energy Capacity of the Nano Saber", category = ITEMS_ENERGY_CAPACITY)
    public static int nanoSaberCapacity = 32_000;
    @ConfigValue(key = "advanced_drill", name = "Advanced Drill Energy Capacity", comment = "The Energy Capacity of the Advanced Drill", category = ITEMS_ENERGY_CAPACITY)
    public static int advancedDrillCapacity = 32_000;
    @ConfigValue(key = "advanced_chainsaw", name = "Advanced Chainsaw Energy Capacity", comment = "The Energy Capacity of the Advanced Chainsaw", category = ITEMS_ENERGY_CAPACITY)
    public static int advancedChainsawCapacity = 32_000;
    @ConfigValue(key = "advanced_battery", name = "Advanced Battery Energy Capacity", comment = "The Energy Capacity of the Advanced Battery", category = ITEMS_ENERGY_CAPACITY)
    public static int advancedBatteryCapacity = 32_000;

    @ConfigValue(key = "ultimate_battery", name = "Ultimate Battery Energy Capacity", comment = "The Energy Capacity of the Ultimate Battery", category = ITEMS_ENERGY_CAPACITY)
    public static int ultimateBatteryCapacity = 128_000;

    private static final String ITEMS_ENERGY_USAGE = "items.energy.usage";

    @ConfigValue(key = "basic_drill", name = "Basic Drill Energy Usage", comment = "The Energy Usage of the Basic Drill", category = ITEMS_ENERGY_USAGE)
    public static int basicDrillEnergyUsage = 16;
    @ConfigValue(key = "basic_chainsaw", name = "Basic Chainsaw Energy Usage", comment = "The Energy Usage of the Basic Chainsaw", category = ITEMS_ENERGY_USAGE)
    public static int basicChainsawEnergyUsage = 16;
    @ConfigValue(key = "electric_hoe", name = "Electric Hoe Energy Usage", comment = "The Energy Usage of the Electric Hoe", category = ITEMS_ENERGY_USAGE)
    public static int electricHoeEnergyUsage = 16;
    @ConfigValue(key = "electric_tree_tap", name = "Electric Tree Tap Energy Usage", comment = "The Energy Usage of the Electric Tree Tap", category = ITEMS_ENERGY_USAGE)
    public static int electricTreeTapEnergyUsage = 16;
    @ConfigValue(key = "rock_cutter", name = "Rock Cutter Energy Usage", comment = "The Energy Usage of the Rock Cutter", category = ITEMS_ENERGY_USAGE)
    public static int rockCutterEnergyUsage = 16;

    @ConfigValue(key = "scanner", name = "Scanner Energy Usage", comment = "The Energy Usage of the Scanner", category = ITEMS_ENERGY_USAGE)
    public static int scannerEnergyUsage = 32;
    @ConfigValue(key = "jetpack", name = "Jetpack Energy Usage", comment = "The Energy Usage of the Jetpack", category = ITEMS_ENERGY_USAGE)
    public static int jetpackEnergyUsage = 32;

    @ConfigValue(key = "nano_saber", name = "Nano Saber Energy Usage", comment = "The Energy Usage of the Nano Saber", category = ITEMS_ENERGY_USAGE)
    public static int nanoSaberEnergyUsage = 64;
    @ConfigValue(key = "advanced_drill", name = "Advanced Drill Energy Usage", comment = "The Energy Usage of the Advanced Drill", category = ITEMS_ENERGY_USAGE)
    public static int advancedDrillEnergyUsage = 64;
    @ConfigValue(key = "advanced_chainsaw", name = "Advanced Chainsaw Energy Usage", comment = "The Energy Usage of the Advanced Chainsaw", category = ITEMS_ENERGY_USAGE)
    public static int advancedChainsawEnergyUsage = 64;

    private static final String ITEMS_FLUID_CAPACITY = "items.fluid.capacity";

    @ConfigValue(key = "fluid_cell", name = "Fluid Cell Fluid Capacity", comment = "The Fluid Capacity of the Fluid Cell", category = ITEMS_FLUID_CAPACITY)
    public static int fluidCellCapacity = 4000;

    private static final String BLOCKS_HEAT_PRODUCTION = "blocks.heat.production";

    @ConfigValue(key = "small_firebox", name = "Small Firebox Heat Production", comment = "The Heat Production of the Small Firebox", category = BLOCKS_HEAT_PRODUCTION)
    public static float smallFireboxHeatProduction = 2.45F;
    @ConfigValue(key = "firebox", name = "Firebox Heat Production", comment = "The Heat Production of the Firebox", category = BLOCKS_HEAT_PRODUCTION)
    public static float fireboxHeatProduction = 0.85F;

    private static final String BLOCKS_HEAT_DECAY = "blocks.heat.decay";

    @ConfigValue(key = "small_firebox", name = "Small Firebox Heat Decay", comment = "The Heat Decay of the Small Firebox", category = BLOCKS_HEAT_DECAY)
    public static float smallFireboxHeatDecay = 0.42F;
    @ConfigValue(key = "firebox", name = "Firebox Heat Decay", comment = "The Heat Decay of the Firebox", category = BLOCKS_HEAT_DECAY)
    public static float fireboxHeatDecay = 0.42F;
    @ConfigValue(key = "crucible", name = "Crucible Heat Decay", comment = "The Heat Decay of the Crucible", category = BLOCKS_HEAT_DECAY)
    public static float crucibleHeatDecay = 0.42F;
    @ConfigValue(key = "blast_furnace", name = "Blast Furnace Heat Decay", comment = "The Heat Decay of the Blast Furnace", category = BLOCKS_HEAT_DECAY)
    public static float blastFurnaceHeatDecay = 0.42F;

    @ConfigValue(name = "Play Drill Item Animation", comment = "Whether the Drill items should play an animation")
    public static boolean drillItemAnimation = true;
    @ConfigValue(name = "Play Chainsaw Item Animation", comment = "Whether the Chainsaw items should play an animation")
    public static boolean chainsawItemAnimation = true;

    @ConfigValue(name = "Nano Saber Attack Speed", comment = "The Attack Speed of the Nano Saber when activated")
    public static float nanoSaberAttackSpeed = 1.2F;
    @ConfigValue(name = "Nano Saber Attack Damage", comment = "The Attack Damage of the Nano Saber when activated")
    public static int nanoSaberAttackDamage = 19;

    public static final String OVERCLOCKER_UPGRADE_CATEGORY = "upgrade.overclocker";

    @ConfigValue(key = "speed", name = "Overclocker Upgrade Speed", comment = "How much the overclocker upgrade speeds up a machine. For example 0.1 = 10%", category = OVERCLOCKER_UPGRADE_CATEGORY)
    public static float overclockerUpgradeSpeed = 0.45F;
    @ConfigValue(key = "energy", name = "Overclocker Upgrade Energy", comment = "How much more energy a machine with the overclocker upgrade consumes. For example 0.1 = 10%", category = OVERCLOCKER_UPGRADE_CATEGORY)
    public static float overclockerUpgradeEnergy = 0.6F;
}

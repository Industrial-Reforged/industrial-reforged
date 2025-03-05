package com.indref.industrial_reforged;

import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredBlock;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.MOD)
public class IRConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue BASIC_GENERATOR_CAPACITY =
            blockCapacity("basic_generator", "Basic Generator", EnergyTiers.LOW.value().defaultCapacity());
    private static final ModConfigSpec.IntValue CENTRIFUGE_CAPACITY =
            blockCapacity("centrifuge", "Centrifuge", EnergyTiers.LOW.value().defaultCapacity());

    private static final ModConfigSpec.IntValue CRUCIBLE_CAPACITY =
            blockCapacity("crucible", "Crucible", 9_000);
    private static final ModConfigSpec.IntValue BLAST_FURNACE_CAPACITY =
            blockCapacity("blast_furnace", "Blast Furnace", 9_000);
    private static final ModConfigSpec.IntValue DRAIN_CAPACITY =
            blockCapacity("drain", "Drain", 1_000);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int basicGeneratorEnergyCapacity;
    public static int centrifugeEnergyCapacity;

    public static int crucibleFluidCapacity;
    public static int blastFurnaceFluidCapacity;
    public static int drainFluidCapacity;

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        basicGeneratorEnergyCapacity = BASIC_GENERATOR_CAPACITY.getAsInt();
        centrifugeEnergyCapacity = CENTRIFUGE_CAPACITY.getAsInt();

        crucibleFluidCapacity = CRUCIBLE_CAPACITY.getAsInt();
        blastFurnaceFluidCapacity = BLAST_FURNACE_CAPACITY.getAsInt();
        drainFluidCapacity = DRAIN_CAPACITY.getAsInt();
    }

    private static ModConfigSpec.IntValue blockCapacity(String blockId, String name, int defaultCapacity) {
        return BUILDER
                .comment("The " + name + "'s capacity")
                .defineInRange("blocks." + blockId + ".capacity", defaultCapacity, 0, Integer.MAX_VALUE);
    }

}

package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class IRTranslations {
    public static final class Tooltip {
        public static final String ACTIVE = "active";
        public static final String INACTIVE = "inactive";
        public static final String ENERGY_STORED = "energy.stored";
        public static final String ENERGY_TIER = "energy.tier";
        public static final String FLUID_STORED = "fluid.stored";
        public static final String FLUID_AMOUNT = "fluid.amount";
        public static final String HEAT_STORED = "heat.stored";

        public static final String MELTING_PROGRESS = "melting_progress";
        public static final String MELTING_NOT_POSSIBLE = "melting_not_possible";

        // returns full translation key
        public static String translationKey(String tooltip) {
            return "tooltip." + IndustrialReforged.MODID + "." + tooltip;
        }

        public static MutableComponent translatableComponent(String tooltip) {
            return Component.translatable(translationKey(tooltip));
        }
    }

    public static final class MultiblockFeedback {
        public static final String FAILED_TO_CONSTRUCT = "failed_to_construct";
        public static final String ACTUAL_BLOCK = "actual_block";
        public static final String EXPECTED_BLOCK = "expected_block";
        public static final String BLOCK_POS = "block_pos";

        // returns full translation key
        public static String translationKey(String tooltip) {
            return "multiblock_feedback." + IndustrialReforged.MODID + "." + tooltip;
        }

        public static MutableComponent translatableComponent(String tooltip, Object... args) {
            return Component.translatable(translationKey(tooltip), args);
        }
    }
}

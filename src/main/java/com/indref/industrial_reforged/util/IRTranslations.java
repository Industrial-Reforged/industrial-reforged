package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class IRTranslations {
    public static final class Tooltip {
        public static final TranslatableConstant ACTIVE = create("active");
        public static final TranslatableConstant INACTIVE = create("inactive");
        public static final TranslatableConstant ENERGY_STORED = create("energy.stored");
        public static final TranslatableConstant ENERGY_TIER = create("energy.tier");
        public static final TranslatableConstant FLUID_STORED = create("fluid.stored");
        public static final TranslatableConstant FLUID_AMOUNT = create("fluid.amount");
        public static final TranslatableConstant HEAT_STORED = create("heat.stored");

        public static final TranslatableConstant MELTING_NOT_POSSIBLE = create("melting_not_possible");
        public static final TranslatableConstant MELTING_PROGRESS = create("melting_progress");

        public static final TranslatableConstant LIQUID_AMOUNT = create("liquid.amount_with_capacity");

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "tooltip");
        }
    }

    public static final class MultiblockFeedback {
        public static final TranslatableConstant FAILED_TO_CONSTRUCT = create("failed_to_construct");
        public static final TranslatableConstant ACTUAL_BLOCK = create("actual_block");
        public static final TranslatableConstant EXPECTED_BLOCK = create("expected_block");
        public static final TranslatableConstant BLOCK_POS = create("block_pos");

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "multiblock_feedback");
        }
    }

    public record TranslatableConstant(String key, String category) {
        public String key() {
            return category + "." + IndustrialReforged.MODID + "." + key;
        }

        public MutableComponent component(Object ...args) {
            return Component.translatable(key(), args);
        }

        @Override
        public String toString() {
            return key;
        }
    }
}

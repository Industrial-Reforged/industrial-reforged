package com.indref.industrial_reforged.translations;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashMap;
import java.util.Map;

public final class IRTranslations {
    public static final Map<String, String> TRANSLATIONS = new HashMap<>();

    public static final class General {
        public static final TranslatableConstant FLUID_UNIT = create("unit.fluid", "mb");
        public static final TranslatableConstant ENERGY_UNIT = create("unit.energy", "EU");
        public static final TranslatableConstant HEAT_UNIT = create("unit.heat", "°C");

        public static final TranslatableConstant ENERGY_NAME = create("name.energy", "Energy");
        public static final TranslatableConstant HEAT_NAME = create("name.heat", "Heat");
        public static final TranslatableConstant FLUID_NAME = create("name.fluid", "Fluid");

        private static TranslatableConstant create(String key, String defaultValue) {
            TranslatableConstant constant = create(key);
            TRANSLATIONS.put(constant.key(), defaultValue);
            return constant;
        }

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "general");
        }

        private static void init() {
        }
    }

    public static final class Tooltip {
        public static final TranslatableConstant ACTIVE = create("active", "Active");
        public static final TranslatableConstant INACTIVE = create("inactive", "Inactive");

        public static final TranslatableConstant ENERGY_STORED = create("energy.stored", "Stored: ");
        public static final TranslatableConstant ENERGY_TIER = create("energy.tier", "Tier: ");
        public static final TranslatableConstant ENERGY_AMOUNT = create("energy.amount", "%d");
        public static final TranslatableConstant ENERGY_AMOUNT_WITH_CAPACITY = create("energy.amount_with_capacity", "%d/%d");

        public static final TranslatableConstant FLUID_TYPE = create("fluid.type", "Fluid: ");
        public static final TranslatableConstant FLUID_STORED = create("fluid.stored", "Stored: ");
        public static final TranslatableConstant FLUID_AMOUNT = create("fluid.amount", "%d");
        public static final TranslatableConstant FLUID_AMOUNT_WITH_CAPACITY = create("fluid.amount_with_capacity", "%d/%d");
        public static final TranslatableConstant EMPTY_FLUID = create("empty_fluid", "Alt + Shift Click to empty");

        public static final TranslatableConstant HEAT_STORED = create("heat.stored", "Stored: ");
        public static final TranslatableConstant HEAT_AMOUNT = create("heat.amount", "%.1f");
        public static final TranslatableConstant HEAT_AMOUNT_WITH_CAPACITY = create("heat.amount_with_capacity", "%.1f/%.1f");

        public static final TranslatableConstant MELTING_NOT_POSSIBLE = create("melting.not_possible", "Melting not possible");
        public static final TranslatableConstant MELTING_PROGRESS = create("melting.progress", "%.1f/%.1f");

        public static final TranslatableConstant MULTIBLOCK_HINT = create("multiblock.hint", "This is a multiblock, look at the Blueprint for building instructions");
        public static final TranslatableConstant CASTING_MOLD = create("casting_mold", "Hold SHIFT and Scroll + Right Click to select the Casting Mold");

        private static TranslatableConstant create(String key, String defaultValue) {
            TranslatableConstant constant = create(key);
            TRANSLATIONS.put(constant.key(), defaultValue);
            return constant;
        }

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "tooltip");
        }

        private static void init() {
        }
    }

    public static final class Jei {
        public static final TranslatableConstant CRUCIBLE_SMELTING = create("crucible", "Crucible Smelting");
        public static final TranslatableConstant BLAST_FURNACE = create("blast_furnace", "Blast Furnace");
        public static final TranslatableConstant CENTRIFUGE = create("centrifuge", "Centrifuge");
        public static final TranslatableConstant CASTING = create("casting", "Casting");
        public static final TranslatableConstant ENERGY_USAGE = create("energy.usage", "%s/t: %d");
        public static final TranslatableConstant ITEM_CONSUMED = create("item.consumed", "Item is consumed");

        private static TranslatableConstant create(String key, String defaultValue) {
            TranslatableConstant constant = create(key);
            TRANSLATIONS.put(constant.key(), defaultValue);
            return constant;
        }

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "jei");
        }

        private static void init() {
        }
    }

    public static final class Menus {
        public static final TranslatableConstant CRAFTING_STATION = create("crafting_station", "Crafting Station");
        public static final TranslatableConstant FIREBOX = create("firebox", "Firebox");
        public static final TranslatableConstant CRUCIBLE = create("crucible", "Crucible");
        public static final TranslatableConstant SMALL_FIREBOX = create("small_firebox", "Small Firebox");
        public static final TranslatableConstant BLAST_FURNACE = create("blast_furnace", "Blast Furnace");
        public static final TranslatableConstant BASIC_GENERATOR = create("basic_generator", "Basic Generator");
        public static final TranslatableConstant CENTRIFUGE = create("centrifuge", "Centrifuge");
        public static final TranslatableConstant BLUEPRINT = create("blueprint", "Blueprint");

        private static TranslatableConstant create(String key, String defaultValue) {
            TranslatableConstant constant = create(key);
            TRANSLATIONS.put(constant.key(), defaultValue);
            return constant;
        }

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "menu");
        }

        private static void init() {
        }
    }

    public static void init() {
        General.init();
        Tabs.init();
        Tooltip.init();
        Menus.init();
        Jei.init();
    }

    public static final class Tabs {
        public static final TranslatableConstant BLOCKS = create("blocks", "Industrial Reforged Blocks");
        public static final TranslatableConstant ITEMS = create("items", "Industrial Reforged Items");

        private static TranslatableConstant create(String key, String defaultValue) {
            TranslatableConstant constant = create(key);
            TRANSLATIONS.put(constant.key(), defaultValue);
            return constant;
        }

        private static TranslatableConstant create(String key) {
            return new TranslatableConstant(key, "creative_tab");
        }

        private static void init() {
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

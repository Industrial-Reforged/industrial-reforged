package com.indref.industrial_reforged.util.tabs;

public enum ItemTabOrdering implements TabOrdering {
    PRIMITIVE_TOOLS,
    REGULAR_ARMOR,
    PRIMITIVE_COMPONENTS,
    MISC_ITEMS,
    CASTING_MOLDS,
    BASIC_ELECTRIC_TOOLS,
    ELECTRIC_COMPONENTS,
    CIRCUITS,
    BATTERIES,
    RAW_ORES,
    INGOTS,
    DUSTS,
    PLATES,
    WIRES,
    RODS,
    NONE(-1);

    private final int priority;

    ItemTabOrdering(int priority) {
        this.priority = priority;
    }

    ItemTabOrdering() {
        this.priority = ordinal();
    }

    @Override
    public boolean isNone() {
        return this == NONE;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    public static TabPosition noPosition() {
        return NONE.withPosition(-1);
    }
}

package com.indref.industrial_reforged.util.tabs;

public enum BlockTabOrdering implements TabOrdering {
    PRIMITIVE_MACHINES,
    BASIC_MACHINES,
    CABLES,
    BRICKS,
    HATCHES_COILS,
    FAUCET,
    CASTING_BASIN,
    MISC_BLOCKS,
    RUBBER_TREES,
    ORES,
    METALS,
    NONE(-1);

    private final int priority;

    BlockTabOrdering(int priority) {
        this.priority = priority;
    }

    BlockTabOrdering() {
        this.priority = ordinal();
    }

    @Override
    public boolean isNone() {
        return this == NONE;
    }

    public int getPriority() {
        return this.priority;
    }

    public static TabPosition empty() {
        return NONE.withPosition(-1);
    }
}

package com.indref.industrial_reforged.util.tabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.function.TriConsumer;

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
        this.priority = this.ordinal();
    }

    @Override
    public boolean isNone() {
        return this == NONE;
    }

    @Override
    public TriConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output, ItemLike> tabAppendFunction() {
        return (params, output, item) -> output.accept(item);
    }

    public int getPriority() {
        return this.priority;
    }

    public static TabPosition empty() {
        return NONE.withPosition(-1);
    }
}

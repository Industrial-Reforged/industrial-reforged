package com.indref.industrial_reforged.util.tabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.function.TriConsumer;

public interface TabOrdering {
    int getPriority();

    boolean isNone();

    TriConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output, ItemLike> tabAppendFunction();

    default TabPosition withPosition(int position) {
        return new TabPosition(this, position);
    }
}

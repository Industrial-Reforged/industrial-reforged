package com.indref.industrial_reforged.util.tabs;

public interface TabOrdering {
    int getPriority();

    boolean isNone();

    default TabPosition withPosition(int position) {
        return new TabPosition(this, position);
    }
}

package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.IToolItem;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;

public class ScannerItem extends SimpleElectricItem implements IToolItem {
    public ScannerItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getCapacity() {
        return 10000;
    }
}

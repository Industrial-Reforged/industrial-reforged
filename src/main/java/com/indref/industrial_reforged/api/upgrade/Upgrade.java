package com.indref.industrial_reforged.api.upgrade;

import com.indref.industrial_reforged.api.blockentities.UpgradeBlockEntity;
import com.indref.industrial_reforged.api.items.UpgradeItem;

public interface Upgrade {
    UpgradeItem getUpgradeItem();

    void init(UpgradeBlockEntity blockEntity);

}

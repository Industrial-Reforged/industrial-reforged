package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.item.UpgradeItemHandler;
import com.indref.industrial_reforged.api.upgrade.Upgrade;

import java.util.Set;

public interface UpgradeBlockEntity {
    UpgradeItemHandler getUpgradeItemHandler();

    Set<Upgrade> getSupportedUpgrades();

}

package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.item.UpgradeItemHandler;
import com.indref.industrial_reforged.api.upgrade.Upgrade;

import java.util.Set;
import java.util.function.Supplier;

public interface UpgradeBlockEntity {
    UpgradeItemHandler getUpgradeItemHandler();

    Set<Supplier<Upgrade>> getSupportedUpgrades();

    boolean hasUpgrade(Supplier<Upgrade> upgrade);

    void onUpgradeAdded(Supplier<Upgrade> upgrade);

    void onUpgradeRemoved(Supplier<Upgrade> upgrade);

}

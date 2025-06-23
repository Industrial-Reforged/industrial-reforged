package com.indref.industrial_reforged.content.upgrades;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.blockentities.UpgradeBlockEntity;
import com.indref.industrial_reforged.api.items.UpgradeItem;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRUpgrades;

public class OverclockerUpgrade implements Upgrade {
    @Override
    public UpgradeItem getUpgradeItem() {
        return IRItems.OVERCLOCKER_UPGRADE.get();
    }

    @Override
    public void init(UpgradeBlockEntity blockEntity) {
        if (blockEntity instanceof MachineBlockEntity machineBE) {
            int overclockerCount = blockEntity.getUpgradeAmount(IRUpgrades.OVERCLOCKER_UPGRADE);

            double speedMultiplier = 1 + IRConfig.overclockerUpgradeSpeed;
            machineBE.setProgressIncrement((float) Math.pow(speedMultiplier, overclockerCount));

            double energyMultiplier = 1 + IRConfig.overclockerUpgradeEnergy;
            machineBE.setEnergyDecrement((float) Math.pow(energyMultiplier, overclockerCount));
        }
    }
}

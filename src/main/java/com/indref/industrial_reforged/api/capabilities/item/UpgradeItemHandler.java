package com.indref.industrial_reforged.api.capabilities.item;

import com.indref.industrial_reforged.api.items.UpgradeItem;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Set;

public class UpgradeItemHandler extends ItemStackHandler {
    private final Set<Upgrade> supportedUpgrades;

    public UpgradeItemHandler(int slots, Set<Upgrade> supportedUpgrades) {
        super(slots);
        this.supportedUpgrades = supportedUpgrades;
    }

    public UpgradeItemHandler(Set<Upgrade> supportedUpgrades) {
        this(4, supportedUpgrades);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem upgradeItem && this.supportedUpgrades.contains(upgradeItem.getUpgrade());
    }

    @Override
    public int getSlotLimit(int slot) {
        return 4;
    }
}

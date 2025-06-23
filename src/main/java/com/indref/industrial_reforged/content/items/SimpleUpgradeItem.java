package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.items.UpgradeItem;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SimpleUpgradeItem extends Item implements UpgradeItem {
    private final Supplier<Upgrade> upgradeSupplier;

    public SimpleUpgradeItem(Properties properties, Supplier<Upgrade> upgradeSupplier) {
        super(properties);
        this.upgradeSupplier = upgradeSupplier;
    }

    @Override
    public Supplier<Upgrade> getUpgrade() {
        return this.upgradeSupplier;
    }
}

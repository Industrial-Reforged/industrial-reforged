package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
import com.indref.industrial_reforged.api.items.ToolItem;
import net.minecraft.world.item.ItemStack;

public class ScannerItem extends ToolItem implements IEnergyItem {
    public ScannerItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getMaxEnergy() {
        return 10000;
    }
}

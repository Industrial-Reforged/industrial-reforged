package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.items.IToolItem;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import net.minecraft.world.item.ItemStack;

public class ScannerItem extends SimpleElectricItem implements IToolItem {
    public ScannerItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getCapacity(ItemStack itemStack) {
        return 10000;
    }
}

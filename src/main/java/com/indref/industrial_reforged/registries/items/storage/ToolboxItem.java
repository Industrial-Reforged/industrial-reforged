package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleItem;
import net.minecraft.world.item.ItemStack;

//TODO: Refactor this into api and use caps
public class ToolboxItem extends AdvancedBundleItem {
    public ToolboxItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getSlots(ItemStack itemStack) {
        return 1;
    }
}

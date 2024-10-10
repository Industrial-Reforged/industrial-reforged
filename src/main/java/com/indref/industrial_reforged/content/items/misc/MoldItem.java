package com.indref.industrial_reforged.registries.items.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// TODO: Migrate instanceof checks for this to use tags instead
public class MoldItem extends Item {
    public MoldItem(Properties p_41383_) {
        super(p_41383_);
    }

    public MoldItem() {
        this(new Properties());
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}

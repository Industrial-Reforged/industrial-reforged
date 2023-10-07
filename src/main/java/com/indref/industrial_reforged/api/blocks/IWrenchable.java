package com.indref.industrial_reforged.api.blocks;

import net.minecraft.world.item.Item;

public interface IWrenchable {
    default Item getDropItem() {
        return null;
    }
}

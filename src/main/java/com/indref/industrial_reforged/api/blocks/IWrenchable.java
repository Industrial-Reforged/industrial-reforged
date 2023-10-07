package com.indref.industrial_reforged.api.blocks;

import net.minecraft.world.item.Item;

public interface IWrenchable {
    /**
     * If you override this method it will use a custom drop.
     * If it returns null the block itself will drop
     * @return the item that should be dropped
     */
    default Item getDropItem() {
        return null;
    }
}

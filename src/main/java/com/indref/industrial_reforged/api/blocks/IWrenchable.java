package com.indref.industrial_reforged.api.blocks;

import net.minecraft.world.item.Item;

/**
 * Implement this if you want your block to be able to be picked up by a
 * variant (inheritor) of the {@link com.indref.industrial_reforged.content.items.WrenchItem} class
 */
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

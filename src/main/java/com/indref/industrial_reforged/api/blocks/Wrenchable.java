package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.registries.items.tools.WrenchItem;
import net.minecraft.world.item.Item;

/**
 * Implement this if you want your block to be able to be picked up by a
 * variant (inheritor) of the {@link WrenchItem} class
 */
public interface Wrenchable {
    /**
     * If you override this method it will use a custom drop.
     * If it returns null the block itself will drop
     * @return the item that should be dropped
     */
    default Item getDropItem() {
        return null;
    }
}

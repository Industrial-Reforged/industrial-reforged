package com.indref.industrial_reforged.capabilities;

import com.indref.industrial_reforged.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.capabilities.energy.EnergyStorage;
import net.minecraft.world.item.ItemStack;

/**
 * Basic Capability Interface used for handling
 * methods related to the energy storage capability
 */
public class EnergyWrapper {
    public static class Item implements IEnergyStorage {
        private final ItemStack itemStack;

        public Item(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public int getEnergyStored() {
            return itemStack.getData(IRAttachmentTypes.ENERGY.get()).getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            itemStack.setData(IRAttachmentTypes.ENERGY.get(), new EnergyStorage(value));
        }
    }

}

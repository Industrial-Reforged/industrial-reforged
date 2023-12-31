package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.data.energy.EnergyStorage;
import com.indref.industrial_reforged.api.data.heat.IHeatStorage;
import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import net.minecraft.world.item.ItemStack;

public class HeatWrapper {
    public static class Item implements IHeatStorage {
        private final ItemStack itemStack;

        public Item(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public int getHeatStored() {
            return itemStack.getData(IRAttachmentTypes.ENERGY.get()).getEnergyStored();
        }

        @Override
        public void setHeatStored(int value) {
            itemStack.setData(IRAttachmentTypes.ENERGY.get(), new EnergyStorage(value));
        }
    }
}

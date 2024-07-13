package com.indref.industrial_reforged.api.capabilities.energy.storage;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.components.EnergyStorage;
import net.minecraft.world.item.ItemStack;

/**
 * Basic Capability Interface used for handling
 * methods related to the heat storage capability
 */
public record ItemEnergyWrapper(ItemStack itemStack) implements IEnergyStorage {
    public ItemEnergyWrapper(ItemStack itemStack, int initialCapacity) {
        this(itemStack);
        this.setEnergyCapacity(initialCapacity);
    }

    @Override
    public boolean tryDrainEnergy(int value) {
        return false;
    }

    @Override
    public boolean tryFillEnergy(int value) {
        return false;
    }

    @Override
    public int getEnergyStored() {
        EnergyStorage energyStorage = itemStack.get(IRDataComponents.ENERGY);
        if (energyStorage != null)
            return energyStorage.energyStored();
        else
            throw new NullPointerException("Failed to get energy component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
    }

    @Override
    public void setEnergyStored(int value) {
        itemStack.set(IRDataComponents.ENERGY, new EnergyStorage(value, getEnergyCapacity()));
    }

    @Override
    public int getEnergyCapacity() {
        EnergyStorage energyStorage = itemStack.get(IRDataComponents.ENERGY);
        if (energyStorage != null)
            return energyStorage.energyCapacity();
        else
            throw new NullPointerException("Failed to get energy component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
    }

    @Override
    public void setEnergyCapacity(int value) {
        itemStack.set(IRDataComponents.ENERGY, new EnergyStorage(getEnergyStored(), value));
    }
}

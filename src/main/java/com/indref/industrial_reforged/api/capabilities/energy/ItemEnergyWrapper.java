package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.components.EnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.world.item.ItemStack;

public record ItemEnergyWrapper(ItemStack itemStack, EnergyTier energyTier) implements IEnergyStorage {
    public ItemEnergyWrapper(ItemStack itemStack, EnergyTier energyTier, int initialCapacity) {
        this(itemStack, energyTier);
        this.setEnergyCapacity(initialCapacity);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
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

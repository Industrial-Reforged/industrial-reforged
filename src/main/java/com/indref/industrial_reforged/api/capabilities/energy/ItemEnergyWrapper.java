package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

public record ItemEnergyWrapper(ItemStack itemStack, Holder<EnergyTier> energyTier) implements IEnergyStorage {
    public ItemEnergyWrapper(ItemStack itemStack, Holder<EnergyTier> energyTier, int initialCapacity) {
        this(itemStack, energyTier);
        this.setEnergyCapacity(initialCapacity);
    }

    @Override
    public Holder<EnergyTier> getEnergyTier() {
        return energyTier;
    }

    @Override
    public int getEnergyStored() {
        ComponentEnergyStorage componentEnergyStorage = itemStack.get(IRDataComponents.ENERGY);
        if (componentEnergyStorage != null)
            return componentEnergyStorage.energyStored();
        else
            throw new NullPointerException("Failed to get energy component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
    }

    @Override
    public void setEnergyStored(int value) {
        itemStack.set(IRDataComponents.ENERGY, new ComponentEnergyStorage(value, getEnergyCapacity()));
    }

    @Override
    public int getEnergyCapacity() {
        ComponentEnergyStorage componentEnergyStorage = itemStack.get(IRDataComponents.ENERGY);
        if (componentEnergyStorage != null)
            return componentEnergyStorage.energyCapacity();
        else
            throw new NullPointerException("Failed to get energy component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
    }

    @Override
    public void setEnergyCapacity(int value) {
        itemStack.set(IRDataComponents.ENERGY, new ComponentEnergyStorage(getEnergyStored(), value));
    }
}

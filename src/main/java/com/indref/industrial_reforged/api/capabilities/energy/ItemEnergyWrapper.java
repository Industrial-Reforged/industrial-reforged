package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
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
    public void onEnergyChanged(int oldAmount) {
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            energyItem.onEnergyChanged(itemStack, oldAmount);
        }
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
        int energyStored = getEnergyStored();
        itemStack.set(IRDataComponents.ENERGY, new ComponentEnergyStorage(value, getEnergyCapacity()));
        onEnergyChanged(energyStored);
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

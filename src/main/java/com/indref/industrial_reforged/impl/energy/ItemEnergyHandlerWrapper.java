package com.indref.industrial_reforged.impl.energy;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record ItemEnergyHandlerWrapper(ItemStack itemStack, Supplier<EnergyTierImpl> energyTier) implements EnergyHandler {
    public ItemEnergyHandlerWrapper(ItemStack itemStack, Supplier<EnergyTierImpl> energyTier, int initialCapacity) {
        this(itemStack, energyTier);
        this.setEnergyCapacity(initialCapacity);
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            energyItem.initEnergyStorage(this, itemStack);
        }
    }

    @Override
    public Supplier<EnergyTierImpl> getEnergyTier() {
        return energyTier;
    }

    @Override
    public void onChanged(int oldAmount) {
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            energyItem.onEnergyChanged(itemStack, oldAmount);
        }
    }

    @Override
    public int getEnergyStored() {
        ComponentEuStorage componentEuStorage = itemStack.get(IRDataComponents.ENERGY);
        if (componentEuStorage != null)
            return componentEuStorage.energyStored();
        else
            throw new NullPointerException("Failed to get energy component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
    }

    @Override
    public void setEnergyStored(int value) {
        int energyStored = getEnergyStored();
        itemStack.set(IRDataComponents.ENERGY, new ComponentEuStorage(value, getEnergyCapacity()));
        onChanged(energyStored);
    }

    @Override
    public int getEnergyCapacity() {
        ComponentEuStorage componentEuStorage = itemStack.get(IRDataComponents.ENERGY);
        if (componentEuStorage != null)
            return componentEuStorage.energyCapacity();
        else
            throw new NullPointerException("Failed to get energy component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
    }

    @Override
    public void setEnergyCapacity(int value) {
        itemStack.set(IRDataComponents.ENERGY, new ComponentEuStorage(getEnergyStored(), value));
    }
}

package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.components.HeatStorage;
import net.minecraft.world.item.ItemStack;

public record ItemHeatWrapper(ItemStack itemStack) implements IHeatStorage {
    public ItemHeatWrapper(ItemStack itemStack, int initialCapacity) {
        this(itemStack);
        this.setHeatCapacity(initialCapacity);
    }

    @Override
    public int getHeatStored() {
        HeatStorage heatStorage = itemStack.get(IRDataComponents.HEAT);
        if (heatStorage != null)
            return heatStorage.heatStored();
        else
            throw new NullPointerException("Failed to get heat component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the heat item classes");
    }

    @Override
    public void setHeatStored(int value) {
        itemStack.set(IRDataComponents.HEAT, new HeatStorage(value, getHeatCapacity()));
    }

    @Override
    public int getHeatCapacity() {
        HeatStorage heatStorage = itemStack.get(IRDataComponents.HEAT);
        if (heatStorage != null)
            return heatStorage.heatCapacity();
        else
            throw new NullPointerException("Failed to get heat component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the heat item classes");
    }

    @Override
    public void setHeatCapacity(int value) {
        itemStack.set(IRDataComponents.HEAT, new HeatStorage(getHeatStored(), value));
    }

    @Override
    public int tryDrainHeat(int value) {
        return 0;
    }

    @Override
    public int tryFillHeat(int value) {
        return 0;
    }
}

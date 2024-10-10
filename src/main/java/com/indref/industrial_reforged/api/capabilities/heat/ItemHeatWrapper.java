package com.indref.industrial_reforged.api.capabilities.heat;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentHeatStorage;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemHeatWrapper(@NotNull ItemStack itemStack) implements IHeatStorage {
    public ItemHeatWrapper(@NotNull ItemStack itemStack, int initialCapacity) {
        this(itemStack);
        this.setHeatCapacity(initialCapacity);
    }

    @Override
    public int getHeatStored() {
        ComponentHeatStorage componentHeatStorage = itemStack.get(IRDataComponents.HEAT);
        if (componentHeatStorage != null)
            return componentHeatStorage.heatStored();
        else
            throw new NullPointerException("Failed to get heat component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the heat item classes");
    }

    @Override
    public void setHeatStored(int value) {
        itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(value, getHeatCapacity()));
    }

    @Override
    public int getHeatCapacity() {
        ComponentHeatStorage componentHeatStorage = itemStack.get(IRDataComponents.HEAT);
        if (componentHeatStorage != null)
            return componentHeatStorage.heatCapacity();
        else
            throw new NullPointerException("Failed to get heat component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the heat item classes");
    }

    @Override
    public void setHeatCapacity(int value) {
        itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(getHeatStored(), value));
    }

    @Override
    public int getMaxInput() {
        return 100;
    }

    @Override
    public int getMaxOutput() {
        return 100;
    }
}

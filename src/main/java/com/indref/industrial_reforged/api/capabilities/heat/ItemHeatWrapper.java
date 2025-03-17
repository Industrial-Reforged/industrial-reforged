package com.indref.industrial_reforged.api.capabilities.heat;

import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentHeatStorage;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemHeatWrapper(@NotNull ItemStack itemStack) implements IHeatStorage {
    public ItemHeatWrapper(@NotNull ItemStack itemStack, float initialCapacity) {
        this(itemStack);
        this.setHeatCapacity(initialCapacity);
    }

    @Override
    public void onHeatChanged(float oldAmount) {
        if (itemStack.getItem() instanceof IHeatItem heatItem) {
            heatItem.onHeatChanged(itemStack, oldAmount);
        }
    }

    @Override
    public float getHeatStored() {
        ComponentHeatStorage componentHeatStorage = getHeatStorage();
        return componentHeatStorage.heatStored();
    }

    @Override
    public void setHeatStored(float value) {
        float heatStored = getHeatStored();
        itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(value, getLastHeatStored(), getHeatCapacity()));
        setLastHeatStored(heatStored);
        onHeatChanged(heatStored);
    }

    @Override
    public float getLastHeatStored() {
        ComponentHeatStorage componentHeatStorage = getHeatStorage();
        return componentHeatStorage.lastHeatStored();
    }

    @Override
    public void setLastHeatStored(float value) {
        itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(getHeatStored(), value, getHeatCapacity()));
    }

    @Override
    public float getHeatCapacity() {
        ComponentHeatStorage componentHeatStorage = itemStack.get(IRDataComponents.HEAT);
        if (componentHeatStorage != null)
            return componentHeatStorage.heatCapacity();
        else
            throw new NullPointerException("Failed to get heat component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the heat item classes");
    }

    @Override
    public void setHeatCapacity(float value) {
        itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(getHeatStored(), getLastHeatStored(), value));
    }

    @Override
    public float getMaxInput() {
        return 100;
    }

    @Override
    public float getMaxOutput() {
        return 100;
    }

    private ComponentHeatStorage getHeatStorage() {
        ComponentHeatStorage componentHeatStorage = itemStack.get(IRDataComponents.HEAT);
        if (componentHeatStorage != null)
            return componentHeatStorage;
        else
            throw new NullPointerException("Failed to get heat component for item: "
                    + itemStack.getItem()
                    + " please add it under the item properties using .component(...) or preferably inherit one of the heat item classes");
    }
}

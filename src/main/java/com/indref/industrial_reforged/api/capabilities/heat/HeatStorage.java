package com.indref.industrial_reforged.api.capabilities.heat;

import com.indref.industrial_reforged.api.items.container.IHeatItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class HeatStorage implements IHeatStorage {
    public HeatStorage() {
    }

    public HeatStorage(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IHeatItem energyItem) {
            this.capacity = energyItem.getCapacity(itemStack);
        }
    }

    public int stored;
    public int capacity;

    private static final String NBT_KEY_HEAT_STORED = "heatStored";

    // TODO: 10/16/2023 consider removing this
    private static final String NBT_KEY_HEAT_CAPACITY = "heatCapacity";

    @Override
    public int getHeatStored() {
        return this.stored;
    }

    @Override
    public int getHeatCapacity() {
        return this.capacity;
    }

    @Override
    public void setHeatStored(int value) {
        this.stored = value;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_HEAT_STORED, this.stored);
        tag.putInt(NBT_KEY_HEAT_CAPACITY, this.capacity);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.stored = nbt.getInt(NBT_KEY_HEAT_STORED);
    }
}

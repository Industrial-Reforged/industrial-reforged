package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.HeatStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HeatWrapper {
    public record Item(ItemStack itemStack) implements IHeatStorage {
        public Item(ItemStack itemStack, int initialCapacity) {
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
    }

    public record Block(BlockEntity blockEntity) implements IHeatStorage {
        public Block(BlockEntity blockEntity, int initialCapacity) {
            this(blockEntity);
            this.setHeatCapacity(initialCapacity);
        }

        @Override
        public int getHeatStored() {
            return blockEntity.getData(IRAttachmentTypes.HEAT.get()).heatStored();
        }

        @Override
        public void setHeatStored(int value) {
            blockEntity.setData(IRAttachmentTypes.HEAT.get(), new HeatStorage(value, getHeatCapacity()));
        }

        @Override
        public int getHeatCapacity() {
            return blockEntity.getData(IRAttachmentTypes.HEAT.get()).heatCapacity();
        }

        @Override
        public void setHeatCapacity(int value) {
            blockEntity.setData(IRAttachmentTypes.HEAT.get(), new HeatStorage(getHeatStored(), value));
        }
    }
}

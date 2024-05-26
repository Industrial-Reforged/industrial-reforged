package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.attachments.AttachmentHeatStorage;
import com.indref.industrial_reforged.api.data.components.ComponentHeatStorage;
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
    }

    public record Block(BlockEntity blockEntity) implements IHeatStorage {
        public Block(BlockEntity blockEntity, int initialCapacity) {
            this(blockEntity);
            this.setHeatCapacity(initialCapacity);
        }

        @Override
        public int getHeatStored() {
            return blockEntity.getData(IRAttachmentTypes.HEAT.get()).getHeatStored();
        }

        @Override
        public void setHeatStored(int value) {
            blockEntity.setData(IRAttachmentTypes.HEAT.get(), new AttachmentHeatStorage(value, getHeatCapacity()));
        }

        @Override
        public int getHeatCapacity() {
            return blockEntity.getData(IRAttachmentTypes.HEAT.get()).getHeatCapacity();
        }

        @Override
        public void setHeatCapacity(int value) {
            blockEntity.setData(IRAttachmentTypes.HEAT.get(), new AttachmentHeatStorage(getHeatStored(), value));
        }
    }
}

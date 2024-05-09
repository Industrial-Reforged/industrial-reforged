package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.components.ComponentHeatStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HeatWrapper {
    public static class Item implements IHeatStorage {
        private final ItemStack itemStack;

        public Item(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public int getHeatStored() {
            return itemStack.get(IRDataComponents.HEAT).getHeatStored();
        }

        @Override
        public void setHeatStored(int value) {
            itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(value, getHeatCapacity()));
        }

        @Override
        public int getHeatCapacity() {
            return itemStack.get(IRDataComponents.HEAT).getHeatCapacity();
        }

        @Override
        public void setHeatCapacity(int value) {
            itemStack.set(IRDataComponents.HEAT, new ComponentHeatStorage(getHeatStored(), value));
        }
    }

    public static class Block implements IHeatStorage {
        private final BlockEntity blockEntity;

        public Block(BlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        @Override
        public int getHeatStored() {
            return blockEntity.getData(IRAttachmentTypes.HEAT.get()).getHeatStored();
        }

        @Override
        public void setHeatStored(int value) {
            blockEntity.getData(IRAttachmentTypes.HEAT.get()).setHeatStored(value);
        }

        @Override
        public int getHeatCapacity() {
            return blockEntity.getData(IRAttachmentTypes.HEAT.get()).getHeatCapacity();
        }

        @Override
        public void setHeatCapacity(int value) {
            blockEntity.getData(IRAttachmentTypes.HEAT.get()).setHeatCapacity(value);
        }
    }
}

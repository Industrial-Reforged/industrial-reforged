package com.indref.industrial_reforged.api.capabilities.energy.storage;

import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.energy.EnergyStorage;
import com.indref.industrial_reforged.api.data.energy.IEnergyStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Basic Capability Interface used for handling
 * methods related to the heat storage capability
 */
public class EnergyWrapper {
    public static class Item implements IEnergyStorage {
        private final ItemStack itemStack;

        public Item(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public int getEnergyStored() {
            return itemStack.get(IRDataComponents.ENERGY).getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            itemStack.get(IRDataComponents.ENERGY).setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return itemStack.get(IRDataComponents.ENERGY).getEnergyStored();
        }

        @Override
        public void setEnergyCapacity(int value) {
            itemStack.get(IRDataComponents.ENERGY).setEnergyCapacity(value);
        }
    }

    public static class Block implements IEnergyStorage {
        private final BlockEntity blockEntity;

        public Block(BlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        @Override
        public int getEnergyStored() {
            return blockEntity.getData(IRAttachmentTypes.ENERGY.get()).getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            blockEntity.getData(IRAttachmentTypes.ENERGY.get()).setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return blockEntity.getData(IRAttachmentTypes.ENERGY.get()).getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            blockEntity.getData(IRAttachmentTypes.ENERGY.get()).setEnergyCapacity(value);
        }
    }

}

package com.indref.industrial_reforged.api.capabilities.energy.storage;

import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
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
            return itemStack.getData(IRAttachmentTypes.ENERGY.get()).getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            itemStack.setData(IRAttachmentTypes.ENERGY.get(), new EnergyStorage(value));
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
            blockEntity.setData(IRAttachmentTypes.ENERGY.get(), new EnergyStorage(value));
        }
    }

}

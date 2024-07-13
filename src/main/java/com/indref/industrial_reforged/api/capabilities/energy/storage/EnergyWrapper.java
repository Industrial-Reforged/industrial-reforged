package com.indref.industrial_reforged.api.capabilities.energy.storage;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.EnergyStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Basic Capability Interface used for handling
 * methods related to the heat storage capability
 */
public class EnergyWrapper {
    public record Item(ItemStack itemStack) implements IEnergyStorage {
        public Item(ItemStack itemStack, int initialCapacity) {
            this(itemStack);
            this.setEnergyCapacity(initialCapacity);
        }

        @Override
        public int getEnergyStored() {
            EnergyStorage energyStorage = itemStack.get(IRDataComponents.ENERGY);
            if (energyStorage != null)
                return energyStorage.energyStored();
            else
                throw new NullPointerException("Failed to get energy component for item: "
                        + itemStack.getItem()
                        + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
        }

        @Override
        public void setEnergyStored(int value) {
            itemStack.set(IRDataComponents.ENERGY, new EnergyStorage(value, getEnergyCapacity()));
        }

        @Override
        public int getEnergyCapacity() {
            EnergyStorage energyStorage = itemStack.get(IRDataComponents.ENERGY);
            if (energyStorage != null)
                return energyStorage.energyCapacity();
            else
                throw new NullPointerException("Failed to get energy component for item: "
                        + itemStack.getItem()
                        + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
        }

        @Override
        public void setEnergyCapacity(int value) {
            itemStack.set(IRDataComponents.ENERGY, new EnergyStorage(getEnergyStored(), value));
        }
    }

    // Move this from a wrapper to a regular capability
    public record Block(BlockEntity blockEntity) implements IEnergyStorage {
        public Block(BlockEntity blockEntity, int initialCapacity) {
            this(blockEntity);
            this.setEnergyCapacity(initialCapacity);
        }

        @Override
        public int getEnergyStored() {
            return blockEntity.getData(IRAttachmentTypes.ENERGY.get()).energyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            blockEntity.setData(IRAttachmentTypes.ENERGY.get(), new EnergyStorage(value, getEnergyCapacity()));
        }

        @Override
        public int getEnergyCapacity() {
            return blockEntity.getData(IRAttachmentTypes.ENERGY.get()).energyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            blockEntity.setData(IRAttachmentTypes.ENERGY.get(), new EnergyStorage(getEnergyStored(), value));
        }
    }

}

package com.indref.industrial_reforged.api.capabilities.energy.storage;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.attachments.AttachmentEnergyStorage;
import com.indref.industrial_reforged.api.data.components.ComponentEnergyStorage;
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
            ComponentEnergyStorage componentEnergyStorage = itemStack.get(IRDataComponents.ENERGY);
            if (componentEnergyStorage != null)
                return componentEnergyStorage.energyStored();
            else
                throw new NullPointerException("Failed to get energy component for item: "
                        + itemStack.getItem()
                        + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
        }

        @Override
        public void setEnergyStored(int value) {
            itemStack.set(IRDataComponents.ENERGY, new ComponentEnergyStorage(value, getEnergyCapacity()));
        }

        @Override
        public int getEnergyCapacity() {
            ComponentEnergyStorage componentEnergyStorage = itemStack.get(IRDataComponents.ENERGY);
            if (componentEnergyStorage != null)
                return componentEnergyStorage.energyCapacity();
            else
                throw new NullPointerException("Failed to get energy component for item: "
                        + itemStack.getItem()
                        + " please add it under the item properties using .component(...) or preferably inherit one of the electric item classes");
        }

        @Override
        public void setEnergyCapacity(int value) {
            itemStack.set(IRDataComponents.ENERGY, new ComponentEnergyStorage(getEnergyStored(), value));
        }
    }

    public record Block(BlockEntity blockEntity) implements IEnergyStorage {
        public Block(BlockEntity blockEntity, int initialCapacity) {
            this(blockEntity);
            this.setEnergyCapacity(initialCapacity);
        }

        @Override
        public int getEnergyStored() {
            return blockEntity.getData(IRAttachmentTypes.ENERGY.get()).getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            blockEntity.setData(IRAttachmentTypes.ENERGY.get(), new AttachmentEnergyStorage(value, getEnergyCapacity()));
        }

        @Override
        public int getEnergyCapacity() {
            return blockEntity.getData(IRAttachmentTypes.ENERGY.get()).getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            blockEntity.setData(IRAttachmentTypes.ENERGY.get(), new AttachmentEnergyStorage(getEnergyStored(), value));
        }
    }

}

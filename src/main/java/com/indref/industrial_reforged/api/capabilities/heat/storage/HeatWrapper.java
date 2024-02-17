package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.api.data.energy.EnergyStorage;
import com.indref.industrial_reforged.api.data.heat.HeatStorage;
import com.indref.industrial_reforged.api.data.heat.IHeatStorage;
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
            return itemStack.getData(IRAttachmentTypes.HEAT.get()).getHeatStored();
        }

        @Override
        public void setHeatStored(int value) {
            itemStack.setData(IRAttachmentTypes.HEAT.get(), new HeatStorage(value));
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
            blockEntity.setData(IRAttachmentTypes.HEAT.get(), new HeatStorage(value));
        }
    }
}

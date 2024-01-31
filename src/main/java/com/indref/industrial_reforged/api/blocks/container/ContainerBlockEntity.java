package com.indref.industrial_reforged.api.blocks.container;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class ContainerBlockEntity extends BlockEntity implements IEnergyBlock {
    private final BlockEntityType<?> blockEntityType;
    private final int energyCapacity;
    private ItemStackHandler itemHandler;
    private FluidTank fluidTank;

    public ContainerBlockEntity(int slots, int fluidCapacity, int energyCapacity, BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
        this.blockEntityType = p_155228_;
        this.energyCapacity = energyCapacity;
        registerContainers(slots, fluidCapacity, this);
    }

    @Override
    public int getEnergyCapacity() {
        return energyCapacity;
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        if (itemHandler != null) {
            p_187471_.put("itemhandler", itemHandler.serializeNBT());
        }
        if (fluidTank != null) {
            fluidTank.writeToNBT(p_187471_);
        }
    }

    private static void registerContainers(int slots, int fluidCapacity, ContainerBlockEntity blockEntity) {
        if (slots > 0) {
            blockEntity.itemHandler = new ItemStackHandler(slots) {
                @Override
                protected void onContentsChanged(int slot) {
                    blockEntity.setChanged();
                    if (!blockEntity.level.isClientSide()) {
                        blockEntity.level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }
                }
            };
        }

        if (fluidCapacity > 0) {
            blockEntity.fluidTank = new FluidTank(16000) {
                @Override
                protected void onContentsChanged() {
                    blockEntity.setChanged();
                    blockEntity.level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                }
            };
        }
    }

}

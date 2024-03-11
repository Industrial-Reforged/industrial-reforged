package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.api.util.ValidationFunctions;
import com.indref.industrial_reforged.networking.data.ItemSyncData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class ContainerBlockEntity extends BlockEntity implements IEnergyBlock, IHeatBlock {
    private int energyCapacity;
    private int heatCapacity;
    private ItemStackHandler itemHandler;
    private FluidTank fluidTank;
    private @Nullable EnergyTier energyTier;

    public ContainerBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public int getEnergyCapacity() {
        return energyCapacity;
    }

    @Override
    public int getHeatCapacity() {
        return heatCapacity;
    }

    @Override
    public @Nullable EnergyTier getEnergyTier() {
        return energyTier;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack[] getItemHandlerStacks() {
        ItemStack[] itemStacks = new ItemStack[getItemHandler().getSlots()];
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemStacks[i] = itemHandler.getStackInSlot(i);
        }
        return itemStacks;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        if (fluidTank != null) fluidTank.writeToNBT(p_187471_);
        if (itemHandler != null) p_187471_.put("itemhandler", itemHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        if (fluidTank != null) fluidTank.readFromNBT(p_155245_);
        if (itemHandler != null) itemHandler.deserializeNBT(p_155245_.getCompound("itemhandler"));
    }

    protected final void addItemHandler(int slots) {
        addItemHandler(slots, (slot, itemStack) -> true);
    }

    protected final void addFluidTank(int capacityInMb) {
        addFluidTank(capacityInMb, fluidStack -> true);
    }

    protected final void addItemHandler(int slots, ValidationFunctions.ItemValid validation) {
        this.itemHandler = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                onItemsChanged(slot);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return validation.itemValid(slot, stack);
            }
        };
    }

    protected final void addFluidTank(int capacityInMb, ValidationFunctions.FluidValid validation) {
        this.fluidTank = new FluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                onFluidsChanged();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return validation.fluidValid(stack);
            }
        };
    }

    protected void onItemsChanged(int slot) {
    }

    protected void onFluidsChanged() {
    }

    public void tick(BlockPos blockPos, Level level) {}

    protected final void addHeatStorage(int capacity) {
        this.heatCapacity = capacity;
    }

    protected final void addEnergyStorage(EnergyTier energyTier, @Nullable Integer capacity) {
        this.energyTier = energyTier;
        this.energyCapacity = Objects.requireNonNullElseGet(capacity, energyTier::getDefaultCapacity);
    }
}

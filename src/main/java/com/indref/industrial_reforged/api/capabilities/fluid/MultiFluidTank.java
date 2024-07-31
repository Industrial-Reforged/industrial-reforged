package com.indref.industrial_reforged.api.capabilities.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MultiFluidTank implements IFluidHandler, INBTSerializable<CompoundTag> {
    protected List<FluidStack> fluids;
    protected int tanks;
    protected int capacity;

    public MultiFluidTank(int capacity, int tanks) {
        this.fluids = new ArrayList<>();
        this.tanks = tanks;
        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return this.tanks;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return fluids.get(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return i < this.tanks;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return null;
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return null;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("fluids_amount", this.fluids.size());
        tag.putInt("capacity", this.capacity);
        tag.putInt("tanks", this.tanks);
        CompoundTag fluidsTag = new CompoundTag();
        for (int i = 0; i < this.fluids.size(); i++) {
            fluidsTag.put(String.valueOf(i), this.fluids.get(i).saveOptional(provider));
        }
        tag.put("fluids", fluidsTag);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        int fluidsAmount = tag.getInt("fluids_amount");
        CompoundTag fluids = tag.getCompound("fluids");
        for (int i = 0; i < fluidsAmount; i++) {
            FluidStack fluidStack = FluidStack.parseOptional(provider, fluids.getCompound(String.valueOf(i)));
            this.fluids.add(fluidStack);
        }
        this.capacity = tag.getInt("capacity");
        this.tanks = tag.getInt("tanks");
    }
}

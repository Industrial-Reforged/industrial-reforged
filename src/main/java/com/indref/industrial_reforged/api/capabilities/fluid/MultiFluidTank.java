package com.indref.industrial_reforged.api.capabilities.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A tank that stores multiple fluids (has multiple tanks). This tank will automatically
 * fill new fluids to a new tank as long as there is one available. The amount of tanks is defined
 * by the `tanks` field.
 * <br>
 * The capacity of this tank is the sum of fluid amounts in all tanks, not the capacity for every individual tank.
 */
public class MultiFluidTank implements IFluidHandler, INBTSerializable<CompoundTag> {
    protected List<FluidStack> fluids;
    protected int tanks;
    protected int capacity;

    public MultiFluidTank(int capacity, int tanks) {
        this.fluids = new ArrayList<>();
        this.tanks = tanks;
        this.capacity = capacity;
        for (int i = 0; i < this.tanks; i++) {
            fluids.add(FluidStack.EMPTY);
        }
    }

    @Override
    public int getTanks() {
        return this.tanks;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return i < this.tanks && i < this.fluids.size() ? this.fluids.get(i) : FluidStack.EMPTY;
    }

    public int getTankCapacity() {
        return this.capacity;
    }

    @Override
    @Deprecated
    public int getTankCapacity(int i) {
        return this.getTankCapacity();
    }

    public void setTankCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack fluidStack) {
        return tank < this.tanks;
    }

    public void setFluidInTank(int tank, FluidStack fluidStack) {
        this.fluids.set(tank, fluidStack);
    }

    public int fill(int tank, FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(tank, resource)) {
            return 0;
        }
        FluidStack fluid = this.getFluidInTank(tank);
        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }
            if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                return 0;
            }
            return Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            fluid = resource.copyWithAmount(Math.min(capacity, resource.getAmount()));
            onContentsChanged();
            return fluid.getAmount();
        }
        if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
            return 0;
        }
        int filled = capacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        if (filled > 0)
            onContentsChanged();
        return filled;
    }

    protected void onContentsChanged() {
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) return 0;

        for (int i = 0; i < this.tanks; i++) {
            FluidStack fluidInTank = this.getFluidInTank(i);
            if (FluidStack.isSameFluidSameComponents(resource, fluidInTank) || fluidInTank.isEmpty()) {
                return this.fill(i, resource, action);
            }
        }

        return 0;
    }

    public FluidStack drain(int tank, FluidStack resource, IFluidHandler.FluidAction action) {
        return !resource.isEmpty() && FluidStack.isSameFluidSameComponents(resource, this.getFluidInTank(tank)) ? this.drain(resource.getAmount(), action) : FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) return FluidStack.EMPTY;

        for (int i = 0; i < this.tanks; i++) {
            FluidStack fluidInTank = this.getFluidInTank(i);
            if (FluidStack.isSameFluidSameComponents(resource, fluidInTank)) {
                return this.drain(i, resource, action);
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    @Deprecated
    public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
    }

    public boolean isEmpty() {
        return this.fluids.stream().filter(f -> !f.isEmpty()).toList().isEmpty();
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.Provider provider) {
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
            this.fluids.set(i, fluidStack);
        }
        this.capacity = tag.getInt("capacity");
        this.tanks = tag.getInt("tanks");
    }
}

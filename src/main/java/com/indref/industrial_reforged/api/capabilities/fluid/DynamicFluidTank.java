package com.indref.industrial_reforged.api.capabilities.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Predicate;

/**
 * FluidTank with a dynamic capacity
 */
public class DynamicFluidTank implements IFluidTank, IFluidHandler, INBTSerializable<CompoundTag> {
    protected Predicate<FluidStack> validator;
    protected FluidStack fluid = FluidStack.EMPTY;
    protected int capacity;

    public DynamicFluidTank(int capacity) {
        this(capacity, e -> true);
    }

    public DynamicFluidTank(int capacity, Predicate<FluidStack> validator) {
        this.capacity = capacity;
        this.validator = validator;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setValidator(Predicate<FluidStack> validator) {
        if (validator != null) {
            this.validator = validator;
        }
    }

    public boolean isFluidValid(FluidStack stack) {
        return validator.test(stack);
    }

    public int getCapacity() {
        return capacity;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return fluid.getAmount();
    }

    private DynamicFluidTank readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        fluid = FluidStack.parseOptional(lookupProvider, nbt.getCompound("Fluid"));
        return this;
    }

    private CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        if (!fluid.isEmpty()) {
            nbt.put("Fluid", fluid.save(lookupProvider));
        }

        return nbt;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
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

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, fluid)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = fluid.copyWithAmount(drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            onContentsChanged();
        }
        return stack;
    }

    protected void onContentsChanged() {}

    public void setFluid(FluidStack stack) {
        this.fluid = stack;
    }

    public boolean isEmpty() {
        return fluid.isEmpty();
    }

    public int getSpace() {
        return Math.max(0, capacity - fluid.getAmount());
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("capacity", this.capacity);
        return writeToNBT(provider, nbt);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.capacity = nbt.getInt("capacity");
        this.fluid = readFromNBT(provider, nbt).getFluid();
    }
}

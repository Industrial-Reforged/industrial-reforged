package com.indref.industrial_reforged.api.capabilities.fluid;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class MultiFluidTank implements IFluidHandler, INBTSerializable<CompoundTag> {
    private final int tanks;
    private final IntList tankCapacities;
    private final NonNullList<FluidStack> fluids;

    public MultiFluidTank(int tanks, Int2IntFunction capacityFunction) {
        this.tanks = tanks;
        this.tankCapacities = new IntArrayList();
        for (int i = 0; i < this.tanks; i++) {
            this.tankCapacities.add(capacityFunction.applyAsInt(i));
        }
        this.fluids = NonNullList.withSize(tanks, FluidStack.EMPTY);
    }

    @Override
    public int getTanks() {
        return this.tanks;
    }

    public void setFluidInTank(int i, FluidStack fluid) {
        this.fluids.set(i, fluid);
    }

    protected void onContentsChanged() {
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return this.fluids.get(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return this.tankCapacities.getInt(i);
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        if (fluidStack.isEmpty()) return 0;

        int tank = findBestTankToFill(fluidStack);

        if (tank != -1) {
            return fill(tank, fluidStack, new TransferContext(fluidAction));
        } else {
            return 0;
        }
    }

    public int fill(int tank, FluidStack fluidStack, TransferContext context) {
        FluidStack fluidInTank = this.getFluidInTank(tank);
        int tankCapacity = this.getTankCapacity(tank);
        if (context.action().simulate()) {
            if (fluidInTank.isEmpty()) {
                return Math.min(tankCapacity, fluidStack.getAmount());
            } else {
                return !FluidStack.isSameFluidSameComponents(fluidInTank, fluidStack) ? 0 : Math.min(tankCapacity - fluidInTank.getAmount(), fluidStack.getAmount());
            }
        } else if (fluidInTank.isEmpty()) {
            this.setFluidInTank(tank, fluidStack.copyWithAmount(Math.min(this.getTankCapacity(tank), fluidStack.getAmount())));
            if (context.update()) {
                this.onContentsChanged();
            }
            return this.getFluidInTank(tank).getAmount();
        } else if (!FluidStack.isSameFluidSameComponents(fluidInTank, fluidStack)) {
            return 0;
        } else {
            int filled = tankCapacity - fluidInTank.getAmount();
            if (fluidStack.getAmount() < filled) {
                fluidInTank.grow(fluidStack.getAmount());
                filled = fluidStack.getAmount();
            } else {
                fluidInTank.setAmount(this.getTankCapacity(tank));
            }

            if (filled > 0 && context.update()) {
                this.onContentsChanged();
            }

            return filled;
        }
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        int tank = -1;
        for (int i = this.tanks - 1; i >= 0; i--) {
            if (this.getFluidInTank(i).getAmount() > 0 && FluidStack.isSameFluidSameComponents(this.getFluidInTank(i), fluidStack)) {
                tank = i;
                break;
            }
        }
        if (tank != -1) {
            return this.drain(tank, fluidStack.getAmount(), new TransferContext(fluidAction));
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction fluidAction) {
        int tank = -1;
        for (int i = this.tanks - 1; i >= 0; i--) {
            if (this.getFluidInTank(i).getAmount() > 0) {
                tank = i;
                break;
            }
        }
        if (tank != -1) {
            return this.drain(tank, maxDrain, new TransferContext(fluidAction));
        }
        return FluidStack.EMPTY;
    }

    public @NotNull FluidStack drain(int tank, int maxDrain, TransferContext context) {
        int drained = Math.min(this.getFluidInTank(tank).getAmount(), maxDrain);

        FluidStack fluidInTank = getFluidInTank(tank);
        FluidStack stack = fluidInTank.copyWithAmount(drained);
        if (context.action().execute() && drained > 0) {
            fluidInTank.shrink(drained);
            this.onContentsChanged();
        }

        return stack;
    }

    private int findBestTankToFill(FluidStack resource) {
        int emptyTankIndex = -1;

        for (int i = 0; i < this.tanks; i++) {
            FluidStack current = this.getFluidInTank(i);

            if (current.isEmpty()) {
                // Remember first empty tank as fallback
                if (emptyTankIndex == -1 && this.isFluidValid(i, resource)) {
                    emptyTankIndex = i;
                }
            } else if (FluidStack.isSameFluidSameComponents(current, resource) &&
                    this.getTankCapacity(i) > current.getAmount()) {
                // Best option: partially filled matching tank with space
                return i;
            }
        }

        // If no partial match, use the first valid empty tank
        return emptyTankIndex;
    }


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        for (int i = 0; i < tanks; i++) {
            nbt.put("tank_" + i, this.getFluidInTank(i).saveOptional(provider));
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (int i = 0; i < tanks; i++) {
            CompoundTag tag = nbt.getCompound("tank_" + i);
            this.setFluidInTank(i, FluidStack.parseOptional(provider, tag));
        }
    }

    public record TransferContext(FluidAction action, boolean forced, boolean update) {
        public TransferContext(FluidAction action) {
            this(action, false, true);
        }

        public TransferContext() {
            this(FluidAction.EXECUTE, false, true);
        }
    }
}

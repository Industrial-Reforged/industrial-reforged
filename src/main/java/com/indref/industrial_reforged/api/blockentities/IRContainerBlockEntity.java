package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorage;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.energy.SidedEnergyHandler;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.capabilities.heat.SidedHeatHandler;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;
import java.util.function.Supplier;

public abstract class IRContainerBlockEntity extends ContainerBlockEntity {
    private EnergyStorage euStorage;
    private HeatStorage heatStorage;

    public IRContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected final void addEuStorage(Supplier<EnergyTier> energyTier) {
        addEuStorage(energyTier, energyTier.get().defaultCapacity());
    }

    protected final void addEuStorage(Supplier<EnergyTier> energyTier, int energyCapacity) {
        this.euStorage = new EnergyStorage(energyTier) {
            @Override
            public void onEnergyChanged(int oldAmount) {
                update();
                IRContainerBlockEntity.this.onEuChanged(oldAmount);
            }
        };
        this.euStorage.setEnergyCapacity(energyCapacity);
    }

    protected final void addHeatStorage(float heatCapacity) {
        addHeatStorage(heatCapacity, 5, 5);
    }

    protected final void addHeatStorage(float heatCapacity, float maxInput, float maxOutput) {
        this.heatStorage = new HeatStorage(heatCapacity, maxInput, maxOutput) {
            @Override
            public void onHeatChanged(float oldAmount) {
                update();
                IRContainerBlockEntity.this.onHeatChanged(oldAmount);
            }
        };
    }

    public IEnergyStorage getEuStorage() {
        return this.euStorage;
    }

    protected EnergyStorage getEuStorageImpl() {
        return this.euStorage;
    }

    public IHeatStorage getHeatStorage() {
        return this.heatStorage;
    }

    protected HeatStorage getHeatStorageImpl() {
        return this.heatStorage;
    }

    public void onEuChanged(int oldAmount) {
    }

    public void onHeatChanged(float oldAmount) {
    }

    public ItemStack forceInsertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack existing = getItemHandler().getStackInSlot(slot);

        IItemHandler itemHandler1 = getItemHandler();
        int limit = Math.min(itemHandler1.getSlotLimit(slot), stack.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                getItemStackHandler().setStackInSlot(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onItemsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public int forceFillTank(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        }

        FluidStack fluid = getFluidTank().getFluid();
        int capacity = getFluidTank().getCapacity();

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
            onFluidChanged();
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
            onFluidChanged();
        return filled;
    }

    public IEnergyStorage getEuHandlerOnSide(Direction direction) {
        return getHandlerOnSide(IRCapabilities.EnergyStorage.BLOCK, SidedEnergyHandler::new, direction, getEuStorage());
    }

    public IHeatStorage getHeatHandlerOnSide(Direction direction) {
        return getHandlerOnSide(IRCapabilities.HeatStorage.BLOCK, SidedHeatHandler::new, direction, getHeatStorage());
    }

    public List<ItemStack> getNonEmptyStacks() {
        return getItemHandlerStacksList().stream().filter(stack -> !stack.isEmpty()).toList();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        if (getEuStorage() != null)
            tag.put("eu_storage", this.getEuStorageImpl().serializeNBT(provider));
        if (getHeatStorage() != null)
            tag.put("heat_storage", this.getHeatStorageImpl().serializeNBT(provider));
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        if (getEuStorageImpl() != null)
            this.getEuStorageImpl().deserializeNBT(provider, tag.getCompound("eu_storage"));
        if (getHeatStorageImpl() != null)
            this.getHeatStorageImpl().deserializeNBT(provider, tag.getCompound("heat_storage"));
    }
}

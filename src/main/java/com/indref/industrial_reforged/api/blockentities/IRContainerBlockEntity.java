package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.StorageChangedListener;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.impl.heat.HeatStorageImpl;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class IRContainerBlockEntity extends ContainerBlockEntity {
    private EnergyHandler euStorage;
    private HeatStorageImpl heatStorage;

    public IRContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected final <T extends EnergyHandler & StorageChangedListener> void addEuStorage(Function<Supplier<EnergyTierImpl>, T> energyHandlerConstructor, Supplier<EnergyTierImpl> energyTier, int energyCapacity) {
        T storage = energyHandlerConstructor.apply(energyTier);
        storage.setOnChangedFunction(oldAmount -> {
            this.updateData();
            this.onEuChanged(oldAmount);
        });
        storage.setEnergyCapacity(energyCapacity);
        this.euStorage = storage;
    }

    protected final void addHeatStorage(float heatCapacity) {
        addHeatStorage(heatCapacity, 5, 5);
    }

    protected final void addHeatStorage(float heatCapacity, float maxInput, float maxOutput) {
        this.heatStorage = new HeatStorageImpl(heatCapacity, maxInput, maxOutput) {
            @Override
            public void onHeatChanged(float oldAmount) {
                updateData();
                IRContainerBlockEntity.this.onHeatChanged(oldAmount);
            }
        };
    }

    public EnergyHandler getEuStorage() {
        return this.euStorage;
    }

    public HeatStorage getHeatStorage() {
        return this.heatStorage;
    }

    protected HeatStorageImpl getHeatStorageImpl() {
        return this.heatStorage;
    }

    public void onEuChanged(int oldAmount) {
    }

    public void onHeatChanged(float oldAmount) {
    }

    public int forceFillTank(IFluidHandler fluidHandler, FluidStack resource, IFluidHandler.FluidAction action, Consumer<Integer> onChange) {
        if (resource.isEmpty()) {
            return 0;
        }

        FluidStack fluid = fluidHandler.getFluidInTank(0);
        int capacity = fluidHandler.getTankCapacity(0);

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
            onChange.accept(0);
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
            onChange.accept(0);
        return filled;
    }

    public EnergyHandler getEuHandlerOnSide(Direction direction) {
        return this.getEuStorage();
    }

    public HeatStorage getHeatHandlerOnSide(Direction direction) {
        return this.getHeatStorage();
    }

    @Override
    public IEnergyStorage getEnergyStorageOnSide(Direction direction) {
        return null;
    }

    public List<ItemStack> getNonEmptyStacks(IItemHandler itemHandler) {
        return getItemHandlerStacksList(itemHandler).stream().filter(stack -> !stack.isEmpty()).toList();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        if (getEuStorage() instanceof INBTSerializable<?> serializableEuStorage)
            tag.put("eu_storage", serializableEuStorage.serializeNBT(provider));
        if (getHeatStorage() != null)
            tag.put("heat_storage", this.getHeatStorageImpl().serializeNBT(provider));
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        if (getEuStorage() instanceof INBTSerializable<?> serializableEuStorage) {
            Tag storage = tag.get("eu_storage");
            ((INBTSerializable<Tag>) serializableEuStorage).deserializeNBT(provider, storage);
        }
        if (getHeatStorageImpl() != null)
            this.getHeatStorageImpl().deserializeNBT(provider, tag.getCompound("heat_storage"));
    }
}

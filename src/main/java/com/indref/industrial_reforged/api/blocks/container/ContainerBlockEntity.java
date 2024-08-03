package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorage;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.api.util.ValidationFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ContainerBlockEntity extends BlockEntity {
    private @Nullable ItemStackHandler itemHandler;
    private @Nullable FluidTank fluidTank;
    private @Nullable EnergyStorage energyStorage;
    private @Nullable HeatStorage heatStorage;

    private BlockCapabilityCache<IItemHandler, @Nullable Direction> itemCapCache;
    private BlockCapabilityCache<IFluidHandler, @Nullable Direction> fluidCapCache;
    private BlockCapabilityCache<IEnergyStorage, @Nullable Direction> energyCapCache;
    private BlockCapabilityCache<IHeatStorage, @Nullable Direction> heatCapCache;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void clientTick() {
    }

    public void serverTick() {
    }

    public void commonTick() {
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidTank;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public IHeatStorage getHeatStorage() {
        return heatStorage;
    }

    protected ItemStackHandler getItemStackHandler() {
        return itemHandler;
    }

    protected FluidTank getFluidTank() {
        return fluidTank;
    }

    protected EnergyStorage getEnergyStorageImpl() {
        return energyStorage;
    }

    protected HeatStorage getHeatStorageImpl() {
        return heatStorage;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel serverLevel) {
            if (getItemHandler() != null) {
                this.itemCapCache = createCache(Capabilities.ItemHandler.BLOCK, serverLevel);
            }
            if (getFluidTank() != null) {
                this.fluidCapCache = createCache(Capabilities.FluidHandler.BLOCK, serverLevel);
            }
            if (getEnergyStorage() != null) {
                this.energyCapCache = createCache(IRCapabilities.EnergyStorage.BLOCK, serverLevel);
            }
            if (getHeatStorage() != null) {
                this.heatCapCache = createCache(IRCapabilities.HeatStorage.BLOCK, serverLevel);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T, C> T getCapFromCache(BlockCapability<T, C> capability) {
        if (capability == Capabilities.ItemHandler.BLOCK)
            return this.itemCapCache != null ? (T) this.itemCapCache.getCapability() : null;
        else if (capability == Capabilities.FluidHandler.BLOCK)
            return this.fluidCapCache != null ? (T) this.fluidCapCache.getCapability() : null;
        else if (capability == IRCapabilities.EnergyStorage.BLOCK)
            return this.energyCapCache != null ? (T) this.energyCapCache.getCapability() : null;
        else if (capability == IRCapabilities.HeatStorage.BLOCK)
            return this.heatCapCache != null ? (T) this.heatCapCache.getCapability() : null;
        else return null;
    }

    private @NotNull <T> BlockCapabilityCache<T, @Nullable Direction> createCache(BlockCapability<T, @Nullable Direction> capability, ServerLevel serverLevel) {
        return BlockCapabilityCache.create(
                capability,
                serverLevel,
                worldPosition,
                null
        );
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        if (this.getFluidTank() != null)
            this.getFluidTank().readFromNBT(provider, nbt);
        if (this.getItemStackHandler() != null)
            this.getItemStackHandler().deserializeNBT(provider, nbt.getCompound("itemhandler"));
        if (this.getEnergyStorageImpl() != null)
            this.getEnergyStorageImpl().deserializeNBT(provider, nbt.getCompound("energy_storage"));
        if (this.getHeatStorageImpl() != null)
            this.getHeatStorageImpl().deserializeNBT(provider, nbt.getCompound("heat_storage"));
        loadData(nbt, provider);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        if (getFluidTank() != null)
            getFluidTank().writeToNBT(provider, nbt);
        if (getItemStackHandler() != null)
            nbt.put("itemhandler", getItemStackHandler().serializeNBT(provider));
        if (getEnergyStorageImpl() != null)
            nbt.put("energy_storage", getEnergyStorageImpl().serializeNBT(provider));
        if (getHeatStorageImpl() != null)
            nbt.put("heat_storage", getHeatStorageImpl().serializeNBT(provider));
        saveData(nbt, provider);
    }

    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected final void addItemHandler(int slots) {
        addItemHandler(slots, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, int slotLimit) {
        addItemHandler(slots, slotLimit, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, ValidationFunctions.ItemValid validation) {
        addItemHandler(slots, 64, validation);
    }

    protected final void addFluidTank(int capacityInMb) {
        addFluidTank(capacityInMb, fluidStack -> true);
    }

    protected final void addItemHandler(int slots, int slotLimit, ValidationFunctions.ItemValid validation) {
        this.itemHandler = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                onItemsChanged(slot);
                level.invalidateCapabilities(worldPosition);
                update();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return validation.itemValid(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return slotLimit;
            }
        };
    }

    protected final void addFluidTank(int capacityInMb, ValidationFunctions.FluidValid validation) {
        this.fluidTank = new FluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                update();
                setChanged();
                onFluidChanged();
                level.invalidateCapabilities(worldPosition);
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return validation.fluidValid(stack);
            }
        };
    }

    protected final void addEnergyStorage(EnergyTier energyTier) {
        addEnergyStorage(energyTier, energyTier.getDefaultCapacity());
    }

    protected final void addEnergyStorage(EnergyTier energyTier, int energyCapacity) {
        this.energyStorage = new EnergyStorage(energyTier) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                ContainerBlockEntity.this.onEnergyChanged();
                level.invalidateCapabilities(worldPosition);
                update();
            }
        };
        this.energyStorage.setEnergyCapacity(energyCapacity);
    }

    protected final void addHeatStorage(int capacity) {
        this.heatStorage = new HeatStorage() {
            @Override
            public void onHeatChanged() {
                setChanged();
                ContainerBlockEntity.this.onHeatChanged();
                level.invalidateCapabilities(worldPosition);
                update();
            }
        };
        this.heatStorage.setHeatCapacity(capacity);
    }

    private void update() {
        if (!level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    protected void onItemsChanged(int slot) {
    }

    protected void onFluidChanged() {
    }

    public void onEnergyChanged() {
    }

    public void onHeatChanged() {
    }

    public void drop() {
        ItemStack[] stacks = getItemHandlerStacks();
        SimpleContainer inventory = new SimpleContainer(stacks);
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public @Nullable ItemStack[] getItemHandlerStacks() {
        IItemHandler itemStackHandler = getItemHandler();

        if (itemStackHandler == null) return null;

        ItemStack[] itemStacks = new ItemStack[itemStackHandler.getSlots()];
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStacks[i] = itemStackHandler.getStackInSlot(i);
        }
        return itemStacks;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }
}

package com.indref.industrial_reforged.api.blockentities.container;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.misc.RotatableEntityBlock;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorage;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.fluid.SidedFluidHandler;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.capabilities.heat.SidedHeatHandler;
import com.indref.industrial_reforged.api.capabilities.item.SidedItemHandler;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.api.util.ValidationFunctions;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class ContainerBlockEntity extends BlockEntity {
    private @Nullable ItemStackHandler itemHandler;
    private @Nullable FluidTank fluidTank;
    private @Nullable EnergyStorage energyStorage;
    private @Nullable HeatStorage heatStorage;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
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
                update();
                setChanged();
                onItemsChanged(slot);
                invalidateCapabilities();
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
                invalidateCapabilities();
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
                update();
                setChanged();
                ContainerBlockEntity.this.onEnergyChanged();
                invalidateCapabilities();
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
                invalidateCapabilities();
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

    public <T> T getHandlerOnSide(BlockCapability<T, @Nullable Direction> capability, SidedHandlerSupplier<T> handlerSupplier, Direction direction, T baseHandler) {
        if (direction == null) {
            return baseHandler;
        }

        Map<Direction, Pair<IOActions, int[]>> ioPorts = getSidedInteractions(capability);
        if (ioPorts.containsKey(direction)) {

            if (direction == Direction.UP || direction == Direction.DOWN) {
                return handlerSupplier.get(baseHandler, ioPorts.get(direction));
            }

            if (!this.getBlockState().hasProperty(RotatableEntityBlock.FACING)) {
                if (SharedConstants.IS_RUNNING_IN_IDE) {
                    IndustrialReforged.LOGGER.error("Cannot get Capability for a specific direction if the block does not have the facing blockstate. Affected be: {}", this);
                }
                return null;
            }

            Direction localDir = this.getBlockState().getValue(RotatableEntityBlock.FACING);

            return switch (localDir) {
                case NORTH -> handlerSupplier.get(baseHandler, ioPorts.get(direction.getOpposite()));
                case EAST -> handlerSupplier.get(baseHandler, ioPorts.get(direction.getClockWise()));
                case SOUTH -> handlerSupplier.get(baseHandler, ioPorts.get(direction));
                case WEST -> handlerSupplier.get(baseHandler, ioPorts.get(direction.getCounterClockWise()));
                default -> {
                    IndustrialReforged.LOGGER.debug("Unreachable");
                    yield null;
                }
            };
        }

        IndustrialReforged.LOGGER.debug("Handler is null :O");
        return null;
    }

    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return getHandlerOnSide(
                Capabilities.ItemHandler.BLOCK,
                SidedItemHandler::new,
                direction,
                getItemHandler()
        );
    }

    public IFluidHandler getFluidHandlerOnSide(Direction direction) {
        return getHandlerOnSide(
                Capabilities.FluidHandler.BLOCK,
                SidedFluidHandler::new,
                direction,
                getFluidHandler()
        );
    }

    public IHeatStorage getHeatHandlerOnSide(Direction direction) {
        return getHandlerOnSide(
                IRCapabilities.HeatStorage.BLOCK,
                ((handler, supportedActions) -> new SidedHeatHandler(handler, supportedActions.left())),
                direction,
                getHeatStorage()
        );
    }

    /**
     * Get the input/output config for the blockenitity.
     * If directions are not defined in the map, they are assumed to be {@link IOActions#NONE} and do not affect any slot.
     *
     * @return Map of directions that each map to a pair that defines the IOAction as well as the tanks that are affected. Return an empty map if you do not have an itemhandler
     */
    public abstract <T> ImmutableMap<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability);

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

    @FunctionalInterface
    public interface SidedHandlerSupplier<T> {
        T get(T handler, Pair<IOActions, int[]> supportedActions);
    }
}

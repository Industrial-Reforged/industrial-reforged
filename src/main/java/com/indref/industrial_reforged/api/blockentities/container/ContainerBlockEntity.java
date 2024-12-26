package com.indref.industrial_reforged.api.blockentities.container;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.misc.RotatableEntityBlock;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorage;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.fluid.DynamicFluidTank;
import com.indref.industrial_reforged.api.capabilities.fluid.SidedFluidHandler;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.capabilities.energy.SidedEnergyHandler;
import com.indref.industrial_reforged.api.capabilities.heat.SidedHeatHandler;
import com.indref.industrial_reforged.api.capabilities.item.SidedItemHandler;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.api.util.ValidationFunctions;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ContainerBlockEntity extends BlockEntity {
    private @Nullable ItemStackHandler itemHandler;
    private @Nullable DynamicFluidTank fluidTank;
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

    protected DynamicFluidTank getFluidTank() {
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
            this.getFluidTank().deserializeNBT(provider, nbt.getCompound("fluid_handler"));
        if (this.getItemStackHandler() != null)
            this.getItemStackHandler().deserializeNBT(provider, nbt.getCompound("item_handler"));
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
           nbt.put("fluid_handler", getFluidTank().serializeNBT(provider));
        if (getItemStackHandler() != null)
            nbt.put("item_handler", getItemStackHandler().serializeNBT(provider));
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
                onItemsChanged(slot);
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
        this.fluidTank = new DynamicFluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                update();
                onFluidChanged();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return validation.fluidValid(stack);
            }

            @Override
            public void setCapacity(int capacity) {
                super.setCapacity(capacity);
                onContentsChanged();
            }
        };
    }

    protected final void addEnergyStorage(Holder<EnergyTier> energyTier) {
        addEnergyStorage(energyTier, energyTier.value().getDefaultCapacity());
    }

    protected final void addEnergyStorage(Holder<EnergyTier> energyTier, int energyCapacity) {
        this.energyStorage = new EnergyStorage(energyTier) {
            @Override
            public void onEnergyChanged(int oldAmount) {
                update();
                ContainerBlockEntity.this.onEnergyChanged(oldAmount);
            }
        };
        this.energyStorage.setEnergyCapacity(energyCapacity);
    }

    protected final void addHeatStorage(int capacity) {
        this.heatStorage = new HeatStorage() {
            @Override
            public void onHeatChanged(int oldAmount) {
                update();
                ContainerBlockEntity.this.onHeatChanged(oldAmount);
            }
        };
        this.heatStorage.setHeatCapacity(capacity);
    }

    public void update() {
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    protected void onItemsChanged(int slot) {
    }

    protected void onFluidChanged() {
    }

    public void onEnergyChanged(int oldAmount) {
    }

    public void onHeatChanged(int oldAmount) {
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

    public List<ItemStack> getNonEmptyStacks() {
        IItemHandler itemStackHandler = getItemHandler();

        if (itemStackHandler == null) return List.of();

        List<ItemStack> itemStacks = new ArrayList<>(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                itemStacks.add(stackInSlot);
            }
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
                default -> null;
            };
        }

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

    public IEnergyStorage getEnergyHandlerOnSide(Direction direction) {
        return getHandlerOnSide(
                IRCapabilities.EnergyStorage.BLOCK,
                ((handler, supportedActions) -> new SidedEnergyHandler(handler, supportedActions.left())),
                direction,
                getEnergyStorage()
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

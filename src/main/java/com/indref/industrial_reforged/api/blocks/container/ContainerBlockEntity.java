package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.api.util.ValidationFunctions;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Remove optionals
public abstract class ContainerBlockEntity extends BlockEntity implements IEnergyBlock, IHeatBlock {
    private int energyCapacity;
    private int heatCapacity;
    private ItemStackHandler itemHandler;
    private FluidTank fluidTank;
    private EnergyTier energyTier;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void drop() {
        ItemStack[] stacks = getItemHandlerStacks();
        SimpleContainer inventory = new SimpleContainer(stacks);
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void clientTick() {
    }

    public void serverTick() {
    }

    public void commonTick() {
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
    public @NotNull EnergyTier getEnergyTier() {
        return energyTier;
    }

    public @Nullable ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public @Nullable ItemStack[] getItemHandlerStacks() {
        ItemStackHandler itemStackHandler = getItemHandler();
        if (itemStackHandler == null) return null;

        ItemStack[] itemStacks = new ItemStack[itemStackHandler.getSlots()];
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStacks[i] = itemStackHandler.getStackInSlot(i);
        }
        return itemStacks;

    }

    public @Nullable FluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        if (this.getFluidTank() != null) this.getFluidTank().readFromNBT(provider, nbt);
        if (this.getItemHandler() != null)
            this.getItemHandler().deserializeNBT(provider, nbt.getCompound("itemhandler"));
        loadData(nbt, provider);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        if (getFluidTank() != null) getFluidTank().writeToNBT(provider, nbt);
        if (getItemHandler() != null) nbt.put("itemhandler", getItemHandler().serializeNBT(provider));
        saveData(nbt, provider);
    }

    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
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
                update();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return validation.itemValid(slot, stack);
            }
        };
    }

    private void update() {
        if (!level.isClientSide())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    protected final void addFluidTank(int capacityInMb, ValidationFunctions.FluidValid validation) {
        this.fluidTank = new FluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                onFluidsChanged();
                update();
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

    @Override
    public void onEnergyChanged() {
        update();
    }

    @Override
    public void onHeatChanged() {
        update();
    }

    protected final void addHeatStorage(int capacity) {
        this.heatCapacity = capacity;
    }

    protected final void addEnergyStorage(@NotNull EnergyTier energyTier, int capacity) {
        this.energyTier = energyTier;
        this.energyCapacity = capacity;
    }

    protected final void addEnergyStorage(@NotNull EnergyTier energyTier) {
        this.energyTier = energyTier;
        this.energyCapacity = energyTier.getDefaultCapacity();
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

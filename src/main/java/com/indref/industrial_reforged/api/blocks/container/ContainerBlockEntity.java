package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.api.util.ValidationFunctions;
import net.minecraft.core.BlockPos;
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

import java.util.Optional;

public abstract class ContainerBlockEntity extends BlockEntity implements IEnergyBlock, IHeatBlock {
    private int energyCapacity;
    private int heatCapacity;
    private Optional<ItemStackHandler> itemHandler;
    private Optional<FluidTank> fluidTank;
    private Optional<EnergyTier> energyTier;

    public ContainerBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void drops() {
        getItemHandlerStacks().ifPresent(stacks -> {
            SimpleContainer inventory = new SimpleContainer(stacks);
            Containers.dropContents(this.level, this.worldPosition, inventory);
        });
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
    public Optional<EnergyTier> getEnergyTier() {
        return energyTier;
    }

    public Optional<ItemStackHandler> getItemHandler() {
        return itemHandler;
    }

    public Optional<ItemStack[]> getItemHandlerStacks() {
        return this.itemHandler.map(handler -> {
            ItemStack[] itemStacks = new ItemStack[handler.getSlots()];
            for (int i = 0; i < handler.getSlots(); i++) {
                itemStacks[i] = handler.getStackInSlot(i);
            }
            return itemStacks;
        });
    }

    public Optional<FluidTank> getFluidTank() {
        return fluidTank;
    }

    @Override
    protected final void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        getFluidTank().ifPresent(fluidTank1 -> fluidTank1.writeToNBT(p_187471_));
        getItemHandler().ifPresent(itemStackHandler -> p_187471_.put("itemhandler", itemStackHandler.serializeNBT()));
        saveOther(p_187471_);
    }

    @Override
    public final void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        getFluidTank().ifPresent(fluidTank1 -> fluidTank1.readFromNBT(p_155245_));
        getItemHandler().ifPresent(itemStackHandler -> itemStackHandler.deserializeNBT(p_155245_.getCompound("itemhandler")));
        loadOther(p_155245_);
    }

    protected void loadOther(CompoundTag tag) {
    }

    protected void saveOther(CompoundTag tag) {
    }

    protected final void addItemHandler(int slots) {
        addItemHandler(slots, (slot, itemStack) -> true);
    }

    protected final void addFluidTank(int capacityInMb) {
        addFluidTank(capacityInMb, fluidStack -> true);
    }

    protected final void addItemHandler(int slots, ValidationFunctions.ItemValid validation) {
        this.itemHandler = Optional.of(new ItemStackHandler(slots) {
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
        });
    }

    private void update() {
        if (!level.isClientSide())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    protected final void addFluidTank(int capacityInMb, ValidationFunctions.FluidValid validation) {
        this.fluidTank = Optional.of(new FluidTank(capacityInMb) {
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
        });
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

    public void tick() {
    }

    protected final void addHeatStorage(int capacity) {
        this.heatCapacity = capacity;
    }

    protected final void addEnergyStorage(EnergyTier energyTier, int capacity) {
        this.energyTier = Optional.ofNullable(energyTier);
        this.energyCapacity = capacity;
    }

    protected final void addEnergyStorage(EnergyTier energyTier) {
        this.energyTier = Optional.ofNullable(energyTier);
        this.energyCapacity = energyTier.getDefaultCapacity();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}

package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.GeneratorBlockEntity;
import com.indref.industrial_reforged.api.blocks.machine.MachineBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.transportation.energy.EnergyNet;
import com.indref.industrial_reforged.transportation.energy.EnetsSavedData;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.gui.menus.BasicGeneratorMenu;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BasicGeneratorBlockEntity extends MachineBlockEntity implements MenuProvider, GeneratorBlockEntity {
    public static final int GENERATION_AMOUNT = 3;

    private int burnTime;
    private int maxBurnTime;

    public BasicGeneratorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BASIC_GENERATOR.get(), p_155229_, p_155230_);
        addItemHandler(2, (slot, item) -> {
            boolean canInsertFuel = slot == 0 && item.getBurnTime(RecipeType.SMELTING) > 0;
            boolean canInsertBattery = slot == 1 && item.getCapability(IRCapabilities.EnergyStorage.ITEM) != null;
            return canInsertFuel || canInsertBattery;
        });
        addEnergyStorage(EnergyTiers.LOW);
    }

    public boolean isActive() {
        return this.burnTime > 0;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    @Override
    public void onItemsChanged(int slot) {
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
        if (itemHandler != null) {
            ItemStack stack = getItemHandler().getStackInSlot(slot);
            int burnTime = stack.getBurnTime(RecipeType.SMELTING);
            if (burnTime > 0 && this.burnTime <= 0) {
                this.burnTime = burnTime;
                this.maxBurnTime = burnTime;
                stack.shrink(1);
            }
        }
    }

    @Override
    public void commonTick() {
        super.commonTick();
        IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(this);
        if (energyStorage != null) {
            if (this.burnTime > 0) {
                burnTime--;
                energyStorage.tryFillEnergy(GENERATION_AMOUNT);
            } else {
                this.maxBurnTime = 0;
                ItemStack stack = getItemHandler().getStackInSlot(0);
                int burnTime = stack.getBurnTime(RecipeType.SMELTING);
                if (burnTime > 0) {
                    this.burnTime = burnTime;
                    this.maxBurnTime = burnTime;
                    stack.shrink(1);
                }
            }
        }
    }

    @Override
    public void serverTick() {
        super.serverTick();
        IEnergyStorage thisEnergyStorage = CapabilityUtils.energyStorageCapability(this);
        if (level instanceof ServerLevel serverLevel) {
            EnetsSavedData energyNets = EnergyNetUtils.getEnergyNets(serverLevel);
            Optional<EnergyNet> enet = energyNets.getEnets().getNetwork(worldPosition);
            if (enet.isPresent() && thisEnergyStorage.getEnergyStored() - thisEnergyStorage.getEnergyTier().getMaxOutput() >= 0) {
                if (enet.get().distributeEnergy(thisEnergyStorage.getEnergyTier().getMaxOutput())) {
                    thisEnergyStorage.tryDrainEnergy(thisEnergyStorage.getEnergyTier().getMaxOutput());
                }
            }
        }
    }

    @Override
    protected void saveData(CompoundTag pTag, HolderLookup.Provider provider) {
        pTag.putInt("burnTime", burnTime);
        pTag.putInt("maxBurnTime", maxBurnTime);
    }

    @Override
    protected void loadData(CompoundTag pTag, HolderLookup.Provider provider) {
        burnTime = pTag.getInt("burnTime");
        maxBurnTime = pTag.getInt("maxBurnTime");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("title.indref.basic_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BasicGeneratorMenu(i, inventory, this);
    }
}

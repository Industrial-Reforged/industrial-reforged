package com.indref.industrial_reforged.capabilities.energy.network;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.energy.storage.IEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyNetsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final EnergyNets energyNets;
    public final LazyOptional<IEnergyNets> optional;
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(IndustrialReforged.MODID, "energy_nets");
    public EnergyNetsProvider(Level level) {
        this.energyNets = new EnergyNets(level);
        this.optional = LazyOptional.of(() -> energyNets);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return IRCapabilities.ENERGY_NETWORKS.orEmpty(cap, this.optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return energyNets.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        energyNets.deserializeNBT(nbt);
    }
}

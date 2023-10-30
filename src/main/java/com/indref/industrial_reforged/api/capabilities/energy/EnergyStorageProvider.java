package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Exposes the energy capability for use with items and blocks
 */
public class EnergyStorageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public EnergyStorageProvider() {
    }

    public EnergyStorageProvider(ItemStack itemStack) {
        this.energyStorage = new EnergyStorage(itemStack);
    }

    public EnergyStorage energyStorage = new EnergyStorage();
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(IndustrialReforged.MODID, "energy");

    public final LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> energyStorage);

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return IRCapabilities.ENERGY.orEmpty(cap, this.optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.energyStorage.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.energyStorage.deserializeNBT(nbt);
    }
}

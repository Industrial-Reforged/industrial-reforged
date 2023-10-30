package com.indref.industrial_reforged.api.capabilities.heat;

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

public class HeatStorageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public HeatStorageProvider() {
    }

    public HeatStorageProvider(ItemStack itemStack) {
        this.heatStorage = new HeatStorage(itemStack);
    }

    public HeatStorage heatStorage = new HeatStorage();
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(IndustrialReforged.MODID, "heat");

    public final LazyOptional<IHeatStorage> optional = LazyOptional.of(() -> heatStorage);

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return IRCapabilities.HEAT.orEmpty(cap, this.optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.heatStorage.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.heatStorage.deserializeNBT(nbt);
    }
}


package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyStorageCapabilityAttacher {
    private static class EnergyStorageCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation("indref", "energy");

        private final IEnergyStorageCapability backend = new EnergyStorageCapability(10, 10);
        private final LazyOptional<IEnergyStorageCapability> optionalData = LazyOptional.of(() -> backend);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == IRCapabilities.ENERGY) {
                return optionalData.cast();
            }
            return LazyOptional.empty();
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<BlockEntity> event) {
        final EnergyStorageCapabilityProvider provider = new EnergyStorageCapabilityProvider();

        event.addCapability(EnergyStorageCapabilityProvider.IDENTIFIER, provider);
    }

    private EnergyStorageCapabilityAttacher() {
    }
}

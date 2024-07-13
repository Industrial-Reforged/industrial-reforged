package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import io.netty.util.IllegalReferenceCountException;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public final class CapabilityUtils {
    public static <T, C> @Nullable T blockEntityCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        if (blockEntity instanceof ContainerBlockEntity containerBlockEntity) {
            return containerBlockEntity.getCap(cap);
        }
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
    }

    public static @Nullable IEnergyStorage energyStorageCapability(BlockEntity blockEntity) {
        return blockEntityCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity);
    }

    public static @Nullable IHeatStorage heatStorageCapability(BlockEntity blockEntity) {
        return blockEntityCapability(IRCapabilities.HeatStorage.BLOCK, blockEntity);
    }

    public static @Nullable IItemHandler itemHandlerCapability(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
    }

    public static @Nullable IFluidHandler fluidHandlerCapability(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
    }

}

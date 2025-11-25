package com.indref.industrial_reforged.util.capabilities;

import com.indref.industrial_reforged.capabilites.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public final class CapabilityUtils {
    public static <T, C> @Nullable T blockEntityCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
    }

    public static @Nullable EnergyHandler energyStorageCapability(BlockEntity blockEntity) {
        return blockEntityCapability(IRCapabilities.ENERGY_BLOCK, blockEntity);
    }

    public static @Nullable HeatStorage heatStorageCapability(BlockEntity blockEntity) {
        return blockEntityCapability(IRCapabilities.HEAT_BLOCK, blockEntity);
    }

    public static @Nullable IItemHandler itemHandlerCapability(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
    }

    public static @Nullable IFluidHandler fluidHandlerCapability(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
    }

}

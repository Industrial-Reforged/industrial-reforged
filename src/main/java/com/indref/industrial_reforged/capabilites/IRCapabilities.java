package com.indref.industrial_reforged.capabilites;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;


public final class IRCapabilities {
    public static final BlockCapability<HeatStorage, @Nullable Direction> HEAT_BLOCK = BlockCapability.createSided(IndustrialReforged.rl("heat"), HeatStorage.class);
    public static final ItemCapability<HeatStorage, Void> HEAT_ITEM = ItemCapability.createVoid(IndustrialReforged.rl("heat"), HeatStorage.class);

    public static final BlockCapability<EnergyHandler, @Nullable Direction> ENERGY_BLOCK = BlockCapability.createSided(IndustrialReforged.rl("energy"), EnergyHandler.class);
    public static final ItemCapability<EnergyHandler, Void> ENERGY_ITEM = ItemCapability.createVoid(IndustrialReforged.rl("energy"), EnergyHandler.class);

}

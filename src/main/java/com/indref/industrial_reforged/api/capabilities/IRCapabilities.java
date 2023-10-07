package com.indref.industrial_reforged.api.capabilities;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorageCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Used for storing references to all IndustrialReforged capabilities
 */
public class IRCapabilities {
    public static final Capability<IEnergyStorageCapability> ENERGY = CapabilityManager.get(new CapabilityToken<>() {
    });
}

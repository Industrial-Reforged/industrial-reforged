package com.indref.industrial_reforged.api.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Used for registering all capabilities
 */
public class IRCapabilities {
    public static final Capability<IEnergyCapability> ENERGY = CapabilityManager.get(new CapabilityToken<>() {
    });
}

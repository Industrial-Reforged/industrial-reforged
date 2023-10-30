package com.indref.industrial_reforged.api.capabilities;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;

/**
 * Class used for registering and storing
 * references to all capabilities of ind-ref
 */
public class IRCapabilities {
    public static final Capability<IEnergyStorage> ENERGY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IHeatStorage> HEAT = CapabilityManager.get(new CapabilityToken<>() {
    });
}

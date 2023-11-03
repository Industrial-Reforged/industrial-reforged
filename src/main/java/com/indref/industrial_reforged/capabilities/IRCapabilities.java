package com.indref.industrial_reforged.capabilities;

import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.capabilities.energy.storage.IEnergyStorage;
import com.indref.industrial_reforged.capabilities.heat.IHeatStorage;
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
    public static final Capability<IEnergyNets> ENERGY_NETWORKS = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IHeatStorage> HEAT = CapabilityManager.get(new CapabilityToken<>() {
    });
}

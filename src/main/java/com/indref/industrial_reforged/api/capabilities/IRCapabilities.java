package com.indref.industrial_reforged.api.capabilities;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class IRCapabilities {
    public static final Capability<IEnergyStorage> ENERGY = CapabilityManager.get(new CapabilityToken<>() {
    });
}

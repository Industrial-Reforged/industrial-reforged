package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.transportation.energy.NetworkNode;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRNetworks {
    public static final DeferredRegister<TransportNetwork<?>> NETWORKS = DeferredRegister.create(IRRegistries.NETWORK, IndustrialReforged.MODID);

    public static final Supplier<TransportNetwork<Integer>> ENERGY_NETWORK = NETWORKS.register("energy_network",
            () -> TransportNetwork.<Integer>builder(NetworkNode::new)
                    .lossPerBlock(state -> 0.5F)
                    .transferSpeed(state -> 1.5F)
                    .build());
}

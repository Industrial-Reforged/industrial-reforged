package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.capabilites.IRCapabilities;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransferSpeed;
import com.indref.industrial_reforged.content.transportation.EnergyTransportingHandler;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRNetworks {
    public static final DeferredRegister<TransportNetwork<?>> NETWORKS = DeferredRegister.create(IRRegistries.NETWORK, IndustrialReforged.MODID);

    public static final Supplier<TransportNetwork<Integer>> ENERGY_NETWORK = NETWORKS.register("energy_network",
            () -> TransportNetwork.builder(NetworkNode::new, Codec.INT, EnergyTransportingHandler.INSTANCE)
                    .synced(ByteBufCodecs.INT)
                    .lossPerBlock((level, node) -> 0.5F)
                    .transferSpeed(TransferSpeed::instant)
                    .interactorCheck((level, nodePos, direction) ->
                            level.getCapability(IRCapabilities.ENERGY_BLOCK, nodePos.relative(direction), direction) != null
                    )
                    .build());
}

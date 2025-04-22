package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRNetworks {
    public static final DeferredRegister<TransportNetwork<?>> NETWORKS = DeferredRegister.create(IRRegistries.NETWORK, IndustrialReforged.MODID);

    public static final Supplier<TransportNetwork<Integer>> ENERGY_NETWORK = NETWORKS.register("energy_network",
            () -> TransportNetwork.builder(NetworkNode::new, Codec.INT, () -> 0)
                    .synced(ByteBufCodecs.INT)
                    .lossPerBlock(state -> 0.5F)
                    .transferSpeed(state -> 1.5F)
                    .interactorCheck((level, nodePos, direction) ->
                            level.getCapability(IRCapabilities.EnergyStorage.BLOCK, nodePos.relative(direction), direction) != null
                    )
                    .build());
}

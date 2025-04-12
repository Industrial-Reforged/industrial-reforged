package com.indref.industrial_reforged.client.transportation;

import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import net.minecraft.core.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientNodes {
    public static final Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> NODES = new ConcurrentHashMap<>();
}

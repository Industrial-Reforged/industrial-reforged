package com.indref.industrial_reforged.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NetworkingHelper {
    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.SERVER.noArg().send(payload);
    }

    public static void sendToClient(CustomPacketPayload payload) {
        PacketDistributor.ALL.noArg().send(payload);
    }
}

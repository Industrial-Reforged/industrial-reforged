package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.packets.C2SEnergySync;
import com.indref.industrial_reforged.networking.packets.S2CEnergySync;
import com.indref.industrial_reforged.networking.packets.S2CFluidSync;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class IRPackets {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(IndustrialReforged.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(S2CEnergySync.class, id(), PlayNetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CEnergySync::new)
                .encoder(S2CEnergySync::toBytes)
                .consumerMainThread(S2CEnergySync::handle)
                .add();

        net.messageBuilder(C2SEnergySync.class, id(), PlayNetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SEnergySync::new)
                .encoder(C2SEnergySync::toBytes)
                .consumerMainThread(C2SEnergySync::handle)
                .add();

        net.messageBuilder(S2CFluidSync.class, id(), PlayNetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CFluidSync::new)
                .encoder(S2CFluidSync::toBytes)
                .consumerMainThread(S2CFluidSync::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}

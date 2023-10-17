package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.packets.S2CEnergyStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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

        net.messageBuilder(S2CEnergyStorage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CEnergyStorage::new)
                .encoder(S2CEnergyStorage::toBytes)
                .consumerMainThread(S2CEnergyStorage::handle)
                .add();

        IndustrialReforged.LOGGER.info("registering packets");
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
        IndustrialReforged.LOGGER.info("sending to all clients");
    }
}

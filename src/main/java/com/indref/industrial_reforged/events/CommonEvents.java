package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.networking.transportation.SyncNetworkNodePayload;
import com.indref.industrial_reforged.networking.transportation.SyncNextNodePayload;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class CommonEvents {
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).is(IRItems.HAZMAT_BOOTS.get())) {
            event.setDistance(0);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            for (TransportNetwork<?> network : IRRegistries.NETWORK) {
                if (network.isSynced()) {
                    sendSyncPayload(network, serverPlayer);
                }
            }
        }
    }

    private static <T> void sendSyncPayload(TransportNetwork<T> network, ServerPlayer serverPlayer) {
        Map<BlockPos, NetworkNode<T>> serverNodes = network.getServerNodes(serverPlayer.serverLevel());
        PacketDistributor.sendToPlayer(serverPlayer, new SyncNetworkNodePayload<>(network, new HashMap<>(serverNodes)));
        PacketDistributor.sendToPlayer(serverPlayer, new SyncNextNodePayload(network));


    }

}

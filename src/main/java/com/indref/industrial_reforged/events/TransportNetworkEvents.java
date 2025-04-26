package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class TransportNetworkEvents {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

    }
}

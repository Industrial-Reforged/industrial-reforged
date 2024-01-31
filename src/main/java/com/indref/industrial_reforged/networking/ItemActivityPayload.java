package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.networking.data.ItemActivitySyncData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ItemActivityPayload {
    private static final ItemActivityPayload INSTANCE = new ItemActivityPayload();

    public static ItemActivityPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final ItemActivitySyncData data, final PlayPayloadContext ctx) {
        Player player = ctx.player().get();
        player.containerMenu.getSlot(data.slot()).getItem().getOrCreateTag().putBoolean(data.tagName(), data.active());
    }
}

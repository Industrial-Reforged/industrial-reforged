package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.networking.data.ArmorActivitySyncData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ArmorActivityPayload {
    private static final ArmorActivityPayload INSTANCE = new ArmorActivityPayload();

    public static ArmorActivityPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final ArmorActivitySyncData data, final PlayPayloadContext ctx) {
        Player player = ctx.player().get();
        player.getItemBySlot(data.slot()).getOrCreateTag().putBoolean("active", data.active());
    }
}

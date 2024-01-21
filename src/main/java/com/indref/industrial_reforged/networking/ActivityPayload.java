package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.networking.data.ActivitySyncData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ActivityPayload {
    private static final ActivityPayload INSTANCE = new ActivityPayload();

    public static ActivityPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final ActivitySyncData data, final PlayPayloadContext ctx) {
        Player player = ctx.player().get();
        player.getItemBySlot(data.slot()).getOrCreateTag().putBoolean("active", data.active());
    }
}

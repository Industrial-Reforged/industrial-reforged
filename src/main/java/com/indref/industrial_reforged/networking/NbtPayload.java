package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.networking.data.ItemNbtSyncData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class NbtPayload {
    private static final NbtPayload INSTANCE = new NbtPayload();

    public static NbtPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final ItemNbtSyncData data, final PlayPayloadContext ctx) {
        Player player = ctx.player().get();
        player.containerMenu.getSlot(data.slot()).getItem().getOrCreateTag().put(data.tagName(), data.nbt());
    }
}

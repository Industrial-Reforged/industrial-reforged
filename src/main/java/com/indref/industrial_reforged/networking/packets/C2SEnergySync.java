package com.indref.industrial_reforged.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.NetworkEvent;

public class C2SEnergySync {
    public C2SEnergySync() {
    }

    public C2SEnergySync(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(NetworkEvent.Context supplier) {
        supplier.enqueueWork(() -> {
            ServerPlayer player = supplier.getSender();
            assert player != null;
            ItemStack mainHandItem = player.getMainHandItem();
        });
        return true;
    }
}

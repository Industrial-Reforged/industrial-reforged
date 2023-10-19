package com.indref.industrial_reforged.networking.packets;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SEnergySync {
    public C2SEnergySync() {
    }

    public C2SEnergySync(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ItemStack mainHandItem = player.getMainHandItem();
            IEnergyItem mainHandEnergyItem = (IEnergyItem) mainHandItem.getItem();
            mainHandEnergyItem.setStored(mainHandItem, mainHandEnergyItem.getStored(mainHandItem)-1);
        });
        return true;
    }
}

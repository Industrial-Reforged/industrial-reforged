package com.indref.industrial_reforged.networking.packets;

import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CEnergyItem {
    private final int energy;
    private final ItemStack itemStack;

    public S2CEnergyItem(int energy, ItemStack itemStack) {
        this.energy = energy;
        this.itemStack = itemStack;
    }

    public S2CEnergyItem(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.itemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeItem(itemStack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            for (ItemStack item : Minecraft.getInstance().player.getInventory().items) {
                if (item.getItem() instanceof IEnergyItem energyItem) {
                    energyItem.setStoredEnergy(itemStack, energy);
                }
            }
        });
        return true;
    }
}

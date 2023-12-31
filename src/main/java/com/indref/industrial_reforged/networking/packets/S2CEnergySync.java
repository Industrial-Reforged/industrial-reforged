package com.indref.industrial_reforged.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.NetworkEvent;

public class S2CEnergySync {
    private final int energy;
    private final BlockPos pos;

    public S2CEnergySync(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public S2CEnergySync(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle(NetworkEvent.Context supplier) {
        supplier.enqueueWork(() -> {
            BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(pos);
        });
        return true;
    }
}


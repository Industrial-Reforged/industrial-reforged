package com.indref.industrial_reforged.networking.packets;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(pos);
            if (entity instanceof IEnergyBlock blockEntity) {
                blockEntity.setStored(entity, energy);
            }
        });
        return true;
    }
}


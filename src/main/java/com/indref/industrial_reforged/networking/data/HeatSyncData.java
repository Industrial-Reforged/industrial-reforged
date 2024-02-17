package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record HeatSyncData(int heat, BlockPos blockPos) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "heat_sync_data");

    public HeatSyncData(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(heat());
        buffer.writeBlockPos(blockPos());
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}

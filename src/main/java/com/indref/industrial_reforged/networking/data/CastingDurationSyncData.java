package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CastingDurationSyncData(int duration, int maxDuration, BlockPos blockPos) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "casting_duration_sync_data");

    public CastingDurationSyncData(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(duration);
        buffer.writeInt(maxDuration);
        buffer.writeBlockPos(blockPos);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}

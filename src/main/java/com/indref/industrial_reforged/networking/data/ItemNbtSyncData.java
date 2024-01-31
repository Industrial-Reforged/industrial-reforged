package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ItemNbtSyncData(int slot, String tagName, CompoundTag nbt) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "nbt_sync_data");

    public ItemNbtSyncData(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readUtf(), buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(slot);
        buffer.writeUtf(tagName);
        buffer.writeNbt(nbt);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}

package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public record ItemActivitySyncData(int slot, String tagName, boolean active)  implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "item_activity_sync_data");

    public ItemActivitySyncData(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readUtf(), buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(slot());
        buffer.writeUtf(tagName);
        buffer.writeBoolean(active());
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}

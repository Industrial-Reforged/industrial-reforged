package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public record ActivitySyncData(EquipmentSlot slot, boolean active)  implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "activity_sync_data");

    public ActivitySyncData(final FriendlyByteBuf buffer) {
        this(ItemUtils.equipmentSlotFromIndex(buffer.readInt()), buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(ItemUtils.indexFromEquipmentSlot(slot()));
        buffer.writeBoolean(active());
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}

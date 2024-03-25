package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record CastingGhostItemSyncData(ItemStack itemStack, BlockPos blockPos) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "casting_ghost_item_sync_data");
    public CastingGhostItemSyncData(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeItem(itemStack);
        friendlyByteBuf.writeBlockPos(blockPos);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}

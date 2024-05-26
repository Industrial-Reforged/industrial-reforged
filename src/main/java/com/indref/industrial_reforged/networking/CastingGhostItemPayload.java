package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record CastingGhostItemPayload(ItemStack itemStack, BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<CastingGhostItemPayload> TYPE = new Type<>(new ResourceLocation(IndustrialReforged.MODID, "casting_ghost_item_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CastingGhostItemPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            CastingGhostItemPayload::itemStack,
            BlockPos.STREAM_CODEC,
            CastingGhostItemPayload::blockPos,
            CastingGhostItemPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

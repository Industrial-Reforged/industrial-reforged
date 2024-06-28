package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemActivityPayload(ItemStack itemStack, boolean active)  implements CustomPacketPayload {
    public static final Type<ItemActivityPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "item_activity_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemActivityPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ItemActivityPayload::itemStack,
            ByteBufCodecs.BOOL,
            ItemActivityPayload::active,
            ItemActivityPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

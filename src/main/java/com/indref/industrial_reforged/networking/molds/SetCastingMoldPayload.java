package com.indref.industrial_reforged.networking.molds;

import com.indref.industrial_reforged.IndustrialReforged;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetCastingMoldPayload(Item moldItem) implements CustomPacketPayload {
    public static final Type<SetCastingMoldPayload> TYPE = new Type<>(IndustrialReforged.rl("set_casting_mold"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SetCastingMoldPayload> STREAM_CODEC = CodecUtils.registryStreamCodec(BuiltInRegistries.ITEM).map(SetCastingMoldPayload::new, SetCastingMoldPayload::moldItem);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            int count = player.getMainHandItem().getCount();
            player.setItemInHand(InteractionHand.MAIN_HAND, moldItem.getDefaultInstance().copyWithCount(count));
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle SetCastingMoldPayload", err);
            return null;
        });
    }

}

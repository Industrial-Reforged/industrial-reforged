package com.indref.industrial_reforged.networking.molds;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.hud.CastingMoldSelectionOverlay;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SyncCastingMoldsPayload(List<Item> castingMoldItems) implements CustomPacketPayload {
    public static final Type<SyncCastingMoldsPayload> TYPE = new Type<>(IndustrialReforged.rl("sync_casting_molds"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SyncCastingMoldsPayload> STREAM_CODEC = CodecUtils.registryStreamCodec(BuiltInRegistries.ITEM).apply(ByteBufCodecs.list()).map(SyncCastingMoldsPayload::new, SyncCastingMoldsPayload::castingMoldItems);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            CastingMoldSelectionOverlay.CASTING_MOLD_ITEMS.clear();
            CastingMoldSelectionOverlay.CASTING_MOLD_ITEMS.addAll(this.castingMoldItems);
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Encountered error while handling SyncCastingMolds payload");
            return null;
        });
    }

}

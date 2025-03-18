package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.PowerableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PowerBlockEntityPayload(BlockPos blockPos, boolean powered) implements CustomPacketPayload {
    public static final Type<PowerBlockEntityPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "power_block_entity_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerBlockEntityPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PowerBlockEntityPayload::blockPos,
            ByteBufCodecs.BOOL,
            PowerBlockEntityPayload::powered,
            PowerBlockEntityPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (level.getBlockEntity(blockPos) instanceof PowerableBlockEntity be) {
                be.setPowered(powered);
            }
        });
    }
}


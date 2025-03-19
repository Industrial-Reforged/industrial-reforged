package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blockentities.CastingBasinBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BasinFluidChangedPayload(BlockPos pos, int amount) implements CustomPacketPayload {
    public static final Type<BasinFluidChangedPayload> TYPE = new Type<>(IndustrialReforged.rl("basin_fluid_changed"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BasinFluidChangedPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BasinFluidChangedPayload::pos,
            ByteBufCodecs.INT,
            BasinFluidChangedPayload::amount,
            BasinFluidChangedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext iPayloadContext) {
        Level level = iPayloadContext.player().level();
        if (level.getBlockEntity(pos) instanceof CastingBasinBlockEntity containerBlockEntity) {
            containerBlockEntity.onClientFluidChanged(amount);
        }
    }
}

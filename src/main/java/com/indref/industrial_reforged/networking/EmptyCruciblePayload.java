package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EmptyCruciblePayload(BlockPos pos) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, EmptyCruciblePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            EmptyCruciblePayload::pos,
            EmptyCruciblePayload::new
    );
    public static final Type<EmptyCruciblePayload> TYPE = new Type<>(IndustrialReforged.rl("empty_crucible"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (level.getBlockEntity(pos) instanceof CrucibleBlockEntity be) {
                IFluidHandler tank = be.getFluidHandler();
                tank.drain(tank.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Error handling empty crucible Payload", err);
            return null;
        });
    }
}

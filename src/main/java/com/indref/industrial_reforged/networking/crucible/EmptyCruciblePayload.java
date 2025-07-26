package com.indref.industrial_reforged.networking.crucible;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
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
            IFluidHandler tank = CapabilityUtils.fluidHandlerCapability(level.getBlockEntity(pos));
            if (tank != null) {
                tank.drain(tank.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Error handling empty crucible Payload", err);
            return null;
        });
    }
}

package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blockentities.misc.FaucetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FaucetSetRenderStack(BlockPos pos, FluidStack stack) implements CustomPacketPayload {
    public static final Type<FaucetSetRenderStack> TYPE = new Type<>(IndustrialReforged.rl("faucet_set_render_stack"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FaucetSetRenderStack> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            FaucetSetRenderStack::pos,
            FluidStack.OPTIONAL_STREAM_CODEC,
            FaucetSetRenderStack::stack,
            FaucetSetRenderStack::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (level.getBlockEntity(pos) instanceof FaucetBlockEntity be) {
                be.setRenderStack(stack);
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle set render stack payload", err);
            return null;
        });
    }
}

package com.indref.industrial_reforged.networking.crucible;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record CrucibleTurnPayload(BlockPos controllerPos, boolean forward) implements CustomPacketPayload {
    public static final Type<CrucibleTurnPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_turn_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleTurnPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrucibleTurnPayload::controllerPos,
            ByteBufCodecs.BOOL,
            CrucibleTurnPayload::forward,
            CrucibleTurnPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            CrucibleBlockEntity be = BlockUtils.getBE(level, controllerPos, CrucibleBlockEntity.class);
            if (forward) {
                be.turn();
            } else {
                be.turnBack();
            }
        });
    }
}


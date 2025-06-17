package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.api.gui.slots.SlotAccessor;
import com.indref.industrial_reforged.api.gui.slots.UpgradeSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record UpgradeWidgetSetSlotPositionsPayload(int startY) implements CustomPacketPayload {
    public static final Type<UpgradeWidgetSetSlotPositionsPayload> TYPE = new Type<>(IndustrialReforged.rl("upgrade_widget_set_slot_positions"));
    public static final StreamCodec<ByteBuf, UpgradeWidgetSetSlotPositionsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UpgradeWidgetSetSlotPositionsPayload::startY,
            UpgradeWidgetSetSlotPositionsPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof MachineContainerMenu<?> machineMenu) {
                machineMenu.setUpgradeSlotPositions(this.startY);
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle UpgradeWidgetSetSlotPositions payload", err);
            return null;
        });
    }
}